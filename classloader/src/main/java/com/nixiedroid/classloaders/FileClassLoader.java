package com.nixiedroid.classloaders;

import java.io.IOException;

public class FileClassLoader extends ClassLoader{
    private final String PKG_PREFIX = "/";
    private final String PKG = "com.nixiedroid.plugins.";

    @Override
    public String getName(){
        return "byte";
    }
    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        Class<?> c = findLoadedClass(name);
        if (c != null)
            return c;

        if (name.startsWith(PKG)) {
            c = findClass(name);
            if (c != null)
                return c;
        }

        return super.loadClass(name);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String classname = name.substring(name.lastIndexOf('.')+1);
        byte[] classBytes = readFile("/" + classname+".clazz");
        return defineClass(name, classBytes, 0, classBytes.length); //Name must be equal to inside class

    }
    private static String getMagic(byte[] bytes){
        return String.format("%02X%02X%02X%02X",bytes[0]&0xFF,bytes[1]&0xFF,bytes[2]&0xFF,bytes[3]&0xFF);
    }

    protected byte[] readFile(String name) throws ClassNotFoundException {
        byte[] fileBytes;
        try ( var res = this.getClass().getResourceAsStream(name)) {
            fileBytes = new byte[1];
            if (res != null) fileBytes = res.readAllBytes();
            if (fileBytes.length>4 && getMagic(fileBytes).equals("CAFEBABE")) {
                return fileBytes;
            } else throw new IOException("Invalid class file");
        } catch (IOException e) {
            throw new ClassNotFoundException();
        }
    }
}
