package classloading;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.Assertions;

public class BootstrapClassloaderTest {
        @Test
    public void test() {
            Assertions.assertNull(java.lang.ClassLoader.class.getClassLoader());
            Assertions.assertEquals("app",this.getClass().getClassLoader().getName());
        }
}
