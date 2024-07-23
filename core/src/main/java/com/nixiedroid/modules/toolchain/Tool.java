package com.nixiedroid.modules.toolchain;

import com.nixiedroid.runtime.Properties;

public interface Tool {
    int javaVersion = Properties.getVersion();

    default boolean geq(int version){
        return version >= javaVersion;
    }
}
