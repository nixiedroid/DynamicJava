package com.nixiedroid.classloaders;

import com.nixiedroid.classloaders.parser.JavaClassParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public final class ServletLoader {


    private static String getRootPackageName() {
        String pkgName = ServletLoader.class.getPackageName();
        if (pkgName.isEmpty()) return "";
        int i = pkgName.lastIndexOf('.');
        if (i == -1) {
            i = pkgName.length();
        }
        return pkgName.substring(0, i);
    }

    private static List<String> findClasses(String pkg) {

        String codeSrc = ServletLoader.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        if (isJarFile()) {
            return findClassesInJar(codeSrc.replace("%20", " "),pkg);
        } else {
            return findServletClassesFileTree(codeSrc,pkg);
        }
    }

    /**
     * Checks if Application is Running from jar file
     *
     * @return boolean value
     */
    private static boolean isJarFile() {
        return ServletLoader.class.getProtectionDomain().getCodeSource().getLocation().getPath().endsWith(".jar");
    }


    private static List<String> findServletClassesFileTree(String classPath,String pkg) {
        File classes = new File(classPath + getRootPackageName().replace('.', '/'));
        if (classes.exists()) {
            List<String> classNames = new ArrayList<>();
            parseFileTree(classes, classNames,pkg);
            return classNames;
        }
        return null;
    }

    private static void parseFileTree(File root, List<String> classNames,String pkg) {
        File[] fClasses = root.listFiles();
        if (fClasses == null) return;
        for (File f : fClasses) {
            if (f.isDirectory()) {
                parseFileTree(f, classNames, pkg);
            } else {
                try {
                    checkAndAdd(Files.readAllBytes(Path.of(f.toURI())), classNames,pkg);
                } catch (IOException ignored) {
                }
            }
        }
    }

    private static byte[] read(ZipInputStream zis, int size) throws IOException {
        byte[] bytes = new byte[size];
        int read = 0;
        while (read < size) {
            read += zis.read(bytes, read, (size - read));
        }
        return bytes;
    }

    private static void checkAndAdd(byte[] classFile, List<? super String> classNames, String pkg) {
        JavaClassParser.ClassInfo info = JavaClassParser.create(classFile);
        if (info.getPackageName().equals(pkg)) {
            classNames.add(info.getName());
        }
    }

    private static List<String> findClassesInJar(String jarPath,String pkg) {
        try {
            List<String> classNames = new ArrayList<>();
            ZipInputStream zip = new ZipInputStream(new FileInputStream(jarPath));
            for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
                if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
                    String className = entry.getName().replace('/', '.');
                    if (className.startsWith(getRootPackageName())) {
                        checkAndAdd(read(zip, (int) entry.getSize()), classNames,pkg);
                    }
                }
            }
            return classNames;
        } catch (IOException e) {
            return null;
        }
    }

    public void findAndLoadClassesFromPackage(String pkg) {
        List<String> classes = findClasses(pkg);
        loadServletClasses(classes);
    }

    private void loadServletClasses(List<String> fullClassNames) {
        if (fullClassNames == null) return;
        for (String className : fullClassNames) {
            try {
                Class<?> clazz = Class.forName(className.replace('/', '.'));
                loadClass(clazz);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private <T> T loadClass(Class<?> servletClass) {
        if (servletClass == null) throw new IllegalArgumentException("Servlet class must not be null");
        try {
            return (T) servletClass.getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
