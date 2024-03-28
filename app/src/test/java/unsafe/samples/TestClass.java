package unsafe.samples;

public class TestClass {
    public static int staticInt = 96;
    public final String finalString = "Tom";
    public final int finalInt = 2;
    public final boolean finalBoolean = true;
    public String unsetString;
    public int unsetInteger;
    public int preSetInteger = 3;
    public int preSetInteger2;
    public boolean preSetBoolean = true;

    {
        preSetInteger2 = 4;
    }

    public TestClass(int integer, String string) {
        this.unsetInteger = integer;
        this.unsetString = string;
    }
    public int getSum() {
        return unsetInteger + preSetInteger2 + preSetInteger + finalInt;
    }
}