package com.nixiedroid;

import com.nixiedroid.asm.Asm;
import com.nixiedroid.bytes.ByteArrayUtils;
import com.nixiedroid.classblob.BlobHeader;
import com.nixiedroid.classblob.ClassHeader;
import com.nixiedroid.classblob.ClassHeaderFlags;
import com.nixiedroid.classloaders.ClassParser;
import com.nixiedroid.exceptions.Thrower;

import java.io.*;
import java.util.function.Function;


public class Main {
    class Func implements Function<Class<?>,String> {
        @Override
        public String apply(Class<?> clazz) {
            return clazz.getSimpleName();
        }
    }
    Main() throws IOException {
        generics();

        funcMethod();
        classBlob();
        new Asm();
    }


    public void funcMethod(){
        Func f = new Func();
        System.out.println();
    }


    /**
     * @see jdk.internal.misc.MainMethodFinder;
     */
    @SuppressWarnings("JavadocReference")
    public static void main(String[] args) throws Exception {
        new Main();
    }

    public void generics() {
//        CallSite fact = null;
//        try {
//            fact = java.lang.invoke.LambdaMetafactory.altMetafactory(
//                    MethodHandles.lookup(), "test",
//                    MethodType.methodType(int.class)
//            );
//        } catch (LambdaConversionException e) {
//            throw new AssertionError(e);
//        }
//        System.out.println(fact.type());


    }

    public void classBlob() throws IOException {
        FileOutputStream fos = new FileOutputStream("out.bin");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BlobHeader header = new BlobHeader(1, 1);
        ClassHeader clh = new ClassHeader(new ClassHeaderFlags(0x12345678), 0x100200,0);
        header.serialize(baos);
        clh.serialize(baos);
        System.out.println(ByteArrayUtils.toString(baos.toByteArray()));
        baos.writeTo(fos);
        fos.flush();
        fos.close();

        FileInputStream fis = new FileInputStream("out.bin");
        ByteArrayInputStream bais = new ByteArrayInputStream(fis.readAllBytes());
        header = new BlobHeader(3, 4);
        header.deserialize(bais);
        clh.deserialize(bais);
        for (int i = 0; i <  5; i++) {
            System.out.printf("%02x ", bais.read());
        }
        System.out.println("");
        bais.reset();
        for (int i = 0; i <  5; i++) {
            System.out.printf("%02x ", bais.read());
        }
        System.out.println("");
        System.out.println(header);
        fis.close();

        ClassHeaderFlags flags = new ClassHeaderFlags(0xFF);
        System.out.println(flags);
    }

}



