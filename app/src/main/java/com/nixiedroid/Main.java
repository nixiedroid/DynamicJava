package com.nixiedroid;

import com.nixiedroid.reflection.samples.PrivateConstructorClass;

import java.lang.reflect.Constructor;

public class Main {
    public static void main(String[] args) {
        //Object plugin = PluginWrapper.loadPluginFromFile("Plugin");
        //someCLass = loader.loadPluginFromClassPath("com.nixiedroid.plugins.Plugin");
        PrivateConstructorClass obj = null;
        Constructor<PrivateConstructorClass> constructor;
        try {
            constructor = PrivateConstructorClass.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            obj =  constructor.newInstance();
        } catch (Exception a_lot_of) {}
        if (obj != null) obj.actionOne();
    }
}