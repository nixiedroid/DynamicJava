package com.nixiedroid;

import com.nixiedroid.bytes.ByteArrayUtils;
import com.nixiedroid.bytes.Endiannes;
import com.nixiedroid.classblob.BlobHeader;
import com.nixiedroid.classblob.ClassHeader;
import com.nixiedroid.classblob.ClassHeaderFlags;

import java.io.*;


public class Main {
    Main() throws IOException {
        generics();
        bytes();
    }

    /**
     * @see jdk.internal.misc.MainMethodFinder;
     */
    @SuppressWarnings("JavadocReference")
    public static void main(String[] args) throws Exception {
        new Main();
    }

    public void generics() {
//        List list = new LinkedList();
//        list.add(1);
//        list.add("hello");
//        list.stream().forEach(System.out::println);
//        list.stream().forEach(o -> o = (Integer) o - 1);
//        list.stream().forEach(System.out::println);
    }

    public void bytes() {
        short i = (short) 0xfff0;
        System.out.println(i);
        byte[] data = ByteArrayUtils.i().toBytes(i,Endiannes.LITTLE);
        System.out.println(ByteArrayUtils.toString(data));
        int ii = ByteArrayUtils.chunk.toInt16L(data);
        System.out.printf("%04X\n", (ii & 0xFFFF));
        System.out.println(i & 0xFFFF);
        var in = (long) 1234567890L;
        System.out.println(in);
        var inwt = ByteArrayUtils.i().fromBytes(ByteArrayUtils.i().toBytes(in,Endiannes.LITTLE),Endiannes.LITTLE);
        System.out.println(inwt +" " + inwt.getClass().getName());
    }


    public void classBlob() throws IOException {
        FileOutputStream fos = new FileOutputStream("out.bin");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BlobHeader header = new BlobHeader(1, 1);
        ClassHeader clh = new ClassHeader(new ClassHeaderFlags(0x12345678), 0x100200);
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
        System.out.printf("%02x\n", bais.read());
        System.out.printf("%02x\n", bais.read());
        System.out.printf("%02x\n", bais.read());
        System.out.printf("%02x\n", bais.read());
        System.out.println(header);
        fis.close();
    }


}


// jdk.internal.org.objectweb.asm
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

