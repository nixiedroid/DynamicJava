package com.nixiedroid.plugins;


import com.nixiedroid.classloaders.FileClassLoader;

public class PluginLoader {

    public PluginStub loadPluginFromClassPath(String fullClassName) {
        //Assume ClassLoader.getSystemClassLoader() is equals to Class.forName("THIS CLASS").getClassLoader())
        //This requires exact class package and name.
        //So, works only for single plugin
        return loadUsingClassloader(fullClassName,ClassLoader.getSystemClassLoader());
    }
    public PluginStub loadPluginFromFile(String fileName) {
        return loadUsingClassloader(fileName,new FileClassLoader("classes/","com.nixiedroid.plugins."));
    }
    private PluginStub loadUsingClassloader(String className, ClassLoader loader) {
        try  {
            Class<?> loadedClass = Class.forName(className,false,loader);
            return (PluginStub)loadedClass.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.err.println("No Suitable Class Found :(");
            return new PluginStub();
        }
    }
}
