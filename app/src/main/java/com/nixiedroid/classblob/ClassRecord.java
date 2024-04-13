package com.nixiedroid.classblob;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ClassRecord extends ByteSerializable{
    public ClassRecord(ByteArrayInputStream stream) throws IOException {
        super(stream);
    }

    @Override
    Object deserialize(ByteArrayInputStream stream) throws IOException {
        ClassHeader header = new ClassHeader(stream);
        stream.mark(0); //What is readAheadLimit??
        if (stream.skip(header.getOffset()) != header.getOffset()) throw new IOException("Too few data");
        ClassFile classFile = new ClassFile(stream.readNBytes(header.getClassSize()));
        stream.reset();
        return null;
    }

    @Override
    void serialize(ByteArrayOutputStream stream) throws IOException {

    }
}
