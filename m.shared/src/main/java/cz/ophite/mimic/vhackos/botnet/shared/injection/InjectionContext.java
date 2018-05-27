package cz.ophite.mimic.vhackos.botnet.shared.injection;

import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Kontext pro dependency injection. Drží všechny instance tříd, které jsou označené pro inject.
 *
 * @author mimic
 */
public final class InjectionContext {

    private static final Logger LOG = LoggerFactory.getLogger(InjectionContext.class);

    private static final InjectionContext INSTANCE = new InjectionContext();

    private static Map<String, Object> injects;
    private static Map<Class, String> lazyClasses;
    private static Map<Class, IInjectRule> rules;

    private InjectionContext() {
    }

    /**
     * Získá instanci inject kontextu.
     */
    public static InjectionContext getInstance() {
        return INSTANCE;
    }

    /**
     * Initializuje kontext a vytvoří instance všech tříd, které mají Inject anotaci. Všechny třídy musí mít
     * konstruktor bez parametru.
     */
    public static synchronized void initialize(String[] scanPackages) {
        initialize(scanPackages, null);
    }

    /**
     * Initializuje kontext a vytvoří instance všech tříd, které mají Inject anotaci.<br>
     * Pokud pravidla budou obsahovat jako klič hodnotu null, tak toto pravidlo bude použito automaticky pro všechny
     * třídy, pro které neexistuje pravidlo a nemají bezparametrický konstruktor.
     */
    public static synchronized void initialize(String[] scanPackages, Map<Class, IInjectRule> rules) {
        if (injects != null) {
            LOG.warn("Context has already been created");
            return;
        }
        injects = new HashMap<>();
        lazyClasses = new HashMap<>();
        InjectionContext.rules = rules;

        for (var pkg : scanPackages) {
            LOG.debug("Scan the package: {}", pkg);
            var ref = new Reflections(pkg, new TypeAnnotationsScanner());
            var classes = ref.getTypesAnnotatedWith(Inject.class, true);
            LOG.debug("{} classes with an '{}' annotation were found", classes.size(), Inject.class.getSimpleName());

            // projde všechny nalezené třídy
            for (var clazz : classes) {
                var name = clazz.getSimpleName();
                var alias = getClassAlias(clazz);
                var ctors = clazz.getDeclaredConstructors();
                var a = clazz.getDeclaredAnnotation(Inject.class);

                LOG.debug("Class '{}' has {} constructors", name, ctors.length);
                try {
                    // alias třídy musí být unikátní
                    if (injects.containsKey(alias)) {
                        var aliasInstance = injects.get(alias);
                        throw new IllegalStateException("An instance of an '" + name + "' class with an alias '" + alias + "'" + " already exists and is assigned to the '" + aliasInstance
                                .getClass().getSimpleName() + "' class");
                    }
                    if (a.lazy()) {
                        if (!lazyClasses.containsKey(clazz)) {
                            lazyClasses.put(clazz, alias);
                            LOG.debug("The class '{}' was added for later initialization", name);
                        } else {
                            LOG.error("The class '{}' for lazy init already exists", name);
                        }
                    } else {
                        var instance = createInstanceOf(clazz, rules);
                        if (instance != null) {
                            injects.put(alias, instance);
                            LOG.debug("An instance of an '{}' class with a parametric constructor was created", name);
                        } else {
                            throw new IllegalStateException("Class '" + name + "' instance is null");
                        }
                    }
                } catch (Exception e) {
                    LOG.error("Could not create class '" + name + "' instances", e);
                }
            }
        }
        processAutowireds();
        processPostConstructs();
    }

    /**
     * Pomocná metoda pro získání správného konstruktoru i když je privátní.
     */
    public static Constructor getConstructor(Class clazz, Class... params) throws NoSuchMethodException {
        var ctor = clazz.getDeclaredConstructor(params);
        ctor.setAccessible(true);
        return ctor;
    }

    /**
     * Donačte všechny inject třídy, které byly označené jako Inject a jsou lazy.
     */
    public static synchronized void initLazyClasses() {
        if (injects == null) {
            throw new IllegalStateException("Context was not initialized");
        }
        if (!lazyClasses.isEmpty()) {
            for (var entry : lazyClasses.entrySet()) {
                var clazz = entry.getKey();
                var name = clazz.getSimpleName();

                try {
                    var instance = createInstanceOf(clazz, rules);
                    if (instance != null) {
                        injects.put(entry.getValue(), instance);
                        recursiveProcessAutowireds(clazz, instance);
                        recursiveProcessPostConstructs(clazz, instance);
                        LOG.debug("An instance of an '{}' class with a parametric constructor was created", name);
                    } else {
                        throw new IllegalStateException("Class '" + name + "' instance is null");
                    }
                } catch (Exception e) {
                    LOG.error("Could not create class '" + name + "' instances", e);
                }
            }
            lazyClasses.clear();
        }
    }

    /**
     * Donačte všechny fieldy, které mají anotaci Autowired a zavolá PostConstruct. Vstupní třída NEMUSÍ být označena
     * injectem.
     */
    public static void lazyInit(Object classInstance) {
        if (classInstance == null) {
            throw new NullPointerException("The class input parameter must not be null");
        }
        if (injects == null) {
            throw new IllegalStateException("Context was not initialized");
        }
        recursiveProcessAutowireds(classInstance.getClass(), classInstance);
        recursiveProcessPostConstructs(classInstance.getClass(), classInstance);
    }

    /**
     * Získá instanci třídy.
     */
    public <T> T get(Class<T> clazz) {
        if (clazz == null) {
            throw new NullPointerException("The class input parameter must not be null");
        }
        if (injects == null) {
            throw new IllegalStateException("Context was not initialized");
        }
        var a = clazz.getAnnotation(Inject.class);
        if (a == null) {
            LOG.warn("The '{}' class has no annotation for injection", clazz.getSimpleName());
            return null;
        }
        var alias = getClassAlias(clazz);

        if (injects.containsKey(alias)) {
            return (T) injects.get(alias);
        }
        LOG.error("An instance of '{}' class does not exist in the context", clazz.getSimpleName());
        return null;
    }

    /**
     * Získá instanci třídy.
     */
    public <T> T get(String alias) {
        if (alias == null) {
            throw new NullPointerException("The input class alias must not be null");
        }
        if (injects == null) {
            throw new IllegalStateException("Context was not initialized");
        }
        if (injects.containsKey(alias)) {
            return (T) injects.get(alias);
        }
        LOG.error("An instance of '{}' alias does not exist in the context", alias);
        return null;
    }

    /**
     * Získá seznam tříd, které jsou k dispozici.
     */
    public List<String> getInjectedClasses() {
        if (injects == null) {
            throw new IllegalStateException("Context was not initialized");
        }
        List<String> list = new ArrayList<>();

        for (var entry : injects.entrySet()) {
            list.add(entry.getKey());
        }
        return list;
    }

    /**
     * Získá třídu podle aliasu.
     */
    public Class getClassByAlias(String alias) {
        if (alias == null) {
            throw new NullPointerException("The input alias must not be null");
        }
        if (injects == null) {
            throw new IllegalStateException("Context was not initialized");
        }
        var instance = injects.get(alias);
        return (instance != null) ? instance.getClass() : null;
    }

    private static Object createInstanceOf(Class clazz, Map<Class, IInjectRule> rules) throws Exception {
        var ctors = clazz.getDeclaredConstructors();
        var name = clazz.getSimpleName();
        Object instance = null;

        // existuje konstruktor bez parametru
        if (ctors.length == 1 && ctors[0].getParameterCount() == 0) {
            var ctor = getConstructor(clazz);
            instance = ctor.newInstance();
        } else {
            // třída má více konstruktorů nebo nemá bez parametru - vyhledá pravidlo pro vytvoření instance
            if (rules != null && (rules.containsKey(clazz) || rules.containsKey(null))) {
                var value = rules.containsKey(clazz) ? rules.get(clazz) : rules.get(null);
                instance = value.createInstance(clazz);
            } else {
                // konstruktor má parametry, ale neexistuje pravidlo pro vytvoření
                if (ctors.length == 1) {
                    LOG.error("There is no rule for creating an instance of an '{}' class. The class constructor has the following parameters: {}", name, ctors[0]
                            .getParameterTypes());
                } else {
                    LOG.error("There is no rule for creating an instance of an '{}' class. The class has multiple constructors with different parameters", name);
                }
            }
        }
        return instance;
    }

    private static void processAutowireds() {
        var instances = injects.values();

        for (var instance : instances) {
            recursiveProcessAutowireds(instance.getClass(), instance);
        }
    }

    private static void recursiveProcessAutowireds(Class clazz, Object instance) {
        var instanceName = clazz.getName();

        if (!clazz.getSuperclass().equals(Object.class)) {
            recursiveProcessAutowireds(clazz.getSuperclass(), instance);
        }
        var fields = clazz.getDeclaredFields();

        for (var field : fields) {
            if (field.isAnnotationPresent(Autowired.class)) {
                LOG.debug("A field '{}.{}' with '{}' annotation in the class '{}' was found", instanceName, field
                        .getName(), Autowired.class.getSimpleName(), instance.getClass().getSimpleName());
                var alias = getClassAlias(field.getType());

                // alias může but null v případě, že se pokusíme připojit třídu, která nemá anotaci Inject
                if (alias != null && injects.containsKey(alias)) {
                    field.setAccessible(true);
                    try {
                        if (field.get(instance) == null) {
                            field.set(instance, injects.get(alias));
                            LOG.debug("Field '{}.{}' was initialized", instanceName, field.getName());
                        }
                    } catch (Exception e) {
                        LOG.error("An error occurred while initializing the '" + instanceName + "." + field
                                .getName() + "' field", e);
                    }
                } else if (lazyClasses.containsKey(field.getType())) {
                    LOG.debug("Field '{}.{}' could not be initialized because the expected object '{}' is marked " + "as lazy inject", instanceName, field
                            .getName(), field.getType().getName());
                } else {
                    LOG.error("Field '{}.{}' could not be initialized because the expected object '{}' is not " + "marked " + "with an '{}' annotation", instanceName, field
                            .getName(), field.getType().getName(), Inject.class.getSimpleName());
                }
            }
        }
    }

    private static void processPostConstructs() {
        var instances = injects.values();

        for (var instance : instances) {
            recursiveProcessPostConstructs(instance.getClass(), instance);
        }
    }

    private static void recursiveProcessPostConstructs(Class clazz, Object instance) {
        var instanceName = clazz.getName();

        if (!clazz.getSuperclass().equals(Object.class)) {
            recursiveProcessPostConstructs(clazz.getSuperclass(), instance);
        } else {
            var methods = clazz.getDeclaredMethods();

            for (var method : methods) {
                if (method.isAnnotationPresent(PostConstruct.class)) {
                    LOG.debug("A method '{}.{}' with '{}' annotation in the class '{}' was found", instanceName, method
                            .getName(), PostConstruct.class.getSimpleName(), instance.getClass().getSimpleName());
                    try {
                        method.setAccessible(true);

                        if (method.getParameterCount() == 1 && method.getParameterTypes()[0]
                                .equals(InjectionContext.class)) {
                            method.invoke(instance, InjectionContext.INSTANCE);
                        } else {
                            method.invoke(instance);
                        }
                    } catch (Exception e) {
                        LOG.error("There was an error when calling the PostConstruct '" + method
                                .getName() + "" + "." + instanceName + "' method", e);
                    }
                }
            }
        }
    }

    private static String getClassAlias(Class clazz) {
        if (clazz.isAnnotationPresent(Inject.class)) {
            Inject a = (Inject) clazz.getAnnotation(Inject.class);
            return a.value().isEmpty() ? clazz.getName() : a.value();
        }
        return null;
    }
}
