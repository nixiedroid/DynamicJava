module nixiedroid.dynamic.app {
    requires java.instrument;
    requires nixiedroid.dynamic.core;
    requires java.sql;
    requires jdk.unsupported;
    requires nixiedroid.dynamic.premain;
    requires java.desktop;
}