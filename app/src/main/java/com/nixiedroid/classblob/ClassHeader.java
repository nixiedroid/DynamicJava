package com.nixiedroid.classblob;

import com.nixiedroid.bytes.ByteArrayUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class ClassHeader extends ByteSerializable<ClassHeader>{
    private ClassHeaderFlags flags;
    private int offset;

    public ClassHeader(ClassHeaderFlags flags, int offset) {
        this.flags = flags;
        this.offset = offset;
    }

    @Override
    public ClassHeader deserialize(ByteArrayInputStream stream) throws IOException {
        if (stream == null) throw new IOException("Input is null");
        if (stream.available() < 8) throw new IOException("Input is too short");
        this.flags = new ClassHeaderFlags(ByteArrayUtils.chunk.toInt32L(stream.readNBytes(4)));
        this.offset = ByteArrayUtils.chunk.toInt32L(stream.readNBytes(4));
        return this;
    }

    @Override
    public void serialize(ByteArrayOutputStream stream) throws IOException {
        if (stream == null) throw new IOException();
        stream.write(ByteArrayUtils.fast.int32ToBytesL(flags.toInt()));
        stream.write(ByteArrayUtils.fast.int32ToBytesL(offset));
    }
}
