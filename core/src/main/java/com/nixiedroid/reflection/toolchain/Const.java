package com.nixiedroid.reflection.toolchain;

/**
 * Enum representing resource paths used in the application.
 * Each enum constant represents a specific resource required for functionality.
 */
enum Const {
    /**
     * Path to the AccessibleObject hook blob file.
     */
    ACCESSIBLE_BLOB("/AccessibleObjectHook.blob"),

    /**
     * Path to the PrivateLookup hook blob file.
     */
    LOOKUP_HOOK("/PrivateLookup.blob");

    private final String path;

    /**
     * Constructor for Const enum.
     *
     * @param path the resource path associated with the enum constant
     */
    Const(String path) {
        this.path = path;
    }

    /**
     * Returns the resource path for the enum constant.
     *
     * @return the resource path as a String
     */
    public String getPath() {
        return this.path;
    }
}
