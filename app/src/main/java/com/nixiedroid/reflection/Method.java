package com.nixiedroid.reflection;

import java.lang.reflect.InvocationTargetException;

public class Method {
    public static void execute(Object obj, String name){
        try {
            java.lang.reflect.Method m =  obj.getClass().getDeclaredMethod(name);
            m.invoke(obj);
        } catch (NoSuchMethodException e) {
            System.out.println("No Such Method");
        } catch (InvocationTargetException e) {
            System.out.println(e.getCause().getMessage());
        } catch (IllegalAccessException e) {
            System.out.println("Illegal Access");
        }
    }
    public static void executeStatic(Object obj, String name){
        try {
            java.lang.reflect.Method m =  obj.getClass().getDeclaredMethod(name);
            m.invoke(null);
        } catch (NoSuchMethodException e) {
            System.out.println("No Such Method");
        } catch (InvocationTargetException e) {
            System.out.println(e.getCause().getMessage());
        } catch (IllegalAccessException e) {
            System.out.println("Illegal Access");
        }
    }
}
