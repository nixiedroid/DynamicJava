package com.nixiedroid.modules.communism;

import com.nixiedroid.runtime.Info;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;

import java.io.ByteArrayInputStream;
import java.io.IOException;
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
        Class<?> cl = findHandler(parent);

        try {
            return cl.getConstructor().newInstance();
        } catch (Exception e) {
           throw new RuntimeException(e);
        }
    }

    private static Class<?> getForJavaVersion(Class<?>[] classes) throws ClassNotFoundException {
        for (int i = JAVA_VERSIONS_LEQ_CURRENT.length-1; i >= 0;i-- ){
            for (Class<?> aClass : classes) {
                if (aClass.getSimpleName().equals(JAVA + JAVA_VERSIONS_LEQ_CURRENT[i])) {
                    return aClass;
                }
            }
        }
        throw new ClassNotFoundException(classes.toString());
    }
    public static String getRealClassName(byte[] classBytes) throws ClassNotFoundException {
        try {
            ClassParser classParser = new ClassParser(new ByteArrayInputStream(classBytes), "");
            JavaClass jc = classParser.parse();
            return jc.getClassName();
        } catch (IOException e) {
            throw new ClassNotFoundException(e.getMessage());
        }
    }
}
