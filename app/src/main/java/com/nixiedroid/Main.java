package com.nixiedroid;

import com.nixiedroid.plugins.PluginLoader;
import com.nixiedroid.plugins.PluginStub;

public class Main {
    public static void main(String[] args) {
        PluginLoader loader = new PluginLoader();
        PluginStub someCLass;
        someCLass = loader.loadPluginFromFile("Plugin");
        //someCLass = loader.loadPluginFromClassPath("com.nixiedroid.plugins.Plugin");
        System.out.println(someCLass.getResult());
    }
}