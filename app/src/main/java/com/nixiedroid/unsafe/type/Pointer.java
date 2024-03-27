package com.nixiedroid.unsafe.type;

public final class Pointer {
    private final long address;
    public static final Pointer ZERO_POINTER = new Pointer(0,false);

    private Pointer(long address, boolean validate){
        if (validate && address == 0) throw new NullPointerException("Pointer is ACTUALLY null");
        this.address = address;
    }
    public Pointer(long address) {
        this(address,true);
    }

    public static void validate(Pointer p) {
        if (p == null) throw new IllegalArgumentException("Pointer is null");
        if (p.address() == 0) throw new IllegalArgumentException("Pointer address is null");
    }

    public long address() {
        return address;
    }

    @Override
    public String toString() {
        return "Pointer{" +
                "address=" + address +
                '}';
    }
}