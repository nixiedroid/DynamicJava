package classes;

import com.nixiedroid.bytes.ByteArrayUtils;
import com.nixiedroid.classloaders.parser.JavaClassParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class HiddenClassTest {

    String blob = "CAFEBABE0000003400110A0004000D08000E07000F0700100100063C696E6974" +
            "3E010003282956010004436F646501000F4C696E654E756D6265725461626C65" +
            "01000367657401001428294C6A6176612F6C616E672F537472696E673B01000A" +
            "536F7572636546696C6501000B53616D706C652E6A6176610C0005000601000F" +
            "48656C6C6F2066726F6D20424C4F42010001480100106A6176612F6C616E672F" +
            "4F626A656374002000030004000000000002000000050006000100070000001D" +
            "00010001000000052AB70001B100000001000800000006000100000001000100" +
            "09000A000100070000001B00010001000000031202B000000001000800000006" +
            "0001000000030001000B00000002000C";


    @Test
    void testHiddenClass() throws Throwable {
        byte[] cb = ByteArrayUtils.fromHexString(this.blob);
        Assertions.assertEquals(this.blob, ByteArrayUtils.toString(cb));
        JavaClassParser.ClassInfo ci = JavaClassParser.create(cb);
        Assertions.assertEquals("H", ci.getName());
        MethodHandles.Lookup lookup = MethodHandles.lookup().defineHiddenClass(cb, false);
        Class<?> hiddenClass = lookup.lookupClass();
        MethodHandle get = lookup.findVirtual(hiddenClass, "get", MethodType.methodType(String.class));
        Object hello = hiddenClass.getDeclaredConstructor().newInstance();
        Assertions.assertEquals("Hello from BLOB", get.invoke(hello));
    }
}
