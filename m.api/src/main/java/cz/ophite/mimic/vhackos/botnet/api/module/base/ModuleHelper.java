package cz.ophite.mimic.vhackos.botnet.api.module.base;

import com.google.gson.internal.LinkedTreeMap;
import cz.ophite.mimic.vhackos.botnet.api.net.response.base.Response;
import cz.ophite.mimic.vhackos.botnet.api.net.response.base.ResponseKey;
import cz.ophite.mimic.vhackos.botnet.api.net.response.data.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Pomocná třída pro moduly.
 *
 * @author mimic
 */
public final class ModuleHelper {

    private static final Logger LOG = LoggerFactory.getLogger(ModuleHelper.class);

    private static final Map<Class, List<String>> RESPONSE_CLASS_CACHE = new HashMap<>();

    /**
     * Překonvertuje jednotlivé mise.
     */
    public static List<MissionItemData> convertToMissionItemData(Object data) {
        var out = new ArrayList<MissionItemData>();
        var list = ((ArrayList) data);

        for (var item : list) {
            var entries = ((LinkedTreeMap<String, Object>) item).entrySet();
            var m = new MissionItemData();

            for (var entry : entries) {
                checkResponseIntegrity(entry.getKey(), MissionItemData.class);
                setField(entry, m, MissionItemData.P_TITLE);
                setField(entry, m, MissionItemData.P_DESCRIPTION);
                setField(entry, m, MissionItemData.P_FINISHED);
                setField(entry, m, MissionItemData.P_EXPERIENCE);
                setField(entry, m, MissionItemData.P_REWARD_TYPE);
                setField(entry, m, MissionItemData.P_REWARD_AMOUNT);
            }
            out.add(m);
        }
        return out;
    }

    /**
     * Překonvertuje položky pro leaderboard.
     */
    public static List<LeaderboardData> convertToLeaderboardData(Object data) {
        var out = new ArrayList<LeaderboardData>();
        var list = ((ArrayList) data);

        for (var item : list) {
            var entries = ((LinkedTreeMap<String, Object>) item).entrySet();
            var lb = new LeaderboardData();

            for (var entry : entries) {
                checkResponseIntegrity(entry.getKey(), LeaderboardData.class);
                setField(entry, lb, LeaderboardData.P_USER);
                setField(entry, lb, LeaderboardData.P_EXP_PERCENT);
                setField(entry, lb, LeaderboardData.P_LEVEL);
            }
            out.add(lb);
        }
        return out;
    }

    /**
     * Překonvertuje položky pro 24H turnaj v leaderboardu.
     */
    public static List<Tournament24HData> convertToTournament24HData(Object data) {
        var out = new ArrayList<Tournament24HData>();
        var list = ((ArrayList) data);

        for (var item : list) {
            var entries = ((LinkedTreeMap<String, Object>) item).entrySet();
            var t = new Tournament24HData();

            for (var entry : entries) {
                checkResponseIntegrity(entry.getKey(), Tournament24HData.class);
                setField(entry, t, Tournament24HData.P_USER);
                setField(entry, t, Tournament24HData.P_EXP_GAIN);
                setField(entry, t, Tournament24HData.P_LEVEL);
            }
            out.add(t);
        }
        return out;
    }

    /**
     * Překonvertuje položky pro crew v leaderboardu.
     */
    public static List<LeaderboardCrewData> convertToLeaderboardCrewData(Object data) {
        var out = new ArrayList<LeaderboardCrewData>();
        var list = ((ArrayList) data);

        for (var item : list) {
            var entries = ((LinkedTreeMap<String, Object>) item).entrySet();
            var crew = new LeaderboardCrewData();

            for (var entry : entries) {
                checkResponseIntegrity(entry.getKey(), LeaderboardCrewData.class);
                setField(entry, crew, LeaderboardCrewData.P_CREW_NAME);
                setField(entry, crew, LeaderboardCrewData.P_CREW_TAG);
                setField(entry, crew, LeaderboardCrewData.P_CREW_REPUTATION);
                setField(entry, crew, LeaderboardCrewData.P_CREW_ID);
                setField(entry, crew, LeaderboardCrewData.P_MEMBERS);
            }
            out.add(crew);
        }
        return out;
    }

    /**
     * Překonvertuje data do sken IP dat.
     */
    public static List<IpScanData> convertToIpScanData(Object data) {
        var out = new ArrayList<IpScanData>();
        var list = ((ArrayList) data);

        for (var item : list) {
            var entries = ((LinkedTreeMap<String, Object>) item).entrySet();
            var ip = new IpScanData();

            for (var entry : entries) {
                checkResponseIntegrity(entry.getKey(), IpScanData.class);
                setField(entry, ip, IpScanData.P_IP);
                setField(entry, ip, IpScanData.P_LEVEL);
                setField(entry, ip, IpScanData.P_FIREWALL);
                setField(entry, ip, IpScanData.P_OPEN);
            }
            out.add(ip);
        }
        return out;
    }

    /**
     * Překonvertuje data do prolomený IP dat.
     */
    public static List<IpBruteData> convertToIpBruteData(Object data) {
        var out = new ArrayList<IpBruteData>();
        var list = ((ArrayList) data);

        for (var item : list) {
            var entries = ((LinkedTreeMap<String, Object>) item).entrySet();
            var ip = new IpBruteData();

            for (var entry : entries) {
                checkResponseIntegrity(entry.getKey(), IpBruteData.class);
                setField(entry, ip, IpBruteData.P_IP);
                setField(entry, ip, IpBruteData.P_USER_NAME);
                setField(entry, ip, IpBruteData.P_BRUTE);
            }
            out.add(ip);
        }
        return out;
    }

    /**
     * Překonvertuje data do detailní prolomených IP.
     */
    public static List<IpBruteDetailData> convertToIpBruteDetailData(Object data) {
        var out = new ArrayList<IpBruteDetailData>();
        var list = ((ArrayList) data);

        for (var item : list) {
            var entries = ((LinkedTreeMap<String, Object>) item).entrySet();
            var ip = new IpBruteDetailData();

            for (var entry : entries) {
                checkResponseIntegrity(entry.getKey(), IpBruteDetailData.class);
                setField(entry, ip, IpBruteDetailData.P_BRUTE_ID);
                setField(entry, ip, IpBruteDetailData.P_IP);
                setField(entry, ip, IpBruteDetailData.P_START_TIME);
                setField(entry, ip, IpBruteDetailData.P_END_TIME);
                setField(entry, ip, IpBruteDetailData.P_CURRENT_TIME);
                setField(entry, ip, IpBruteDetailData.P_USER_NAME);
                setField(entry, ip, IpBruteDetailData.P_RESULT);
            }
            out.add(ip);
        }
        return out;
    }

    /**
     * Překonvertuje data na aktualizovaný tásk.
     */
    public static List<TaskUpdateData> convertToTaskUpdateData(Object data) {
        var out = new ArrayList<TaskUpdateData>();
        var list = ((ArrayList) data);

        for (var item : list) {
            var entries = ((LinkedTreeMap<String, Object>) item).entrySet();
            var t = new TaskUpdateData();

            for (var entry : entries) {
                checkResponseIntegrity(entry.getKey(), TaskUpdateData.class);
                setField(entry, t, TaskUpdateData.P_TASK_ID);
                setField(entry, t, TaskUpdateData.P_APP_ID);
                setField(entry, t, TaskUpdateData.P_START);
                setField(entry, t, TaskUpdateData.P_END);
                setField(entry, t, TaskUpdateData.P_NOW);
                setField(entry, t, TaskUpdateData.P_LEVEL);
            }
            out.add(t);
        }
        return out;
    }

    /**
     * Překonvertuje data do bankovních transakcí.
     */
    public static List<BankTransactionData> convertToTransactionData(Object data) {
        var out = new ArrayList<BankTransactionData>();
        var list = ((ArrayList) data);

        for (var item : list) {
            var entries = ((LinkedTreeMap<String, Object>) item).entrySet();
            var t = new BankTransactionData();

            for (var entry : entries) {
                checkResponseIntegrity(entry.getKey(), BankTransactionData.class);
                setField(entry, t, BankTransactionData.P_TIME);
                setField(entry, t, BankTransactionData.P_FROM_ID);
                setField(entry, t, BankTransactionData.P_FROM_IP);
                setField(entry, t, BankTransactionData.P_TO_ID);
                setField(entry, t, BankTransactionData.P_TO_IP);
                setField(entry, t, BankTransactionData.P_AMOUNT);
            }
            out.add(t);
        }
        return out;
    }

    /**
     * Překonvertuje data do infromací o aplikaci v obchodu.
     */
    public static List<AppStoreData> convertToAppStoreData(Object data) {
        var out = new ArrayList<AppStoreData>();
        var list = ((ArrayList) data);

        for (var item : list) {
            var entries = ((LinkedTreeMap<String, Object>) item).entrySet();
            var app = new AppStoreData();

            for (var entry : entries) {
                checkResponseIntegrity(entry.getKey(), AppStoreData.class);
                setField(entry, app, AppStoreData.P_PRICE);
                setField(entry, app, AppStoreData.P_BASE_PRICE);
                setField(entry, app, AppStoreData.P_FACTOR);
                setField(entry, app, AppStoreData.P_APP_ID);
                setField(entry, app, AppStoreData.P_LEVEL);
                setField(entry, app, AppStoreData.P_REQUIRE_LEVEL);
                setField(entry, app, AppStoreData.P_MAX_LEVEL);
                setField(entry, app, AppStoreData.P_RUNNING);
            }
            out.add(app);
        }
        return out;
    }

    /**
     * Překonvertuje data do seznamu SKU.
     */
    public static List<String> convertToSKUsData(Object data) {
        var out = new ArrayList<String>();
        var list = ((ArrayList) data);

        for (var item : list) {
            var entries = ((LinkedTreeMap<String, Object>) item).entrySet();

            for (var entry : entries) {
                out.add(entry.getValue().toString());
            }
        }
        return out;
    }

    /**
     * Překonvertuje data do seznamu položek nákupu.
     */
    public static List<BuyItemData> convertToBuyItemData(Object data) {
        var out = new ArrayList<BuyItemData>();
        var list = ((ArrayList) data);

        for (var item : list) {
            var entries = ((LinkedTreeMap<String, Object>) item).entrySet();
            var bi = new BuyItemData();

            for (var entry : entries) {
                checkResponseIntegrity(entry.getKey(), BuyItemData.class);
                setField(entry, bi, BuyItemData.P_SKU);
                setField(entry, bi, BuyItemData.P_PRICE);
            }
            out.add(bi);
        }
        return out;
    }

    /**
     * Přemapuje odpověd do fieldu v DTO.
     */
    public static boolean setField(Map<String, Object> response, Object dto, String fieldName) {
        return setField(response, dto, fieldName, null);
    }

    /**
     * Přemapuje odpověd do fieldu v DTO.
     */
    public static boolean setField(Map<String, Object> response, Object dto, String fieldName, IFieldMapper mapper) {
        boolean set = false;

        for (var entry : response.entrySet()) {
            if (setField(entry, dto, fieldName, mapper)) {
                set = true;
            }
        }
        return set;
    }

    /**
     * Přemapuje entry do fieldu v DTO.
     */
    public static boolean setField(Map.Entry<String, Object> entry, Object dto, String fieldName) {
        return setField(entry, dto, fieldName, null);
    }

    /**
     * Přemapuje entry do fieldu v DTO.
     */
    public static boolean setField(Map.Entry<String, Object> entry, Object dto, String fieldName, IFieldMapper mapper) {
        try {
            var field = dto.getClass().getDeclaredField(fieldName);

            if (field.isAnnotationPresent(ResponseKey.class)) {
                var a = field.getDeclaredAnnotation(ResponseKey.class);
                var type = field.getGenericType();

                if (entry.getKey().equals(a.value())) {
                    var value = entry.getValue();
                    field.setAccessible(true);

                    if (type.equals(String.class)) {
                        value = String.valueOf(value);
                    } else if (type.equals(Integer.class) || type.equals(int.class)) {
                        value = Integer.valueOf(value.toString());
                    } else if (type.equals(Long.class) || type.equals(long.class)) {
                        value = Long.valueOf(value.toString());
                    } else if (type.equals(Byte.class) || type.equals(byte.class)) {
                        value = Byte.valueOf(value.toString());
                    } else if (type.equals(Boolean.class) || type.equals(boolean.class)) {
                        value = Boolean.valueOf(value.toString());
                    } else if (type.equals(Double.class) || type.equals(double.class)) {
                        value = Double.valueOf(value.toString());
                    } else if (type.equals(Float.class) || type.equals(float.class)) {
                        value = Float.valueOf(value.toString());
                    } else if (type.equals(Object.class)) {
                        value = value.toString();
                    } else {
                        if (mapper != null) {
                            value = mapper.convert(field, value);
                        } else {
                            throw new IllegalStateException("This is not a primitive data type '" + type
                                    .getTypeName() + "'. Custom mapper is not set");
                        }
                    }
                    field.set(dto, value);
                    return true;
                }
            } else {
                throw new IllegalStateException("The Field '" + field
                        .getName() + "' is not marked with an '" + ResponseKey.class.getSimpleName() + "' annotation");
            }
        } catch (NoSuchFieldException e) {
            throw new IllegalStateException("Field '" + fieldName + "' in the '" + dto.getClass()
                    .getSimpleName() + "' instance does " + "not exist", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("An unexpected error has occurred", e);
        }
        return false;
    }

    /**
     * Zkontroluje integritu parametru v odpovedi s mapovanim tridy.
     */
    public static int checkResponseIntegrity(String responseKey, Class responseClass) {
        synchronized (RESPONSE_CLASS_CACHE) {
            List<String> respKeyList = getAvailableResponseClassKeys(responseClass);

            if (!respKeyList.contains(responseKey)) {
                LOG.warn("Missing fields mapping in '{}'. Undefined response parameter is: {}", responseClass
                        .getSimpleName(), responseKey);
                return 1;
            }
            return 0;
        }
    }

    /**
     * Zkontroluje integritu parametru v odpovedi s mapovanim tridy.
     */
    public static int checkResponseIntegrity(Collection<String> responseKeys, Class responseClass) {
        synchronized (RESPONSE_CLASS_CACHE) {
            List<String> respKeyList = getAvailableResponseClassKeys(responseClass);
            var undefinedKeys = new ArrayList<String>();

            for (var key : responseKeys) {
                if (!respKeyList.contains(key)) {
                    undefinedKeys.add(key);
                }
            }
            if (!undefinedKeys.isEmpty()) {
                LOG.error("Missing fields mapping in '{}'. Undefined response parameters are: {}", responseClass
                        .getSimpleName(), undefinedKeys);
            }
            return undefinedKeys.size();
        }
    }

    private static List<String> getAvailableResponseClassKeys(Class responseClass) {
        List<String> respKeyList;

        if (!RESPONSE_CLASS_CACHE.containsKey(responseClass)) {
            var fields = responseClass.getDeclaredFields();
            respKeyList = new ArrayList<>();

            for (var field : fields) {
                if (field.isAnnotationPresent(ResponseKey.class)) {
                    var a = field.getDeclaredAnnotation(ResponseKey.class);
                    respKeyList.add(a.value());
                }
            }
            if (Response.class.isAssignableFrom(responseClass.getSuperclass())) {
                fields = responseClass.getSuperclass().getDeclaredFields();

                for (var field : fields) {
                    if (field.isAnnotationPresent(ResponseKey.class)) {
                        var a = field.getDeclaredAnnotation(ResponseKey.class);
                        respKeyList.add(a.value());
                    }
                }
            }
            RESPONSE_CLASS_CACHE.put(responseClass, respKeyList);
        } else {
            respKeyList = RESPONSE_CLASS_CACHE.get(responseClass);
        }
        return respKeyList;
    }
}
