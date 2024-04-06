package com.nixiedroid.classloaders;

import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class FileClassLoader extends ClassLoader {
    private static final byte[] MAGIC = new byte[]{(byte) 0xCA, (byte) 0xFE, (byte) 0xBA, (byte) 0xBE};
    protected final String PKG_PREFIX = "com.nixiedroid.plugins.";
    protected ClassParser classParser;

    public FileClassLoader(ClassLoader parent) {
        super(parent); //Support for classloader replacement by THIS class
    }

    public FileClassLoader() {
    }

    protected static void validateClassMagic(byte[] bytes) throws ValidationException {
        if (bytes == null) throw new ValidationException("Class File is Null");
        if (bytes.length < 4) throw new ValidationException("Invalid Class File Length");
        for (int i = 0; i < 4; i++) {
            if (MAGIC[i] != bytes[i]) throw new ValidationException("Magic Signature is Invalid");
        }
    }

    protected String getRealClassName(byte[] classBytes) throws ClassNotFoundException {
        try {
            classParser = new ClassParser(new ByteArrayInputStream(classBytes), "");
            JavaClass jc = classParser.parse();
            return jc.getClassName();
        } catch (IOException e) {
            throw new ClassNotFoundException(e.getMessage());
        }
    }

    protected String getFileName(String className, String extension) throws ClassNotFoundException {
        int index = className.lastIndexOf('.') + 1;
        if (index == className.length()) throw new ClassNotFoundException();
        String classname = className.substring(index);
        return "/" + classname + extension;
    }

    @Override
    public String getName() {
        return "file";
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        Class<?> cl = findLoadedClass(name);
        if (cl != null) return cl;
        if (name.startsWith(PKG_PREFIX)) {
            cl = findClass(name);
            if (cl != null) return cl;
        }
        return super.loadClass(name);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] classBytes = readFile(getFileName(name,".clazz"));
        String realClassName = getRealClassName(classBytes);
        return defineClass(realClassName, classBytes, 0, classBytes.length); //Name must be equal to inside class
    }

    protected byte[] readFile(String name) throws ClassNotFoundException {
        byte[] fileBytes;
        try (var res = this.getClass().getResourceAsStream(name)) {
            fileBytes = new byte[1];
            if (res != null) fileBytes = res.readAllBytes();
            return fileBytes;
        } catch (IOException e) {
            throw new ClassNotFoundException(e.getMessage());
        }
    }
}
