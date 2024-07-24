package com.nixiedroid.reflection.toolchain;

enum Const {
    ACCESSIBLE_BLOB ("/AccessibleObjectHook.blob"),
    LOOKUP_HOOK( "/PrivateLookup.blob");

    private final String path;

    String getPath() {
        return this.path;
    }

    Const(String path) {
        this.path = path;
    }


}
