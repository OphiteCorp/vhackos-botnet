package cz.ophite.mimic.vhackos.botnet.api.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Vygeneruje náhodného user agenta.
 *
 * @author mimic
 */
final class UserAgentGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(UserAgentGenerator.class);
    private static final Random RAND = new Random();

    private static final List<String> DALVIK_LIST;
    private static final List<String> ANDROID_LIST;
    private static final List<String> DEVICE_LIST;

    static {
        DALVIK_LIST = new ArrayList<>();
        DALVIK_LIST.add("1.1.0");
        DALVIK_LIST.add("1.6.0");
        DALVIK_LIST.add("1.4.0");
        DALVIK_LIST.add("2.1.0");

        ANDROID_LIST = new ArrayList<>();
        ANDROID_LIST.add("4.1.2");
        ANDROID_LIST.add("4.2.1");
        ANDROID_LIST.add("4.4.0");
        ANDROID_LIST.add("4.4.4");
        ANDROID_LIST.add("5.0");
        ANDROID_LIST.add("5.0.1");
        ANDROID_LIST.add("5.0.2");
        ANDROID_LIST.add("5.1.1");
        ANDROID_LIST.add("5.1");
        ANDROID_LIST.add("6.0.1");
        ANDROID_LIST.add("6.0");

        DEVICE_LIST = new ArrayList<>();
        DEVICE_LIST.add("SM-N935F Build/KTU84P");
        DEVICE_LIST.add("GT-I9508V Build/LRX22C");
        DEVICE_LIST.add("MX4 Build/LRX22C");
        DEVICE_LIST.add("D5322 Build/19.3.A.0.472");
        DEVICE_LIST.add("D816w Build/LRX22G");
        DEVICE_LIST.add("HTC D816v Build/LRX22G");
        DEVICE_LIST.add("LG-F320L Build/LRX22G");
        DEVICE_LIST.add("Letv X500 Build/DBXCNOP5500912251S");
        DEVICE_LIST.add("Nexus 5 Build/LRX22G");
        DEVICE_LIST.add("SM-N9005 Build/LRX22G");
        DEVICE_LIST.add("ASUS_Z00ADB Build/LRX21V");
        DEVICE_LIST.add("2014811 MIUI/6.1.26");
        DEVICE_LIST.add("D5833 Build/23.4.A.1.232");
        DEVICE_LIST.add("MI 2 Build/LMY48B");
        DEVICE_LIST.add("MI 3 Build/LMY48Y");
        DEVICE_LIST.add("Mi-4c MIUI/6.1.14");
        DEVICE_LIST.add("ONE A2001 Build/LMY47V");
        DEVICE_LIST.add("R7Plusm Build/LMY47V");
        DEVICE_LIST.add("Redmi Note 2 Build/LMY48Y");
        DEVICE_LIST.add("Xperia Z2 Build/LMY48Y");
        DEVICE_LIST.add("titan Build/LMY48W");
        DEVICE_LIST.add("MX5 Build/LMY47I");
        DEVICE_LIST.add("XT1060 Build/LPA23.12-39.7");
        DEVICE_LIST.add("XT1085 Build/LPE23.32-53");
        DEVICE_LIST.add("m2 note Build/LMY47D");
        DEVICE_LIST.add("A0001 Build/MMB29M");
        DEVICE_LIST.add("MI 4LTE Build/MMB29M");
        DEVICE_LIST.add("Moto G 2014 Build/MMB29M");
        DEVICE_LIST.add("Sensation Build/MMB29U");
        DEVICE_LIST.add("Z1 Build/MMB29T");
        DEVICE_LIST.add("XT1097 Build/MPE24.49-18");
        DEVICE_LIST.add("Lenovo A766 Build/JOP40D");
        DEVICE_LIST.add("HUAWEI Y210-0100 Build/HuaweiY210-0100");
        DEVICE_LIST.add("GT-S5300 Build/GINGERBREAD");
        DEVICE_LIST.add("3G NOTE i Build/JDQ39");
        DEVICE_LIST.add("ASUS_T00Q Build/KVT49L");
        DEVICE_LIST.add("LG-E410 Build/JZO54K");
        DEVICE_LIST.add("W2430 Build/IMM76D");
        DEVICE_LIST.add("Ascend G510 Build/KTU84Q");
        DEVICE_LIST.add("AFTB Build/JDQ39");
        DEVICE_LIST.add("GT-N8013 Build/JZO54K");
        DEVICE_LIST.add("TStick Build/LMY47V");
        DEVICE_LIST.add("SM-T530 Build/KOT49H");
        DEVICE_LIST.add("A850 Build/JDQ39");
        DEVICE_LIST.add("T970 Build/IMM76D");
    }

    /**
     * Vygeneruje unikátní user-agent prohlížeče.
     */
    static String generateNew() {
        var dalvik = DALVIK_LIST.get(RAND.nextInt(DALVIK_LIST.size()));
        var android = ANDROID_LIST.get(RAND.nextInt(ANDROID_LIST.size()));
        var device = DEVICE_LIST.get(RAND.nextInt(DEVICE_LIST.size()));
        var userAgent = String.format("Dalvik/%s (Linux; U; Android %s; %s)", dalvik, android, device);
        LOG.debug("Generated userAgent: {}", userAgent);
        return userAgent;
    }
}
