package com.nixiedroid.classblob;

import com.nixiedroid.bytes.streams.PrimitiveInputStream;
import com.nixiedroid.bytes.streams.PrimitiveOutputStream;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("unused")
public class ClassesBlob extends ByteSerializable<ClassesBlob> {
    BlobHeader blobHeader;
    List<ClassHeader> classHeaders;
    List<ClassBytes> classes;

    public ClassesBlob(PrimitiveInputStream stream) throws IOException {
        super(stream);
    }

    public ClassesBlob(BlobHeader blobHeader, List<ClassHeader> classHeaders, List<ClassBytes> classes) {
        this.blobHeader = blobHeader;
        this.classHeaders = classHeaders;
        this.classes = classes;
    }

    @Override
    void deserialize(PrimitiveInputStream stream) throws IOException {
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
            int read = stream.read(classBytes,0,clSize);
          //  if (read!=clSize) throw new IOException("Not enough data");
            this.classes.add(new ClassBytes(classBytes));
        }
    }

    @Override
    public void serialize(PrimitiveOutputStream stream) throws IOException {
        this.blobHeader.serialize(stream);
        for (ClassHeader h: this.classHeaders) {
            h.serialize(stream);
        }
        for (ClassBytes b: this.classes) {
            stream.write(b.getClassBytes());
        }
    }

    @Override
    public String toString() {
        return "ClassesBlob{\n" +
                "blobHeader=" + this.blobHeader.toString() +
                ", \nclassHeaders=" + this.classHeaders.toString() +
                ", \nclasses=" + this.classes.toString() +
                '}';
    }
}
