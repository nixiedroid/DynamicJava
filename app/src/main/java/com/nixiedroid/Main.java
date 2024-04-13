package com.nixiedroid;

import com.nixiedroid.asm.Asm;
import com.nixiedroid.bytes.ByteArrayUtils;
import com.nixiedroid.classblob.*;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;


public class Main {

    Main() throws IOException {
        classBlob();
    }





    /**
     * @see jdk.internal.misc.MainMethodFinder;
     */
    @SuppressWarnings("JavadocReference")
    public static void main(String[] args) throws Throwable {
        new Main();
    }



    public void classBlob() throws IOException {
        FileOutputStream fos = new FileOutputStream("out.bin");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BlobHeader header = new BlobHeader((short) 1, (short) 1);
        ClassHeader clh = new ClassHeader(new ClassHeaderFlags(0x21),
                0xFFF,
                0x22,"com.nixiedroid.text",0x3a);
        ClassHeader clh2 = new ClassHeader(new ClassHeaderFlags(0x21),
                0x100200,
                0x66,"com.nixiedroid.test",0x4d);
        List<ClassHeader> headers = new LinkedList<>();
        headers.add(clh);
        headers.add(clh2);
        header.setClassesCount(headers.size());
        List<ClassFile> classes = new LinkedList<>();
        ClassesBlob blob = new ClassesBlob(header,headers,classes);
        blob.serialize(baos);
        System.out.println(ByteArrayUtils.toString(baos.toByteArray()));
        baos.writeTo(fos);
        fos.flush();
        fos.close();

        FileInputStream fis = new FileInputStream("out.bin");
        ByteArrayInputStream bais = new ByteArrayInputStream(fis.readAllBytes());
        ClassesBlob blob2 = new ClassesBlob(bais);
        System.out.println(blob2);
    }
}



