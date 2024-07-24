package classes;

import com.nixiedroid.bytes.converter.StringArrayUtils;
import com.nixiedroid.classes.JavaClassParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class HiddenClassTest {

    String wrongPackageBlob = "CAFEBABE0000003400110A0004000D08000E07000F0700100100063C696E6974" +
            "3E010003282956010004436F646501000F4C696E654E756D6265725461626C65" +
            "01000367657401001428294C6A6176612F6C616E672F537472696E673B01000A" +
            "536F7572636546696C6501000B53616D706C652E6A6176610C0005000601000F" +
            "48656C6C6F2066726F6D20424C4F42010001480100106A6176612F6C616E672F" +
            "4F626A656374002000030004000000000002000000050006000100070000001D" +
            "00010001000000052AB70001B100000001000800000006000100000001000100" +
            "09000A000100070000001B00010001000000031202B000000001000800000006" +
            "0001000000030001000B00000002000C";

    String blob = "CAFEBABE00000034000C0A0003000907000A07000B0100063C696E6" +
            "9743E010003282956010004436F64650100036765740100032829490C0004000" +
            "5010009636C61737365732F480100106A6176612F6C616E672F4F626A656374" +
            "002000020003000000000002000000040005000100060000001100010001000" +
            "000052AB70001B100000000000000070008000100060000000F000100010000" +
            "0003102AAC000000000000";


    @Test
    void testHiddenClassWrongPackage() {
        byte[] cb = StringArrayUtils.fromHexString(this.wrongPackageBlob);
        Assertions.assertEquals(this.wrongPackageBlob, StringArrayUtils.toString(cb));
        JavaClassParser.ClassInfo ci = JavaClassParser.create(cb);
        Assertions.assertEquals("H", ci.getName());
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> MethodHandles.lookup().defineHiddenClass(cb, false));
    }

    @Test
    void testHiddenClass() throws Throwable {
        final int FOURTY_TWO = 42;
        byte[] cb = StringArrayUtils.fromHexString(this.blob);
        Assertions.assertEquals(this.blob, StringArrayUtils.toString(cb));
        JavaClassParser.ClassInfo ci = JavaClassParser.create(cb);
        Assertions.assertEquals("classes.H", ci.getName());
        MethodHandles.Lookup lookup = MethodHandles.lookup().defineHiddenClass(cb, false);
        Class<?> hiddenClass = lookup.lookupClass();
        MethodHandle get = lookup.findVirtual(hiddenClass, "get", MethodType.methodType(int.class));
        Object fourtyTwo = hiddenClass.getDeclaredConstructor().newInstance();
        Assertions.assertEquals(FOURTY_TWO, get.invoke(fourtyTwo));
    }
}
