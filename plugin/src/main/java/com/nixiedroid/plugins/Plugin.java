package com.nixiedroid.plugins;

public class Plugin {
   public Plugin(){
       System.out.println("Plugin 1 Instance");
   }
   public void method(){
       System.out.println("Plugin 1 Method");
   }
   public static void staticMethod(){
        System.out.println("Plugin 1 Static Method");
   }
}