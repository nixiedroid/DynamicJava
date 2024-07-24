package com.nixiedroid.bytes.streams;

import com.nixiedroid.bytes.converter.Endianness;
import com.nixiedroid.bytes.interfaces.ThrowablePrimitiveReader;

import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

@SuppressWarnings({"unused", "MagicNumber"})
public class PrimitiveInputStream extends FilterInputStream implements ThrowablePrimitiveReader<IOException> {

    private static final int FF = 0xFF;

    private Endianness endianness = Endianness.LITTLE_ENDIAN;
    private final byte[] readBuffer = new byte[8];

    public PrimitiveInputStream(InputStream in) {
        super(in);
    }

    public final void readFully(byte[] b) throws IOException {
        readFully(b, 0, b.length);
    }

    public final void readFully(byte[] b, int off, int len) throws IOException {
        if (len < 0)
            throw new IndexOutOfBoundsException();
        int n = 0;
        while (n < len) {
            int count = this.in.read(b, off + n, len - n);
            if (count < 0)
                throw new EOFException();
            n += count;
        }
    }

    @Override
    public byte readByte(Endianness e) throws IOException {
        int ch = this.in.read();
        if (ch < 0)
            throw new EOFException();
        return (byte) (ch & FF);
    }

    @Override
    public short readShort(Endianness e) throws IOException {
        int ch1 = this.in.read();
        int ch2 = this.in.read();
        if ((ch1 | ch2) < 0) throw new EOFException();
        if (e == Endianness.BIG_ENDIAN) {
            return (short) ((ch1 & FF) << 8 | (ch2 & FF));
        }
        return (short) ((ch1 & FF) | (ch2 & FF) << 8);
    }

    @Override
    public int readInteger(Endianness e) throws IOException {
        int ch1 = this.in.read();
        int ch2 = this.in.read();
        int ch3 = this.in.read();
        int ch4 = this.in.read();
        if ((ch1 | ch2 | ch3 | ch4) < 0) throw new EOFException();

        if (e == Endianness.BIG_ENDIAN) {
            return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4));
        }
        return ((ch1) + (ch2 << 8) + (ch3 << 16) + (ch4 << 24));
    }

    @Override
    public long readLong(Endianness e) throws IOException {
        readFully(this.readBuffer, 0, 8);
        if (e == Endianness.BIG_ENDIAN) {
            return ((long) this.readBuffer[0] & FF) << 56 |
                    ((long) this.readBuffer[1] & FF) << 48 |
                    ((long) this.readBuffer[2] & FF) << 40 |
                    ((long) this.readBuffer[3] & FF) << 32 |
                    ((long) this.readBuffer[4] & FF) << 24 |
                    ((long) this.readBuffer[5] & FF) << 16 |
                    ((long) this.readBuffer[6] & FF) << 8 |
                    ((long) this.readBuffer[7] & FF);
        }
        return ((long) this.readBuffer[7] & FF) << 56 |
                ((long) this.readBuffer[6] & FF) << 48 |
                ((long) this.readBuffer[5] & FF) << 40 |
                ((long) this.readBuffer[4] & FF) << 32 |
                ((long) this.readBuffer[3] & FF) << 24 |
                ((long) this.readBuffer[2] & FF) << 16 |
                ((long) this.readBuffer[1] & FF) << 8 |
                ((long) this.readBuffer[0] & FF);

    }

    @Override
    public float readFloat(Endianness e) throws IOException {
        return Float.intBitsToFloat(readInteger(e));
    }

    @Override
    public double readDouble(Endianness e) throws IOException {
        return Double.longBitsToDouble(readLong(e));
    }

    @Override
    public byte readByte() throws IOException {
        return readByte(this.endianness);
    }

    @Override
    public short readShort() throws IOException {
        return readShort(this.endianness);
    }

    @Override
    public int readInteger() throws IOException {
        return readInteger(this.endianness);
    }

    @Override
    public long readLong() throws IOException {
        return readLong(this.endianness);
    }

    @Override
    public float readFloat() throws IOException {
        return readFloat(this.endianness);
    }

    @Override
    public double readDouble() throws IOException {
        return readDouble(this.endianness);
    }


    @Override
    public void setEndiannes(Endianness e) {
        this.endianness = e;
    }
}
