module nixiedroid.dynamicJava {
    requires jdk.unsupported;
    requires jdk.management;
    requires java.instrument;
    requires java.prefs;
    requires jdk.dynalink;
    exports com.nixiedroid.javaagent;
}