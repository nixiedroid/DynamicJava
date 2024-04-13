package com.nixiedroid.function;

import java.util.function.Function;

public class Test {

    static class Func implements Function<Class<?>,String> {
        @Override
        public String apply(Class<?> clazz) {
            return clazz.getSimpleName();
        }
    }

    public void funcMethod(){
        Func f = new Func();
        System.out.println();
    }

    public Test() {
        funcMethod();
        generics();


    }

    public void generics() {
//        CallSite fact = null;
//        try {
//            fact = java.lang.invoke.LambdaMetafactory.altMetafactory(
//                    MethodHandles.lookup(), "test",
//                    MethodType.methodType(int.class)
//            );
//        } catch (LambdaConversionException e) {
//            throw new AssertionError(e);
//        }
//        System.out.println(fact.type());


    }
}
