package com.nixiedroid.urlWrapper;

import com.nixiedroid.runtime.Info;
import com.nixiedroid.urlWrapper.mock.URLHandlerMocker;

import java.lang.instrument.Instrumentation;

@SuppressWarnings("unused")
public class Agent {
    private static final int JAVA_COMPILE_VERSION = 21;

    public static void premain(String args, Instrumentation inst) {
        verifyJavaVersion();
        System.out.println(Agent.class.getClassLoader());
        Logger.log.info("Starting URL Hook");
        if ("false".equals(System.getProperty("urlWrapper.doRedirect", "false"))) {
            Logger.log.info("Logging urls only. Without redirect");
        }
        URLHandlerMocker.init();
    }
    private static void verifyJavaVersion(){
        if (Info.getVersion() != JAVA_COMPILE_VERSION) {
            Logger.log.info("Detected untested java version: " + Info.getVersion());

            if (!"true".equals(System.getProperty("urlWrapper.javaVerIgnore", "false"))) {
                Logger.log.info("Found urlWrapper.javaVerIgnore = true. Ignoring old java version");
            } else {
                throw new RuntimeException("Shutting down due to untested java version. Use -DurlWrapper.javaVerIgnore=true to override");
            }
        }
    }
}