package com.nixiedroid.classblob;

import com.nixiedroid.bytes.ByteArrayUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class BlobHeader extends ByteSerializable<BlobHeader> {
    private static final int magic = 0xCAFEB10B;
    private int ver_major;
    private int ver_minor;
    private BlobHeader(){}
    public BlobHeader(int major, int minor){
        this.ver_minor = minor;
        this.ver_major = major;
    }


    @Override
    public BlobHeader deserialize(ByteArrayInputStream stream) throws IOException {
        if (stream == null) throw new IOException("Input is null");
        if (stream.available() < 8) throw new IOException("Input is too short");
        int magic = ByteArrayUtils.chunk.toInt32B(stream.readNBytes(4));
        if (magic!=BlobHeader.magic) throw new IOException("Invalid signature");
        ver_major = ByteArrayUtils.chunk.toInt16L(stream.readNBytes(2));
        ver_minor = ByteArrayUtils.chunk.toInt16L(stream.readNBytes(2));
        if (ver_minor < 0 || ver_major < 0) throw new IOException("Invalid Header Version:"+ ver_major + "." + ver_minor );
        return this;
    }

    @Override
    public void serialize(ByteArrayOutputStream stream) throws IOException {
        if (stream == null) throw new IOException();
        stream.write(ByteArrayUtils.fast.int32ToBytesB(magic));
        stream.write(ByteArrayUtils.fast.int16ToBytesL((short) ver_major));
        stream.write(ByteArrayUtils.fast.int16ToBytesL((short) ver_minor));
    }

    @Override
    public String toString() {
        return "BLOB Header: Ver" + ver_major + "." + ver_minor;
    }
}
