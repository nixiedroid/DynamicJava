package access;

import com.nixiedroid.Main;
import com.nixiedroid.reflection.ModuleManager;
import com.nixiedroid.reflection.Modules;
import com.nixiedroid.runtime.Properties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.util.*;

public class ModulesEscapeTest {

    @Test
    public void modulesTest() {
        String FLOAT_CONSTANTS_CLASS_NAME = "jdk.internal.math.FloatConsts";
        System.out.println(Properties.getVersion());
        System.out.println(Main.class.getModule());
        System.out.println(ModuleManager.class.getModule());
        try {
            Class<?> cl = Class.forName(FLOAT_CONSTANTS_CLASS_NAME);
            //   ModuleManager.allowAccess(Main.class, FLOAT_CONSTANTS_CLASS_NAME, false);
            Field field = cl.getDeclaredField("SIGNIFICAND_WIDTH");
            System.out.println(field.get(null));
        } catch (ReflectiveOperationException e) {
            Assertions.fail("Should not have thrown any exception");
        }
    }

    @Test
    public void communism() {
        try {
            new Modules().exportAllToAll();

        } catch (Throwable exc) {
            Assertions.fail("Should not have thrown any exception");
        }
    }

    @Test
    void epicText() {
        System.out.println(System.getProperty("java.class.path"));
        System.out.println(System.getProperty("jdk.module.path"));
        Map<String, TreeSet<String>> map = new HashMap<>();
        for (Module m : ModuleLayer.boot().modules()) {
            String name = m.getName();
            map.computeIfAbsent(
                    Optional.ofNullable(m.getClassLoader()).
                            map(ClassLoader::getName)
                            .orElse("boot"), k -> new TreeSet<>()
            ).add(name);
        }
        List<Map.Entry<String, TreeSet<String>>> toSort = new ArrayList<>(map.entrySet());
        toSort.sort(Comparator.comparingInt(e -> List.of("boot", "platform", "app").indexOf(e.getKey())));
        for (Map.Entry<String, TreeSet<String>> e : toSort) {
            String s = e.getKey() + "\t" + String.join("\t", e.getValue());
            System.out.println(s);
        }
    }

    @Test
    void HookClass() throws Throwable {
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        Hook hook = new Hook();
        Class<?> hookClass = hook.getClass();
        Class<?> intUnsafeClass = cl.loadClass("jdk.internal.misc.Unsafe");
        ModuleManager.moveToJavaBase(hookClass);
        Assertions.assertEquals("java.base",hook.whereAmI());
        MethodHandles.Lookup l = hook.getLookup(intUnsafeClass);
        System.out.println(l.lookupClass().getName());
        MethodType mt = MethodType.methodType(intUnsafeClass);
        MethodHandle mh = l.findStatic(intUnsafeClass, "getUnsafe", mt);
        Object o = mh.invoke();
        mt = MethodType.methodType(long.class, Class.class, String.class);
        mh = l.findVirtual(intUnsafeClass, "objectFieldOffset", mt);
        Class<?> lClass = MethodHandles.Lookup.class;
        System.out.println(mh.invoke(o, lClass, "allowedModes"));
    }



    static class Hook {
        public String whereAmI() {
             return this.getClass().getModule().getName();
        }

        public MethodHandles.Lookup getLookup(Class<?> cl) throws Throwable {
            ClassLoader loader = ClassLoader.getSystemClassLoader();
            Class<?> intUnsafeClass = loader.loadClass(cl.getName());
            return MethodHandles.privateLookupIn(intUnsafeClass, MethodHandles.lookup());
        }

    }

}
