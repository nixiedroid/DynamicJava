package com.nixiedroid.unsafe.type;

public final class Size {
    private final long size;

    public Size(long size) {
        if (size<0) throw new IllegalArgumentException("Size must be greater than 0");
        this.size = size;
    }

    public long size() {
        return this.size;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Size size1 = (Size) o;

        return this.size == size1.size;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(this.size);
    }

    @Override
    public String toString() {
        return "Size{" +
                "size=" + this.size +
                '}';
    }
}
