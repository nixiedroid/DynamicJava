package com.nixiedroid.classblob;

import com.nixiedroid.bytes.StringArrayUtils;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class ClassReader {
    public ClassReader() throws IOException {
        FileOutputStream fos = new FileOutputStream("out.bin");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BlobHeader header = new BlobHeader((short) 1, (short) 1);
        ClassHeader clh = new ClassHeader(new ClassHeaderFlags(0x21), 0xFFF, 0x22, "com.nixiedroid.text", 0x3a);
        ClassHeader clh2 = new ClassHeader(new ClassHeaderFlags(0x21), 0x100200, 0x66, "com.nixiedroid.test", 0x4d);
        List<ClassHeader> headers = new LinkedList<>();
        headers.add(clh);
        headers.add(clh2);
        header.setClassesCount(headers.size());
        List<ClassBytes> classes = new LinkedList<>();
        ClassesBlob blob = new ClassesBlob(header, headers, classes);
        blob.serialize(baos);
        System.out.println(StringArrayUtils.toString(baos.toByteArray()));
        baos.writeTo(fos);
        fos.flush();
        fos.close();

        FileInputStream fis = new FileInputStream("out.bin");
        ByteArrayInputStream bais = new ByteArrayInputStream(fis.readAllBytes());
        ClassesBlob blob2 = new ClassesBlob(bais);
        System.out.println(blob2);
    }
}
