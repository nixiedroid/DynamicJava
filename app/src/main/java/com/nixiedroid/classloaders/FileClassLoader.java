package com.nixiedroid.classloaders;



import java.io.*;

public class FileClassLoader extends ClassLoader{
    private final String classDirectory;
    private final String classNamePrefix;
    public FileClassLoader(String classDir, String classNamePrefix) {
      classDirectory = classDir;
      this.classNamePrefix = classNamePrefix;
    }
    @Override
    public synchronized Class loadClass(String name,
                                           boolean resolve)
            throws ClassNotFoundException
    {
        Class result= findClass(name);
        if (resolve)
            resolveClass(result);
        return result;
    }

    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        File classFile = new File(classDirectory+name+".file");
        if (isInvalidFile(classFile)) findSystemClass(name);
        byte[] b;
        try {
            b = loadClassFromFile(classFile);
        } catch (IllegalArgumentException e) {
            throw new ClassNotFoundException(e.getMessage());
        }
        return defineClass(classNamePrefix+name, b, 0, b.length);
    }
    private byte[] loadClassFromFile(File classFile) {
         try {
            return loadBytesFromFile(classFile);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
    private boolean isInvalidFile(File file){
        return !(file.isFile() && file.length()>0) ;
    }
    byte[] loadBytesFromFile(File file) throws IOException {
        byte[] fileBytes = new byte[(int) file.length()];
        try  ( FileInputStream bytes = new FileInputStream(file)){
            bytes.read(fileBytes,0,fileBytes.length);
        } catch (IOException e) {
            throw new IOException("Error reading bytes from file");
        }
        return fileBytes;
    }
}
