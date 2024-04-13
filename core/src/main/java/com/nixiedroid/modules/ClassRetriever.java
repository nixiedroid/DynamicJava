package com.nixiedroid.modules;

import com.nixiedroid.runtime.Info;

import java.util.Arrays;

public class ClassRetriever {
    private static final int[] JAVA_VERSIONS = new int[]{7, 9, 14, 17, 20, 21};
    private static final String SJAVA = "$ForJava";
    private static final String JAVA = "ForJava";
    private static final int JAVA_VERSION = Info.getVersion();
    private static final int[] JAVA_VERSIONS_LEQ_CURRENT =
            Arrays.stream(JAVA_VERSIONS).filter(x -> x <= JAVA_VERSION).toArray();
    private ClassRetriever(){}
    private static Class<?> findHandler(Class<?> parent) throws ClassNotFoundException {
        Class<?>[] classes = parent.getClasses();
            for (int i = JAVA_VERSIONS_LEQ_CURRENT.length-1; i >= 0;i-- ){
                for (Class<?> aClass : classes) {
                    if (aClass.getSimpleName().equals(JAVA + JAVA_VERSIONS_LEQ_CURRENT[i])) {
                        return aClass;
                    }
                }
            }
        throw new ClassNotFoundException();
    }
    public static Object getHandler(Class<?> parent){
        try {
            Class<?> cl = findHandler(parent);
            return cl.getConstructor().newInstance();
        } catch (Exception e) {
           throw new RuntimeException(e);
        }
    }

}
