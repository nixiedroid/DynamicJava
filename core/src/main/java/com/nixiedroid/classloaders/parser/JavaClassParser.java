package com.nixiedroid.classloaders.parser;

import java.nio.ByteBuffer;
import java.util.Objects;

public class JavaClassParser {

    public static final class ClassInfo {
        private final int modifiers;
        private final String name;
        private final String simpleName;
        private final String packageName;
        private final String superClassName;
        private final String[] interfaceNames;

        public ClassInfo(int modifiers, String name, String simpleName, String packageName, String superClassName, String[] interfaceNames) {
            this.modifiers = modifiers;
            this.name = name;
            this.simpleName = simpleName;
            this.packageName = packageName;
            this.superClassName = superClassName;
            this.interfaceNames = interfaceNames;
        }

        public String getName() {
            return this.name;
        }

        public String getSimpleName() {
            return this.simpleName;
        }

        public String getPackageName() {
            return this.packageName;
        }

        public String getSuperClassName() {
            return this.superClassName;
        }

        public String[] getInterfaceNames() {
            return this.interfaceNames;
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


    public static ClassInfo create(ByteBuffer byteCode) {
        return create(Objects.requireNonNull(ClassFileParser.retrieveInfo(byteCode.array())));
    }

    public static ClassInfo create(byte[] byteCode) {
        return create(Objects.requireNonNull(ClassFileParser.retrieveInfo(byteCode)));
    }

    public static ClassInfo create(ClassFileParser.RawInfo rawInfo){
        String rawName = rawInfo.getName();
        String[] classNames = retrieveNames(rawName);
        String[] interfaceRawNames = rawInfo.getInterfaceNames();
        String[] interfaceNames = new String[interfaceRawNames.length];
        for (int i = 0; i < interfaceRawNames.length; i++) {
            interfaceNames[i] = retrieveName(interfaceRawNames[i]);
        }
        return new ClassInfo(
                rawInfo.modifiers,
                classNames[2],
                classNames[1],
                classNames[0],
                retrieveName(rawInfo.getSuperClassName()),
                interfaceNames
        );
    }

    private static String retrieveName(String rawName) {
        try {
            return rawName.replace("/", ".");
        } catch (NullPointerException exc) {
            //for module-info.class
            return null;
        }
    }

    private static String[] retrieveNames(String rawName) {
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
}