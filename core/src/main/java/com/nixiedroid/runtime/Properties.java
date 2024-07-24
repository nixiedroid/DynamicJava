package com.nixiedroid.runtime;

/**
 * A utility class that provides system properties related to Java version and architecture.
 *
 * <p>This class provides information about:
 * <ul>
 *     <li>Whether the JVM is running in a 64-bit environment.</li>
 *     <li>The major version of the Java runtime.</li>
 *     <li>Whether compressed object pointers (OOPs) are used.</li>
 * </ul>
 */
public final class Properties {

    private static final boolean is64Bit;
    private static final int version;
    private static final boolean isCompressedOOPS;

    // Static initializer block to initialize properties based on system settings
    static {
        // Determine the Java version
        String sVersion = System.getProperty("java.version");
        if (sVersion.startsWith("1.")) {
            sVersion = sVersion.substring(2, 3);
        } else {
            int dot = sVersion.indexOf(".");
            if (dot != -1) {
                sVersion = sVersion.substring(0, dot);
            } else {
                dot = sVersion.indexOf("-");
                if (dot != -1) {
                    sVersion = sVersion.substring(0, dot);
                }
            }
        }
        version = Integer.parseInt(sVersion);

        // Determine if the JVM is 64-bit
        String sunArchDataModel = System.getProperty("sun.arch.data.model");
        if (sunArchDataModel != null) {
            is64Bit = sunArchDataModel.contains("64");
        } else {
            String osArch = System.getProperty("os.arch");
            is64Bit = osArch != null && osArch.contains("64");
        }

        // Determine if compressed OOPs are used
        boolean compressedOOPSTemp;
        if (is64Bit) {
            try {
                Class<?> mxBean = Class.forName("com.sun.management.HotSpotDiagnosticMXBean");
                Object mgmt = Class.forName("java.lang.management.ManagementFactory")
                        .getMethod("getPlatformMXBean", Class.class)
                        .invoke(null, mxBean);
                Object vmOption = (mxBean.getMethod("getVMOption", String.class)
                        .invoke(mgmt, "UseCompressedOops"));
                String value = vmOption.getClass().getMethod("getValue").invoke(vmOption).toString();
                compressedOOPSTemp = Boolean.parseBoolean(value);
            } catch (ReflectiveOperationException e) {
                compressedOOPSTemp = false;
            }
            isCompressedOOPS = compressedOOPSTemp;
        } else {
            isCompressedOOPS = false;
        }
    }

    /**
     * Returns whether the JVM is running in a 64-bit environment.
     *
     * @return {@code true} if the JVM is 64-bit; {@code false} otherwise
     */
    public static boolean is64Bit() {
        return is64Bit;
    }

    /**
     * Returns the major version of the Java runtime.
     *
     * @return the major Java version
     */
    public static int getVersion() {
        return version;
    }

    /**
     * Returns whether compressed object pointers (OOPs) are used.
     *
     * @return {@code true} if compressed OOPs are used; {@code false} otherwise
     */
    public static boolean isCompressedOOPS() {
        return isCompressedOOPS;
    }

    // Private constructor to prevent instantiation
    private Properties() {
        // Prevent instantiation
    }
}
