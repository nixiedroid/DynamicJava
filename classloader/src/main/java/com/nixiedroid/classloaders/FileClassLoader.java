package com.nixiedroid.classloaders;

import java.io.IOException;

public class FileClassLoader extends ClassLoader {
    private static final byte[] MAGIC = new byte[]{(byte) 0xCA, (byte) 0xFE, (byte) 0xBA, (byte) 0xBE};
    protected final String PKG_PREFIX = "com.nixiedroid.plugins.";


    protected static void validateClassMagic(byte[] bytes) throws ValidationException {
        if (bytes == null) throw new ValidationException("Class File is Null");
        if (bytes.length < 4) throw new ValidationException("Invalid Class File Length");
        for (int i = 0; i < 4; i++) {
            if (MAGIC[i] != bytes[i]) throw new ValidationException("Magic Signature is Invalid");
        }
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
        String classname = name.substring(name.lastIndexOf('.') + 1);
        byte[] classBytes = readFile("/" + classname + ".clazz");
        try {
            validateClassMagic(classBytes);
        } catch (ValidationException e) {
            throw new ClassNotFoundException(e.getMessage());
        }
        return defineClass(name, classBytes, 0, classBytes.length); //Name must be equal to inside class
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
