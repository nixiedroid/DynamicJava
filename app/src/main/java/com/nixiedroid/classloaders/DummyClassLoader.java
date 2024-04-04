package com.nixiedroid.classloaders;

public class DummyClassLoader extends ClassLoader{
    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        System.out.println(name);
        return super.loadClass(name);
    }
}
