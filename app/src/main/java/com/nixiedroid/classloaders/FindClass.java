package com.nixiedroid.classloaders;

public class FindClass {

//    public Class<?> getClassByName(String className, Boolean initialize, ClassLoader classLoader, Class<?> caller) {
//        try {
//            ThrowingQuadFunction<String, Boolean, ClassLoader, Class<?>, Class<?>, Throwable> classByNameRetriever = this.classByNameRetriever;
//
//            try {
//                return (Class)classByNameRetriever.apply(className, initialize, classLoader, caller);
//            } catch (NullPointerException var11) {
//                if (classByNameRetriever != null) {
//                    throw var11;
//                } else {
//                    synchronized(this) {
//                        if (this.classByNameRetriever == null) {
//                            Map<Object, Object> initContext = this.functionsToMap();
//                            this.classByNameRetriever = this.getOrBuildClassByNameRetriever(initContext);
//                            this.refresh(initContext);
//                        }
//                    }
//
//                    return (Class)this.classByNameRetriever.apply(className, initialize, classLoader, caller);
//                }
//            }
//        } catch (Throwable var12) {
//            return (Class)this.throwException(var12);
//        }
//    }
}
