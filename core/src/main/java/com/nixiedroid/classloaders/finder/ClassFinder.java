package com.nixiedroid.classloaders.finder;

import com.nixiedroid.classes.JavaClassParser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.CodeSource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public final class ClassFinder {

    private final Criteria criteria;

    public ClassFinder(Criteria criteria) {
        this.criteria = criteria;
    }

    public static Set<String> findLoadedClasses(String pkg) {
        ClassLoader app = ClassLoader.getSystemClassLoader();
        Function<String,Optional<String>> getClass = s -> {
            try {
                Class<?> clazz =  app.loadClass(pkg + "." + s.substring(0, s.lastIndexOf('.')));
                return Optional.of(clazz.getName());
            } catch (ClassNotFoundException ignored) {}
            return Optional.empty();
        };
        try (InputStream is = app.getResourceAsStream(pkg.replaceAll("[.]", "/"))) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            return reader.lines()
                    .filter(line -> line.endsWith(".class"))
                    .map(getClass)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toSet());
        } catch (IOException | NullPointerException ignored) {}
        return new HashSet<>();
    }

    public List<String> findClasses(Class<?> srcClass) {
        CodeSource cs = srcClass.getProtectionDomain().getCodeSource();
        if (cs == null) return new ArrayList<>();
        java.net.URL location = cs.getLocation();
        if (location == null) return new ArrayList<>();
        String codeSrc = location.getPath();
        if (isJarFile()) {
            return findClassesInJar(codeSrc.replace("%20", " "));
        } else {
            return findClassesInFileTree(codeSrc);
        }
    }

    /**
     * Checks if Application is Running from jar file
     *
     * @return boolean value
     */
    private boolean isJarFile() {
        return ClassFinder.class.getProtectionDomain().getCodeSource().getLocation().getPath().endsWith(".jar");
    }

    private List<String> findClassesInFileTree(String classPath) {
        File classes = new File(classPath);
        if (classes.exists()) {
            List<String> classNames = new ArrayList<>();
            parseFileTree(classes, classNames);
            return classNames;
        }
        return null;
    }

    private void parseFileTree(File root, List<? super String> classNames) {
        File[] fClasses = root.listFiles();
        if (fClasses == null) return;
        for (File f : fClasses) {
            if (f.isDirectory()) {
                parseFileTree(f, classNames);
            } else {
                try {
                    checkAndAdd(Files.readAllBytes(Path.of(f.toURI())), classNames);
                } catch (IOException ignored) {
                }
            }
        }
    }

    private byte[] read(ZipInputStream zis, int size) throws IOException {
        byte[] bytes = new byte[size];
        int read = 0;
        while (read < size) {
            read += zis.read(bytes, read, (size - read));
        }
        return bytes;
    }

    private void checkAndAdd(byte[] classFile, List<? super String> classNames) {
        JavaClassParser.ClassInfo info = JavaClassParser.create(classFile);
        if (this.criteria.matches(info)) classNames.add(info.getName());
    }

    private List<String> findClassesInJar(String jarPath) {
        try {
            List<String> classNames = new ArrayList<>();
            ZipInputStream zip = new ZipInputStream(new FileInputStream(jarPath));
            for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
                if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
                    checkAndAdd(read(zip, (int) entry.getSize()), classNames);
                }
            }
            return classNames;
        } catch (IOException e) {
            return null;
        }
    }
}
