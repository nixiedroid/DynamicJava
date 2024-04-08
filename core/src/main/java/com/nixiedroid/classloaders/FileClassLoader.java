package com.nixiedroid.classloaders;

import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileClassLoader extends ClassLoader {
    private static final byte[] MAGIC = new byte[]{(byte) 0xCA, (byte) 0xFE, (byte) 0xBA, (byte) 0xBE};
    protected final String PKG_PREFIX;
    protected final String EXTENSION;
    protected ClassParser classParser;

    public FileClassLoader(ClassLoader parent) {
        super(parent); //Support for classloader replacement by THIS class
        PKG_PREFIX = "com.nixiedroid.plugins";
        EXTENSION = "clazz";
    }

    public FileClassLoader(final String prefix, final String extension) {
        PKG_PREFIX = prefix;
        EXTENSION = extension;
    }

    private static void validateClassMagic(byte[] bytes) throws ValidationException {
        if (bytes == null) throw new ValidationException("Class File is Null");
        if (bytes.length < 4) throw new ValidationException("Invalid Class File Length");
        for (int i = 0; i < 4; i++) {
            if (MAGIC[i] != bytes[i]) throw new ValidationException("Magic Signature is Invalid");
        }
    }

    protected final String getRealClassName(byte[] classBytes) throws ClassNotFoundException {
        try {
            classParser = new ClassParser(new ByteArrayInputStream(classBytes), "");
            JavaClass jc = classParser.parse();
            return jc.getClassName();
        } catch (IOException e) {
            throw new ClassNotFoundException();
        }
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
        if (name.startsWith(PKG_PREFIX)) {
            loadClassProcessLogging(name);
            cl = findClass(name);
            if (cl != null) return cl;
        }
        return super.loadClass(name);
    }

    protected void loadClassInputLogging(String name) {
    }
    protected void loadClassProcessLogging(String name) {
    }

    @Override
    protected final Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] classBytes = getClassBytes(name);
        String realClassName = getRealClassName(classBytes);
        return defineClass(realClassName, classBytes, 0, classBytes.length); //Name must be equal to inside class
    }

    protected byte[] getClassBytes(String name) {
        try {
            return readFile(getFileName(name, EXTENSION));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    protected final byte[] readFile(String name) throws FileNotFoundException {
        byte[] fileBytes;
        try (var res = this.getClass().getResourceAsStream(name)) {
            fileBytes = null;
            if (res != null) fileBytes = res.readAllBytes();
            if (fileBytes == null) throw new FileNotFoundException();
            return fileBytes;
        } catch (IOException e) {
            throw new FileNotFoundException(name);
        }
    }
}
