package com.nixiedroid.classes.assembler;

import com.nixiedroid.bytes.converter.StringArrayUtils;

public class ByteVectorImpl implements ByteVector{
    private byte[] data;
    private int pos;

    public ByteVectorImpl() {
        this(100);
    }

    public ByteVectorImpl(int sz) {
        this.data = new byte[sz];
        this.pos = -1;
    }

    public int getLength() {
        return this.pos + 1;
    }

    public byte get(int index) {
        if (index >= this.data.length) {
            resize(index);
            this.pos = index;
        }
        return this.data[index];
    }

    public void put(int index, byte value) {
        if (index >= this.data.length) {
            resize(index);
            this.pos = index;
        }
        this.data[index] = value;
    }

    public void add(byte value) {
        if (++this.pos >= this.data.length) {
            resize(this.pos);
        }
        this.data[this.pos] = value;
    }

    public void trim() {
        if (this.pos != this.data.length - 1) {
            byte[] newData = new byte[this.pos + 1];
            System.arraycopy(this.data, 0, newData, 0, this.pos + 1);
            this.data = newData;
        }
    }

    public byte[] getData() {
        return this.data;
    }

    private void resize(int minSize) {
        if (minSize <= 2 * this.data.length) {
            minSize = 2 * this.data.length;
        }
        byte[] newData = new byte[minSize];
        System.arraycopy(this.data, 0, newData, 0, this.data.length);
        this.data = newData;
    }

    @Override
    public String toString() {
        return StringArrayUtils.toString(this.data);
    }
}
