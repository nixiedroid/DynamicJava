package samples;

import java.io.Serializable;
@SuppressWarnings("unused")
public class Clazz implements Serializable {
    boolean sBoolean;
    Boolean oBoolean;
    boolean[] aBoolean;
    short sShort;
    Short oShort;
    short[] aShort;
    char sChar;
    Character oChar;
    char[] aChar;
    int sInteger = 4;
    Integer oInteger;
    int[] aInteger;
    Object obj;
    Object[] aObj;
    public static void sayStatic(String word){
        System.out.println(word + " Static");
    }

    public void setsInteger(int sInteger) {
        this.sInteger = sInteger;
    }

    public void sayDynamic(String word){
        System.out.println(word + " Dynamic");
    }
    private static void privSayStatic(String word){
        System.out.println(word + " Private Static");
    }

    public short getsShort() {
        return this.sShort;
    }

    public void setsShort(short sShort) {
        this.sShort = sShort;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public Object getObj() {
        return this.obj;
    }
}
