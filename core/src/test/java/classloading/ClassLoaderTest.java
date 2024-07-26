package classloading;

import com.nixiedroid.classloaders.CryptedClassLoader;
import com.nixiedroid.classloaders.FileClassLoader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ClassLoaderTest {

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
    void classLoaderLazyDemo() throws ClassNotFoundException {
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
            Assertions.fail(e);
            throw e;
        }
        try {
            cla.getConstructor().newInstance();
        } catch (Exception e) {
            Assertions.fail(e);
        }
    }


}
