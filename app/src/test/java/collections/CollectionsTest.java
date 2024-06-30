package collections;

import com.nixiedroid.classloaders.ServletLoader;
import org.junit.jupiter.api.Test;

public class CollectionsTest {
    @Test
    void treeSetTest(){
        Package p = Class.class.getPackage();
        ServletLoader.class.getClassLoader().getDefinedPackage(p.getName());
        Package.getPackages();


    }
}
