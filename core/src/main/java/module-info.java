module nixiedroid.dynamic.core {
    exports com.nixiedroid.classloaders;
    exports com.nixiedroid.modules;
    exports com.nixiedroid.modules.util;
    exports com.nixiedroid.runtime;
    exports com.nixiedroid.unsafe;
    requires java.instrument;
    requires java.management;
    requires jdk.management;
    requires jdk.unsupported;
    requires org.apache.bcel;
    requires org.jetbrains.annotations;
}