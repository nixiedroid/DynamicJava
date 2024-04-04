package com.nixiedroid.reflection.classes;


import java.nio.ByteBuffer;

public class JavaClass {
    protected int modifiers;
    protected String name;
    protected String simpleName;
    protected String packageName;
    protected String superClassName;
    protected String[] interfaceNames;

    protected JavaClass(ByteBuffer byteCode) {
        this(Classes.File.Reader.retrieveInfo(BufferHandler.shareContent(byteCode)));
    }

    protected JavaClass(byte[] byteCode) {
        this(Classes.File.Reader.retrieveInfo(byteCode));
    }

    protected JavaClass(Classes.RawInfo rawInfo) {
        this.modifiers = rawInfo.modifiers;
        String rawName = rawInfo.getName();
        String[] classNames = retrieveNames(rawName);
        packageName = classNames[0];
        simpleName = classNames[1];
        name = classNames[2];
        superClassName = retrieveName(rawInfo.getSuperClassName());
        String[] interfaceRawNames = rawInfo.getInterfaceNames();
        interfaceNames = new String[interfaceRawNames.length];
        for (int i = 0; i < interfaceRawNames.length; i++) {
            interfaceNames[i] = retrieveName(interfaceRawNames[i]);
        }

    }

    public static JavaClass create(ByteBuffer byteCode) {
        return new JavaClass(byteCode);
    }

    public static JavaClass create(byte[] byteCode) {
        return new JavaClass(byteCode);
    }

    private String retrieveName(String rawName) {
        try {
            return rawName.replace("/", ".");
        } catch (NullPointerException exc) {
            //for module-info.class
            return null;
        }
    }

    private String[] retrieveNames(String rawName) {
        String[] names = new String[3];
        String rawPackageName = rawName.contains("/") ?
                rawName.substring(0, rawName.lastIndexOf("/")) :
                null;
        if (rawPackageName != null) {
            //packageName
            names[0] = rawPackageName.replace("/", ".");
        }
        //simpleName
        names[1] = rawName.contains("/") ?
                rawName.substring(rawName.lastIndexOf("/") + 1) :
                rawName;
        names[2] = (names[0] != null? names[0] + "." : "") + names[1];
        return names;
    }

    public String getName() {
        return name;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getSuperClassName() {
        return superClassName;
    }

    public String[] getInterfaceNames() {
        return interfaceNames;
    }

    public boolean isPublic() {
        return (modifiers & 0x0001) != 0;
    }

    public boolean isFinal() {
        return (modifiers & 0x0010) != 0;
    }

    public boolean isInterface() {
        return (modifiers & 0x0200) != 0;
    }

    public boolean isAbstract() {
        return (modifiers & 0x0400) != 0;
    }

    public boolean isAnnotation() {
        return (modifiers & 0x2000) != 0;
    }

    public boolean isEnum() {
        return (modifiers & 0x4000) != 0;
    }

}