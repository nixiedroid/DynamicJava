package reflection;

import org.junit.jupiter.api.Test;
import samples.Clazz;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class FieldTest
{

    @Test
    void reflections() {
        Class<Clazz> cactus = Clazz.class;
        System.out.println(cactus);
        System.out.println(Modifier.toString(cactus.getModifiers()));
        System.out.println();
        Field[] fields = cactus.getDeclaredFields();
        for (Field f : fields) {
            System.out.println(f.getName() + " is " + f.getType());
        }
    }
}
