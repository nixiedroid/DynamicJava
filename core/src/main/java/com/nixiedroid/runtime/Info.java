package com.nixiedroid.runtime;

public final class Info {
    private static final boolean is64Bit;
    private static final int version;
    private static final boolean isCompressedOOPS;

    static {
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
        String sunArchDataModel = System.getProperty("sun.arch.data.model");
        if (sunArchDataModel != null) {
            is64Bit = sunArchDataModel.contains("64");
        } else {
            String osArch = System.getProperty("os.arch");
            is64Bit = osArch != null && osArch.contains("64");
        }

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

    private Info(){}

    public static boolean is64Bit() {
        return is64Bit;
    }

    public static int getVersion() {
        return version;
    }

    public static boolean isCompressedOOPS() {
        return isCompressedOOPS;
    }
}
