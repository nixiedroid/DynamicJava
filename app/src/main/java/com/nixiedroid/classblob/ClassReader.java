package com.nixiedroid.classblob;

import com.nixiedroid.bytes.streams.PrimitiveInputStream;
import com.nixiedroid.bytes.streams.PrimitiveOutputStream;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("MagicNumber")
public class ClassReader {
    public ClassReader() throws IOException {
        FileOutputStream fos = new FileOutputStream("out.bin");
        PrimitiveOutputStream baos = new PrimitiveOutputStream(fos);
        BlobHeader header = new BlobHeader((short) 1, (short) 1);
        ClassHeader clh = new ClassHeader(new ClassHeaderFlags(0x21), 0x2B, 0x10, "com.nixiedroid.text", 0x3a);
        ClassHeader clh2 = new ClassHeader(new ClassHeaderFlags(0x21), 0x0, 0x10, "com.nixiedroid.test", 0x4d);
        List<ClassHeader> headers = List.of(clh,clh2);
        header.setClassesCount(headers.size());
        List<ClassBytes> classes = new LinkedList<>();
        ClassesBlob blob = new ClassesBlob(header, headers, classes);
        blob.serialize(baos);
        baos.flush();
        fos.flush();
        fos.close();

        FileInputStream fis = new FileInputStream("out.bin");
        PrimitiveInputStream bais = new PrimitiveInputStream(fis);
        ClassesBlob blob2 = new ClassesBlob(bais);
        System.out.println(blob2);
    }
}
