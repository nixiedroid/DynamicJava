package com.nixiedroid;

import com.sun.management.HotSpotDiagnosticMXBean;

import java.lang.management.ManagementFactory;

public class Info {
    private static final boolean is64Bit;
    private static final int version;
    private static final boolean isCompressedOOPS;

    static {
        HotSpotDiagnosticMXBean diagnosticBean = ManagementFactory.getPlatformMXBean(HotSpotDiagnosticMXBean.class);
        isCompressedOOPS = Boolean.parseBoolean(diagnosticBean.getVMOption("UseCompressedOops").getValue());
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
        is64Bit = sunArchDataModel.contains("64");


    }


    public static boolean isIs64Bit() {
        return is64Bit;
    }

    public static int getVersion() {
        return version;
    }

    public static boolean isCompressedOOPS() {
        return isCompressedOOPS;
    }
}
