package com.nixiedroid.waytwo.magic;

import com.nixiedroid.runtime.Info;

import java.util.Arrays;

public class ClassRetriever {
    static final int[] JAVA_VERSIONS = new int[]{7, 9, 14, 17, 20, 21};
    static final String SJAVA = "$ForJava";
    static final String JAVA = "ForJava";
    static int javaVersion = Info.getVersion();
    static final int[] JAVA_VERSIONS_LEQ_CURRENT = Arrays.stream(JAVA_VERSIONS).filter(x -> x < javaVersion).toArray();
    private ClassRetriever(){}
    private static Class<?> findHandler(Class<?> parent) {
        Class<?>[] classes = parent.getClasses();
        try {
            return getForJavaVersion(classes);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public static Object getHandler(Class<?> parent){
        System.out.println( "Req: " + parent.getName());
        Class<?> cl = findHandler(parent);

        try {
            return cl.getConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Class<?> getForJavaVersion(Class<?>[] classes) throws ClassNotFoundException {
        for (int i = JAVA_VERSIONS_LEQ_CURRENT.length-1; i >= 0;i-- ){
            for (Class<?> aClass : classes) {
                if (aClass.getSimpleName().equals(JAVA + JAVA_VERSIONS_LEQ_CURRENT[i])) {
                    System.out.println("Res: " +  aClass.getName());
                    return aClass;
                }
            }
        }
        throw new ClassNotFoundException(classes.toString());
    }
    public static String getClassName(byte[] classBytes){
        //ByteBuffer vs byte[]
        return "";
    }
}
