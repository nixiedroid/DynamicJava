package com.nixiedroid.bytes.buffer;

import com.nixiedroid.bytes.converter.ByteArrayConverter;
import com.nixiedroid.bytes.converter.ByteArrayConverterDefault;
import com.nixiedroid.bytes.converter.Endianness;

public class ArrayBuffer implements Buffer{

    private byte[] buffer;
    private final ByteArrayConverter converter = new ByteArrayConverterDefault();
    private Endianness endianness = Endianness.LITTLE_ENDIAN; //Java uses little endian
    private int pointer = 0;

    public ArrayBuffer(int size) {
        this.buffer = new byte[size];
    }


    @Override
    public void reallocate(int size) {
        if(this.buffer.length > size) throw new IllegalArgumentException("Cannot shrink buffer");
        byte[] tmp = new byte[size];
        System.arraycopy(this.buffer,0,tmp,0,size);
        this.buffer = tmp;
    }

    @Override
    public void setEndiannes(Endianness e) {
        this.endianness = e;
    }

    @Override
    public void setPointer(int pointer) {
        this.pointer = (pointer);
    }

    @Override
    public int getPointer() {
        return this.pointer;
    }

    @Override
    public void resetPointer() {
        this.pointer =0;
    }

    @Override
    public boolean isFull() {
        return this.pointer >= this.buffer.length;
    }

    public byte[] getBuffer(){
        return this.buffer;
    }

    @Override
    public void free() {
        //Not required
    }

    @Override
    public byte readByte(Endianness e) {
        if (this.pointer + Size.BYTE > this.buffer.length) {
            throw new IllegalArgumentException("Not enough data to read byte");
        }
        byte value = this.converter.readByte(this.buffer, this.pointer, e);
        this.pointer += Size.BYTE;
        return value;
    }

    @Override
    public short readShort(Endianness e) {
        if (this.pointer + Size.SHORT > this.buffer.length) {
            throw new IllegalArgumentException("Not enough data to read short");
        }
        short value = this.converter.readShort(this.buffer, this.pointer, e);
        this.pointer += Size.SHORT;
        return value;
    }

    @Override
    public int readInteger(Endianness e) {
        if (this.pointer + Size.INTEGER > this.buffer.length) {
            throw new IllegalArgumentException("Not enough data to read integer");
        }
        int value = this.converter.readInteger(this.buffer, this.pointer, e);
        this.pointer += Size.INTEGER;
        return value;
    }

    @Override
    public long readLong(Endianness e) {
        if (this.pointer + Size.LONG > this.buffer.length) {
            throw new IllegalArgumentException("Not enough data to read long");
        }
        long value = this.converter.readLong(this.buffer, this.pointer, e);
        this.pointer += Size.LONG;
        return value;
    }

    @Override
    public float readFloat(Endianness e) {
        if (this.pointer + Size.FLOAT > this.buffer.length) {
            throw new IllegalArgumentException("Not enough data to read float");
        }
        float value = this.converter.readFloat(this.buffer, this.pointer, e);
        this.pointer += Size.FLOAT;
        return value;
    }

    @Override
    public double readDouble(Endianness e) {
        if (this.pointer + Size.DOUBLE > this.buffer.length) {
            throw new IllegalArgumentException("Not enough data to read double");
        }
        double value = this.converter.readDouble(this.buffer, this.pointer, e);
        this.pointer += Size.DOUBLE;
        return value;
    }

    @Override
    public void writeByte(byte by, Endianness e) {
        if (this.pointer + Size.BYTE > this.buffer.length) {
            throw new IllegalArgumentException("Not enough space to write byte");
        }
        this.converter.writeByte(this.buffer, this.pointer, by, e);
        this.pointer += Size.BYTE;
    }

    @Override
    public void writeShort(short s, Endianness e) {
        if (this.pointer + Size.SHORT > this.buffer.length) {
            throw new IllegalArgumentException("Not enough space to write short");
        }
        this.converter.writeShort(this.buffer, this.pointer, s, e);
        this.pointer += Size.SHORT;
    }

    @Override
    public void writeInteger(int i, Endianness e) {
        if (this.pointer + Size.INTEGER > this.buffer.length) {
            throw new IllegalArgumentException("Not enough space to write integer");
        }
        this.converter.writeInteger(this.buffer, this.pointer, i, e);
        this.pointer += Size.INTEGER;
    }

    @Override
    public void writeLong(long l, Endianness e) {
        if (this.pointer + Size.LONG > this.buffer.length) {
            throw new IllegalArgumentException("Not enough space to write long");
        }
        this.converter.writeLong(this.buffer, this.pointer, l, e);
        this.pointer += Size.LONG;
    }

    @Override
    public void writeFloat(float f, Endianness e) {
        if (this.pointer + Size.FLOAT > this.buffer.length) {
            throw new IllegalArgumentException("Not enough space to write float");
        }
        this.converter.writeFloat(this.buffer, this.pointer, f, e);
        this.pointer += Size.FLOAT;
    }

    @Override
    public void writeDouble(double d, Endianness e) {
        if (this.pointer + Size.DOUBLE > this.buffer.length) {
            throw new IllegalArgumentException("Not enough space to write double");
        }
        this.converter.writeDouble(this.buffer, this.pointer, d, e);
        this.pointer += Size.DOUBLE;
    }

    @Override
    public byte readByte() {
        return readByte(this.endianness); 
    }

    @Override
    public short readShort() {
        return readShort(this.endianness); 
    }

    @Override
    public int readInteger() {
        return readInteger(this.endianness); 
    }

    @Override
    public long readLong() {
        return readLong(this.endianness); 
    }

    @Override
    public float readFloat() {
        return readFloat(this.endianness); 
    }

    @Override
    public double readDouble() {
        return readDouble(this.endianness); 
    }

    @Override
    public void writeByte(byte by) {
        writeByte(by, this.endianness); 
    }

    @Override
    public void writeShort(short s) {
        writeShort(s, this.endianness); 
    }

    @Override
    public void writeInteger(int i) {
        writeInteger(i, this.endianness);
    }

    @Override
    public void writeLong(long l) {
        writeLong(l, this.endianness); 
    }

    @Override
    public void writeFloat(float f) {
        writeFloat(f, this.endianness); 
    }

    @Override
    public void writeDouble(double d) {
        writeDouble(d, this.endianness); 
    }
}
