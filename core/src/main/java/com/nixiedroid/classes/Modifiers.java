package com.nixiedroid.classes;

public enum Modifiers {
    ;
    public static final int PUBLIC = 0x0001;      // Declared public; may be accessed from outside its package.
    public static final int FINAL = 0x0010;       // Declared final; no subclasses allowed.
    public static final int SUPER = 0x0020;       // Treat superclass methods specially when invoked by the invokespecial instruction.
    public static final int INTERFACE = 0x0200;   // Is an interface, not a class.
    public static final int ABSTRACT = 0x0400;    // Declared abstract; must not be instantiated.
    public static final int SYNTHETIC = 0x1000;   // Declared synthetic; not present in the source code.
    public static final int ANNOTATION = 0x2000;  // Declared as an annotation interface.
    public static final int ENUM = 0x4000;        // Declared as an enum class.
    public static final int MODULE = 0x8000;      // Is a module, not a class or interface.

    public static boolean isPublic(int modifiers) {
        return (modifiers & Modifiers.PUBLIC) != 0;
    }

    public static boolean isSuper(int modifiers){
        return (modifiers & Modifiers.SUPER) != 0;
    }

    public static boolean isFinal(int modifiers) {
        return (modifiers & Modifiers.FINAL) != 0;
    }

    public static boolean isInterface(int modifiers) {
        return (modifiers & Modifiers.INTERFACE) != 0;
    }

    public static boolean isAbstract(int modifiers) {
        return (modifiers & Modifiers.ABSTRACT) != 0;
    }

    public static boolean isAnnotation(int modifiers) {
        return (modifiers & Modifiers.ANNOTATION) != 0;
    }

    public static boolean isEnum(int modifiers) {
        return (modifiers & Modifiers.ENUM) != 0;
    }

    public static boolean isModule(int modifiers) {
        return (modifiers & Modifiers.MODULE) != 0;
    }
}
