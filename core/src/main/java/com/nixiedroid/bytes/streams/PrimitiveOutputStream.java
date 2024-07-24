package com.nixiedroid.bytes.streams;

import com.nixiedroid.bytes.converter.Endianness;
import com.nixiedroid.bytes.interfaces.ThrowablePrimitiveWriter;
import org.jetbrains.annotations.NotNull;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@SuppressWarnings({"unused", "MagicNumber"})
public class PrimitiveOutputStream extends FilterOutputStream implements ThrowablePrimitiveWriter<IOException> {

    /**
     * The number of bytes written to the data output stream so far.
     * If this counter overflows, it will be wrapped to Integer.MAX_VALUE.
     */
    protected int written;

    private static final int FF = 0xFF;

    private Endianness endianness;

    public PrimitiveOutputStream(OutputStream out) {
        super(out);
    }

    private void incCount(int value) {
        int temp = this.written + value;
        if (temp < 0) {
            temp = Integer.MAX_VALUE;
        }
        this.written = temp;
    }

    public synchronized void write(int b) throws IOException {
        this.out.write(b);
        incCount(1);
    }

    public synchronized void write(byte @NotNull []  b, int off, int len)
            throws IOException {
        this.out.write(b, off, len);
        incCount(len);
    }

    @Override
    public void writeByte(byte by, Endianness e) throws IOException {
        this.out.write(by);
        incCount(1);
    }

    @Override
    public void writeShort(short s, Endianness e) throws IOException {
        if (e == Endianness.BIG_ENDIAN) {
            this.out.write((s >>> 8) & FF);
            this.out.write((s) & FF);
        } else {
            this.out.write((s) & FF);
            this.out.write((s >>> 8) & FF);
        }
        incCount(2);

    }

    @Override
    public void writeInteger(int i, Endianness e) throws IOException {
        if (e == Endianness.BIG_ENDIAN) {
            this.out.write((i >>> 24) & FF);
            this.out.write((i >>> 16) & FF);
            this.out.write((i >>>  8) & FF);
            this.out.write((i) & FF);
        } else {
            this.out.write((i) & FF);
            this.out.write((i >>>  8) & FF);
            this.out.write((i >>> 16) & FF);
            this.out.write((i >>> 24) & FF);
        }
        incCount(4);
    }

    private final byte[] writeBuffer = new byte[8];

    @Override
    public void writeLong(long l, Endianness e) throws IOException {
        if (e == Endianness.BIG_ENDIAN) {
            this.writeBuffer[0] = (byte)(l >>> 56);
            this.writeBuffer[1] = (byte)(l >>> 48);
            this.writeBuffer[2] = (byte)(l >>> 40);
            this.writeBuffer[3] = (byte)(l >>> 32);
            this.writeBuffer[4] = (byte)(l >>> 24);
            this.writeBuffer[5] = (byte)(l >>> 16);
            this.writeBuffer[6] = (byte)(l >>>  8);
            this.writeBuffer[7] = (byte)(l);
        } else {
            this.writeBuffer[7] = (byte)(l);
            this.writeBuffer[6] = (byte)(l >>>  8);
            this.writeBuffer[5] = (byte)(l >>> 16);
            this.writeBuffer[4] = (byte)(l >>> 24);
            this.writeBuffer[3] = (byte)(l >>> 32);
            this.writeBuffer[2] = (byte)(l >>> 40);
            this.writeBuffer[1] = (byte)(l >>> 48);
            this.writeBuffer[0] = (byte)(l >>> 56);
        }
        this.out.write(this.writeBuffer, 0, 8);
        incCount(8);
    }

    @Override
    public void writeFloat(float f, Endianness e) throws IOException {
        writeInteger(Float.floatToIntBits(f),e);
    }

    @Override
    public void writeDouble(double d, Endianness e) throws IOException {
        writeLong(Double.doubleToLongBits(d),e);
    }

    @Override
    public void writeByte(byte by) throws IOException {
        writeByte(by, this.endianness);
    }

    @Override
    public void writeShort(short s) throws IOException {
        writeShort(s, this.endianness);
    }

    @Override
    public void writeInteger(int i) throws IOException {
        writeInteger(i, this.endianness);
    }

    @Override
    public void writeLong(long l) throws IOException {
        writeLong(l, this.endianness);
    }

    @Override
    public void writeFloat(float f) throws IOException {
        writeFloat(f, this.endianness);
    }

    @Override
    public void writeDouble(double d) throws IOException {
        writeDouble(d, this.endianness);
    }

    @Override
    public void setEndiannes(Endianness e) {
        this.endianness = e;
    }

    public final int size() {
        return this.written;
    }
}
