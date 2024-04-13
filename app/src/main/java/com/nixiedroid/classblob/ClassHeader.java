package com.nixiedroid.classblob;

import com.nixiedroid.bytes.ByteArrayUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class ClassHeader extends ByteSerializable<ClassHeader> {
    public static final int MIN_SIZE = Integer.BYTES * 3;
    private ClassHeaderFlags flags;
    private int offset;
    private int classSize;
    private short nameLen;
    private String name;
    private int encAlgorithm;


    public ClassHeader(ClassHeaderFlags flags, int offset, int classSize) {
        this.flags = flags;
        this.offset = offset;
        this.classSize = classSize;
    }

    public ClassHeader(ClassHeaderFlags flags, int offset, int classSize, String name) {
        this(flags, offset, classSize);
        this.name = name;
        this.nameLen = (short) name.length();
    }

    public ClassHeader(ClassHeaderFlags flags, int offset, int classSize, String name, int alg) {
        this(flags, offset, classSize, name);
        this.encAlgorithm = alg;
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

    public String getName() {
        return this.name;
    }

    public int getEncAlgorithm() {
        return this.encAlgorithm;
    }

    @Override
    public ClassHeader deserialize(ByteArrayInputStream stream) throws IOException {
        if (stream == null) throw new IOException("Input is null");
        if (stream.available() < MIN_SIZE) throw new IOException("Input is too short");
        this.flags = new ClassHeaderFlags(ByteArrayUtils.i().fromBytes(stream.readNBytes(4)));
        this.offset = ByteArrayUtils.i().fromBytes(stream.readNBytes(4));
        this.classSize = ByteArrayUtils.i().fromBytes(stream.readNBytes(4));
        if (this.flags.isFlagSet(ClassHeaderFlags.NAME_PROVIDED)) {
            if (stream.available() < 2) throw new IOException("Input is too short");
            this.nameLen = ByteArrayUtils.i().fromBytes(stream.readNBytes(2));
            if (stream.available() < this.nameLen) throw new IOException("Input is too short");
            this.name = ByteArrayUtils.StringFromBytes(stream.readNBytes(this.nameLen));
        }
        if (this.flags.isFlagSet(ClassHeaderFlags.ENCRYPTED) || this.flags.isFlagSet(ClassHeaderFlags.DECRYPTER)) {
            if (stream.available() < 4) throw new IOException("Input is too short");
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
        if (this.flags.isFlagSet(ClassHeaderFlags.NAME_PROVIDED)) {
            stream.write(ByteArrayUtils.i().toBytes(this.nameLen));
            stream.write(ByteArrayUtils.utf8toBytes(this.name));
        }
        if (this.flags.isFlagSet(ClassHeaderFlags.ENCRYPTED) || this.flags.isFlagSet(ClassHeaderFlags.DECRYPTER)) {
            stream.write(ByteArrayUtils.i().toBytes(this.encAlgorithm));
        }
    }
}
