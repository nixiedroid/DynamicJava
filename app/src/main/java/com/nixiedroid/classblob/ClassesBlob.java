package com.nixiedroid.classblob;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ClassesBlob extends ByteSerializable {
    BlobHeader blobHeader;
    List<ClassHeader> classHeaders;
    List<ClassBytes> classes;
    public ClassesBlob(ByteArrayInputStream stream) throws IOException {
        super(stream);
    }

    public ClassesBlob(BlobHeader blobHeader, List<ClassHeader> classHeaders, List<ClassBytes> classes) {
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
        for (int i = 0; i < this.blobHeader.getClassesCount(); i++) {
            int clSize = this.classHeaders.get(i).getClassSize();
            int clOffset = this.classHeaders.get(i).getOffset();
            byte[] classBytes = new byte[clSize];
            if (stream.read(classBytes,clOffset,clSize)!=clSize) throw new IOException("Not enough data");
            this.classes.add(new ClassBytes(classBytes));
        }
        return this;
    }

    @Override
    public void serialize(ByteArrayOutputStream stream) throws IOException {
        this.blobHeader.serialize(stream);
        for (ClassHeader h: this.classHeaders) {
            h.serialize(stream);
        }
        for (ClassBytes b: this.classes) {
            stream.write(b.getClassBytes());
        }
    }
}
