package com.nixiedroid.classloaders;


import com.nixiedroid.classes.JavaClassParser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class FileClassLoader extends ClassLoader {
    protected final String PKG_PREFIX;
    protected final String EXTENSION;

    public FileClassLoader(final String prefix, final String extension) {
        this.PKG_PREFIX = prefix;
        this.EXTENSION = extension;
    }

    protected final String getRealClassName(byte[] classBytes) {
        return JavaClassParser.create(classBytes).getName();
    }

    protected final String getFileName(String className, String extension) throws FileNotFoundException {
        int index = className.lastIndexOf('.') + 1;
        if (index == className.length()) throw new FileNotFoundException("Illegal Class Name");
        String classname = className.substring(index);
        return "/" + classname + "." + extension;
    }

    @Override
    public String getName() {
        return "file";
    }

    @Override
    public final Class<?> loadClass(String name) throws ClassNotFoundException {
        loadClassInputLogging(name);
        Class<?> cl = findLoadedClass(name);
        if (cl != null) return cl;
        if (name.startsWith(this.PKG_PREFIX)) {
            cl = findClass(name);
            if (cl != null) return cl;
        }
        return super.loadClass(name);
    }

    protected void loadClassInputLogging(String name) {
    }

    @Override
    protected final Class<?> findClass(String name) {
        byte[] classBytes = getClassBytes(name);
        String realClassName = getRealClassName(classBytes);
        return defineClass(realClassName, classBytes, 0, classBytes.length); //Name must be equal to inside class
    }

    protected byte[] getClassBytes(String name) {
        try {
            return readFile(getFileName(name, this.EXTENSION));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    protected final byte[] readFile(String name) throws FileNotFoundException {
        byte[] fileBytes;
        try (InputStream res = this.getClass().getResourceAsStream(name)) {
            fileBytes = null;
            if (res != null) fileBytes = res.readAllBytes();
            if (fileBytes == null) throw new FileNotFoundException();
            return fileBytes;
        } catch (IOException e) {
            throw new FileNotFoundException(name);
        }
    }
}
