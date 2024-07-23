package com.nixiedroid.classblob;

import com.nixiedroid.bytes.ByteArrayConverter;
import com.nixiedroid.bytes.ByteArrayConverterDefault;
import com.nixiedroid.bytes.Endiannes;
import com.nixiedroid.bytes.StringArrayUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
    private final ByteArrayConverter converter = new ByteArrayConverterDefault();


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
        this.flags = new ClassHeaderFlags(this.converter.readInteger(stream.readNBytes(4),0, Endiannes.LITTLE));
        this.offset = this.converter.readInteger(stream.readNBytes(4),0,Endiannes.LITTLE);
        this.classSize = this.converter.readInteger(stream.readNBytes(4),0,Endiannes.LITTLE);
        if (this.flags.isFlagSet(ClassHeaderFlags.NAME_PROVIDED)) {
            if (stream.available() < 2) throw new IOException("Input is too short");
            this.nameLen = this.converter.readShort(stream.readNBytes(2),0,Endiannes.LITTLE);
            if (stream.available() < this.nameLen) throw new IOException("Input is too short");
            this.name = StringArrayUtils.StringFromBytes(stream.readNBytes(this.nameLen));
        }
        if (this.flags.isFlagSet(ClassHeaderFlags.ENCRYPTED) || this.flags.isFlagSet(ClassHeaderFlags.DECRYPTER)) {
            if (stream.available() < 4) throw new IOException("Input is too short");
            this.encAlgorithm = this.converter.readInteger(stream.readNBytes(4),0,Endiannes.LITTLE);
        }
        return this;
    }

    @Override
    public void serialize(ByteArrayOutputStream stream) throws IOException {
        if (stream == null) throw new IOException();
        byte[] arr = new byte[Integer.BYTES*3];
        this.converter.writeInteger(arr,0,this.flags.toInt(),Endiannes.LITTLE);
        this.converter.writeInteger(arr,4,this.offset,Endiannes.LITTLE);
        this.converter.writeInteger(arr,8,this.classSize,Endiannes.LITTLE);
        stream.write(arr);
        if (this.flags.isFlagSet(ClassHeaderFlags.NAME_PROVIDED)) {
            arr = new byte[2];
            this.converter.writeShort(arr,0,this.nameLen,Endiannes.LITTLE);
            stream.write(arr);
            stream.write(StringArrayUtils.utf8toBytes(this.name));
        }

        if (this.flags.isFlagSet(ClassHeaderFlags.ENCRYPTED) || this.flags.isFlagSet(ClassHeaderFlags.DECRYPTER)) {
            arr = new byte[2];
            this.converter.writeInteger(arr,0,this.encAlgorithm,Endiannes.LITTLE);
            stream.write(arr);
        }
    }
}
