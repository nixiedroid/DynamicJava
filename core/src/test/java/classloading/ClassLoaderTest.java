package classloading;

import com.nixiedroid.classloaders.CryptedClassLoader;
import com.nixiedroid.classloaders.FileClassLoader;
import org.junit.jupiter.api.Test;

public class ClassLoaderTest {
    @Test
    void testLoadingFromClassPath(){
//        Object plugin = PluginWrapper.loadPluginFromCP("com.nixiedroid.plugins.Plugin");
//        Assertions.assertEquals("Plugin 1 Methods", Methods.execute(plugin,"method"));
//        Assertions.assertEquals("Plugin 1 Static Methods", Methods.execute(plugin,"staticMethod"));
//        Assertions.assertEquals("Plugin 1 Static Methods", Methods.executeStatic(plugin,"staticMethod"));
    }
    @Test
    void loadPlugin() {
        String name = "com.nixiedroid.plugins.Plugin";
        ClassLoader classLoader = new CryptedClassLoader("com.nixiedroid.plugins", "enc");

        try {
            Class<?> cl = classLoader.loadClass(name);
            Object plugin = cl.getDeclaredConstructor().newInstance();
            System.out.println("ClassLoader of plugin is " + cl.getClassLoader().getName());
        } catch (Exception ignored) {
        }

    }

    @Test
    void classLoaderLazyDemo() {
        FileClassLoader cl = new FileClassLoader("resources", "clazz") {
            @Override
            public void loadClassInputLogging(String string) {
                System.out.println("Loading " + string);
            }
        };
        Class<?> cla;
        try {
            cla = cl.loadClass("resources.Cat");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        System.out.println("-----------");
        try {
            cla.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}
