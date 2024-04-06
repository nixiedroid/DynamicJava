package com.nixiedroid.classloaders;

public class DummyClassLoader extends ClassLoader{
    @Override
    public String getName() {
        return "dummy";
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        System.out.println("Loading " + name + " using " + getName() );
        return super.loadClass(name);
    }
}
