package com.nixiedroid.modules.toolchain;

import com.nixiedroid.runtime.Properties;

public interface Tool {
    int JAVA_17 =17;
    int JAVA_14 = 14;
    int JAVA_9 = 9;
    int JAVA_7 =7;
    int javaVersion = Properties.getVersion();

    default boolean thisVersionGEQ(int version){
        return javaVersion >= version;
    }
}
