module nixiedroid.dynamic.core {
    exports com.nixiedroid.classloaders;
    exports com.nixiedroid.modules;
    exports com.nixiedroid.runtime;
    exports com.nixiedroid.unsafe;
    exports com.nixiedroid.bytes;
    exports com.nixiedroid.exceptions;
    exports com.nixiedroid.classloaders.finder;
    exports com.nixiedroid.unsafe.type;
    exports com.nixiedroid.classes;
    requires java.instrument;
    requires java.management;
    requires jdk.management;
    requires jdk.unsupported;
    requires org.jetbrains.annotations;
    requires java.desktop;
}