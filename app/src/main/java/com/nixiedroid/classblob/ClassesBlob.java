package com.nixiedroid.classblob;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ClassesBlob extends ByteSerializable {
    BlobHeader blobHeader;
    List<ClassHeader> classHeaders;
    List<ClassFile> classes;
    public ClassesBlob(ByteArrayInputStream stream) throws IOException {
        super(stream);
    }

    public ClassesBlob(BlobHeader blobHeader, List<ClassHeader> classHeaders, List<ClassFile> classes) {
        this.blobHeader = blobHeader;
        this.classHeaders = classHeaders;
        this.classes = classes;
    }

    @Override
    ClassesBlob deserialize(ByteArrayInputStream stream) throws IOException {
        this.classHeaders = new LinkedList<>();
        this.classes = new LinkedList<>();
        this.blobHeader = new BlobHeader(stream);
        for (int i = 0; i < this.blobHeader.getClassesCount(); i++) {
            this.classHeaders.add(new ClassHeader(stream));
        }
        return this;
    }

    @Override
    public void serialize(ByteArrayOutputStream stream) throws IOException {
        this.blobHeader.serialize(stream);
        for (ClassHeader h: this.classHeaders) {
            h.serialize(stream);
        }
    }
}
