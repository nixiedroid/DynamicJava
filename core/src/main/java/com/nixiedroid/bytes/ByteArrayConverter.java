package com.nixiedroid.bytes;

public interface ByteArrayConverter {
    int FF = 0xFF;
    int BYTE = 1;
    int SHORT = 2;
    int INTEGER = 4;
    int LONG = 8;
    int DOUBLE = 8;
    int FLOAT = 4;

    byte toByte(byte[] b, int start);

    short toShortB(byte[] b, int start);

    short toShortL(byte[] b, int start);

    int toIntegerB(byte[] b, int start);

    int toIntegerL(byte[] b, int start);

    long toLongB(byte[] b, int start);

    long toLongL(byte[] b, int start);

    float toFloatB(byte[] b, int start);

    float toFloatL(byte[] b, int start);

    double toDoubleB(byte[] b, int start);

    double toDoubleL(byte[] b, int start);

    void fromByte(byte[] b, int start, byte by);

    void fromShortB(byte[] b, int start, short s);

    void fromShortL(byte[] b, int start, short s);

    void fromIntegerB(byte[] b, int start, int i);

    void fromIntegerL(byte[] b, int start, int i);

    void fromLongB(byte[] b, int start, long l);

    void fromLongL(byte[] b, int start, long l);

}
