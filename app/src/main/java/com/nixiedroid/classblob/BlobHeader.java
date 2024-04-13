package com.nixiedroid.classblob;

import com.nixiedroid.bytes.ByteArrayUtils;
import com.nixiedroid.bytes.Endiannes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class BlobHeader extends ByteSerializable<BlobHeader> {
    private static final int magic = 0xCAFEB10B;
    private short ver_major;
    private short ver_minor;
    private int classesCount;
    public static final int SIZE = Short.BYTES*2 + Integer.BYTES*2;
    private BlobHeader(){}
    public BlobHeader(short major, short minor){
        this.ver_minor = minor;
        this.ver_major = major;
    }
    public BlobHeader(short major,short minor,int classesCount){
        this(major,minor);
        this.classesCount = classesCount;
    }

    BlobHeader(ByteArrayInputStream stream) throws IOException {
        super(stream);
    }

    public int getClassesCount() {
        return this.classesCount;
    }

    public void setClassesCount(int classesCount) {
        this.classesCount = classesCount;
    }

    @Override
    public BlobHeader deserialize(ByteArrayInputStream stream) throws IOException {
        if (stream == null) throw new IOException("Input is null");
        if (stream.available() < SIZE) throw new IOException("Input is too short");
        int magic = ByteArrayUtils.i().fromBytes(stream.readNBytes(4), Endiannes.BIG);
        if (magic!=BlobHeader.magic) throw new IOException("Invalid signature " + Integer.toHexString(magic));
        this.ver_major = (short)ByteArrayUtils.i().fromBytes(stream.readNBytes(2));
        this.ver_minor = (short) ByteArrayUtils.i().fromBytes(stream.readNBytes(2));
        if (this.ver_minor < 0 || this.ver_major < 0) throw new IOException("Invalid Header Version:"+ ver_major + "." + ver_minor );
        this.classesCount = ByteArrayUtils.i().fromBytes(stream.readNBytes(4));
        if (this.classesCount < 0 ) throw new IOException("Invalid Classes Count:"+ this.classesCount);
        return this;
    }

    @Override
    public void serialize(ByteArrayOutputStream stream) throws IOException {
        if (stream == null) throw new IOException();
        stream.write(ByteArrayUtils.i().toBytes (magic,Endiannes.BIG));
        stream.write(ByteArrayUtils.i().toBytes((short) ver_major));
        stream.write(ByteArrayUtils.i().toBytes((short) ver_minor));
        stream.write(ByteArrayUtils.i().toBytes(classesCount));
    }

    @Override
    public String toString() {
        return "BLOB Header: Ver " + this.ver_major + "." + this.ver_minor;
    }
}
