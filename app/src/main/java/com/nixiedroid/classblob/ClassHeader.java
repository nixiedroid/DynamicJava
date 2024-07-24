package com.nixiedroid.classblob;

import com.nixiedroid.bytes.converter.Endianness;
import com.nixiedroid.bytes.converter.StringArrayUtils;
import com.nixiedroid.bytes.streams.PrimitiveInputStream;
import com.nixiedroid.bytes.streams.PrimitiveOutputStream;

import java.io.IOException;

@SuppressWarnings("unused")
public class ClassHeader extends ByteSerializable<ClassHeader> {
    public static final int MIN_SIZE = Integer.BYTES * 3;
    private ClassHeaderFlags flags;
    private int offset;
    private int classSize;
    private short nameLen;
    private String name;
    private int encAlgorithm;

    public ClassHeader(PrimitiveInputStream stream) throws IOException {
        super(stream);
    }

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
    public void deserialize(PrimitiveInputStream stream) throws IOException {
        if (stream == null) throw new IOException("Input is null");
        if (stream.available() < MIN_SIZE) throw new IOException("Input is too short");
        this.flags = new ClassHeaderFlags(stream.readInteger(Endianness.LITTLE_ENDIAN));
        this.offset = stream.readInteger(Endianness.LITTLE_ENDIAN);
        this.classSize = stream.readInteger(Endianness.LITTLE_ENDIAN);
        if (this.flags.isFlagSet(ClassHeaderFlags.NAME_PROVIDED)) {
            if (stream.available() < 2) throw new IOException("Input is too short");
            this.nameLen = stream.readShort(Endianness.LITTLE_ENDIAN);
            if (stream.available() < this.nameLen) throw new IOException("Input is too short");
            this.name = StringArrayUtils.StringFromBytes(stream.readNBytes(this.nameLen));
        }
        if (this.flags.isFlagSet(ClassHeaderFlags.ENCRYPTED) || this.flags.isFlagSet(ClassHeaderFlags.DECRYPTER)) {
            if (stream.available() < 4) throw new IOException("Input is too short");
            this.encAlgorithm = stream.readInteger(Endianness.LITTLE_ENDIAN);
        }
    }

    @Override
    public void serialize(PrimitiveOutputStream stream) throws IOException {
        if (stream == null) throw new IOException();
        stream.writeInteger(this.flags.toInt(), Endianness.LITTLE_ENDIAN);
        stream.writeInteger(this.offset, Endianness.LITTLE_ENDIAN);
        stream.writeInteger(this.classSize, Endianness.LITTLE_ENDIAN);
        if (this.flags.isFlagSet(ClassHeaderFlags.NAME_PROVIDED)) {
            stream.writeShort(this.nameLen, Endianness.LITTLE_ENDIAN);
            stream.write(StringArrayUtils.utf8toBytes(this.name));
        }
        if (this.flags.isFlagSet(ClassHeaderFlags.ENCRYPTED) || this.flags.isFlagSet(ClassHeaderFlags.DECRYPTER)) {
            stream.writeInteger(this.encAlgorithm, Endianness.LITTLE_ENDIAN);
        }
    }

    @Override
    public String toString() {
        return "ClassHeader{" +
                "\nflags=" + this.flags +
                ", \noffset=" + this.offset +
                ", \nclassSize=" + this.classSize +
                ", \nnameLen=" + this.nameLen +
                ", \nname='" + this.name + '\'' +
                ", \nencAlgorithm=" + this.encAlgorithm +
                '}';
    }
}
