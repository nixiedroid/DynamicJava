package classloading;

import com.nixiedroid.plugins.PluginWrapper;
import com.nixiedroid.reflection.Method;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ClassLoaderTest {
    @Test
    void testLoadingFromClassPath(){
        Object plugin = PluginWrapper.loadPluginFromCP("com.nixiedroid.plugins.Plugin");
        Assertions.assertEquals("Plugin 1 Method", Method.execute(plugin,"method"));
        Assertions.assertEquals("Plugin 1 Static Method", Method.execute(plugin,"staticMethod"));
        Assertions.assertEquals("Plugin 1 Static Method", Method.executeStatic(plugin,"staticMethod"));
    }
}
