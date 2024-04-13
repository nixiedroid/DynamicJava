package com.nixiedroid.classblob;

import com.nixiedroid.bytes.ByteArrayUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class ClassHeader extends ByteSerializable<ClassHeader>{
    private ClassHeaderFlags flags;
    private int offset;
    private int classSize;
    private String name;
    private int encAlgorithm;
    public static final int MIN_SIZE = Integer.BYTES*3;


    public ClassHeader(ClassHeaderFlags flags, int offset,int classSize) {
        this.flags = flags;
        this.offset = offset;
        this.classSize = classSize;
    }

    public ClassHeader(ByteArrayInputStream stream) throws IOException {
        super(stream);
    }

    public ClassHeaderFlags getFlags() {
        return this.flags;
    }

    public int getOffset() {
        return this.offset;
    }

    public int getClassSize() {
        return this.classSize;
    }

    @Override
    public ClassHeader deserialize(ByteArrayInputStream stream) throws IOException {
        if (stream == null) throw new IOException("Input is null");
        if (stream.available() < MIN_SIZE) throw new IOException("Input is too short");
        this.flags = new ClassHeaderFlags(ByteArrayUtils.i().fromBytes(stream.readNBytes(4)));
        this.offset = ByteArrayUtils.i().fromBytes(stream.readNBytes(4)) ;
        this.classSize = ByteArrayUtils.i().fromBytes(stream.readNBytes(4));
        if (this.flags.isFlagSet(ClassHeaderFlags.NAME_PROVIDED)){
            if (stream.available() < 1) throw new IOException("Input is too short");
            stream.mark(0);
            int counter = 0;
            while (stream.read()>0) counter++;
            stream.reset();
            this.name = ByteArrayUtils.StringFromBytes(stream.readNBytes(counter));
        }
        if (this.flags.isFlagSet(ClassHeaderFlags.ENCRYPTED) || this.flags.isFlagSet(ClassHeaderFlags.DECRYPTER)){
            this.encAlgorithm = ByteArrayUtils.i().fromBytes(stream.readNBytes(4));
        }
        return this;
    }

    @Override
    public void serialize(ByteArrayOutputStream stream) throws IOException {
        if (stream == null) throw new IOException();
        stream.write(ByteArrayUtils.i().toBytes(this.flags.toInt()));
        stream.write(ByteArrayUtils.i().toBytes(this.offset));
        stream.write(ByteArrayUtils.i().toBytes(this.classSize));
        if (this.flags.isFlagSet(ClassHeaderFlags.NAME_PROVIDED)){
            stream.write(ByteArrayUtils.utf8toBytes(this.name));
            stream.write(0);
        }
        if (this.flags.isFlagSet(ClassHeaderFlags.ENCRYPTED) || this.flags.isFlagSet(ClassHeaderFlags.DECRYPTER)){
            stream.write(ByteArrayUtils.i().toBytes(this.encAlgorithm));
        }
    }
}
