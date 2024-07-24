package com.nixiedroid.classblob;

import com.nixiedroid.bytes.converter.Endianness;
import com.nixiedroid.bytes.streams.PrimitiveInputStream;
import com.nixiedroid.bytes.streams.PrimitiveOutputStream;

import java.io.IOException;

public class BlobHeader extends ByteSerializable<BlobHeader> {
    public static final int SIZE = Short.BYTES * 2 + Integer.BYTES * 2;
    private static final int magic = 0xCAFEB10B;
    private short ver_major;
    private short ver_minor;
    private int classesCount;

    public BlobHeader(short major, short minor) {
        this.ver_minor = minor;
        this.ver_major = major;
    }

    public BlobHeader(short major, short minor, int classesCount) {
        this(major, minor);
        this.classesCount = classesCount;
    }

    BlobHeader(PrimitiveInputStream stream) throws IOException {
        super(stream);
    }

    public int getClassesCount() {
        return this.classesCount;
    }

    public void setClassesCount(int classesCount) {
        this.classesCount = classesCount;
    }


    @Override
    public void deserialize(PrimitiveInputStream stream) throws IOException {
        if (stream == null) throw new IOException("Input is null");
        if (stream.available() < SIZE) throw new IOException("Input is too short");
        int magic = stream.readInteger(Endianness.BIG_ENDIAN);
        if (magic != BlobHeader.magic) throw new IOException("Invalid signature " + Integer.toHexString(magic));
        this.ver_major = stream.readShort();
        this.ver_minor = stream.readShort();
        if (this.ver_minor < 0 || this.ver_major < 0)
            throw new IOException("Invalid Header Version:" + this.ver_major + "." + this.ver_minor);
        this.classesCount = stream.readInteger();
        if (this.classesCount < 0) throw new IOException("Invalid Classes Count:" + this.classesCount);
    }

    @Override
    public void serialize(PrimitiveOutputStream stream) throws IOException {
        if (stream == null) throw new IOException();
        stream.writeInteger(magic, Endianness.BIG_ENDIAN);
        stream.writeShort(this.ver_major);
        stream.writeShort(this.ver_minor);
        stream.writeInteger(this.classesCount);
    }

    @Override
    public String toString() {
        return "BLOB Header: Ver " + this.ver_major + "." + this.ver_minor;
    }
}
