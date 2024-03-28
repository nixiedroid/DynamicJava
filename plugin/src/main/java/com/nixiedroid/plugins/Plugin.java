package com.nixiedroid.plugins;

public class Plugin {
   public Plugin(){
       System.out.println("Plugin 1 Instance");
   }
   public String method(){
       return "Plugin 1 Method";
   }
   public static String staticMethod(){
        return "Plugin 1 Static Method";
   }
}