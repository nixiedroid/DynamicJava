package com.nixiedroid.premain;

import com.nixiedroid.modules.ModuleManager2;
import com.nixiedroid.runtime.Info;
import com.nixiedroid.urlMocker.Logger;
import com.nixiedroid.urlMocker.mock.URLHandlerMocker;

import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

@SuppressWarnings("unused")
public class Handler extends PremainHandler {
    private static final int JAVA_COMPILE_VERSION = 11;
    @Override
    public void handle(String args, Instrumentation inst) {
        verifyJavaVersion();
        Logger.log.info("Starting URL Hook");
        if ("false".equals(System.getProperty("urlMocker.doRedirect", "false"))) {
            Logger.log.info("Logging urls only. Without redirect");
        }
        try {
            new ModuleManager2();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        URLHandlerMocker.init();

    }

    private static void verifyJavaVersion(){
        if (Info.getVersion() < JAVA_COMPILE_VERSION) {
            Logger.log.info("Detected untested java version: " + Info.getVersion());

            if ("true".equals(System.getProperty("urlMocker.javaVerIgnore", "false"))) {
                Logger.log.info("Found urlMocker.javaVerIgnore = true. Ignoring old java version");
            } else {
                throw new RuntimeException("Shutting down due to untested java version. Use -DurlMocker.javaVerIgnore=true to override");
            }
        }
    }

    @Override
    public byte[] transform(ClassLoader loader,
                            String className, Class<?>
                                        classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        return super.transform(loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
    }
}
