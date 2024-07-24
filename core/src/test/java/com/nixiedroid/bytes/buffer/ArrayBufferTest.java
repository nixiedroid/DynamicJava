package com.nixiedroid.bytes.buffer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArrayBufferTest {

    @Test
    void testArBuf(){
        Buffer buffer = new ArrayBuffer(20);
        buffer.writeInteger(0x12128787);
        buffer.writeInteger(0x12128787);
        buffer.writeInteger(0x12128787);
        buffer.writeInteger(0x12128787);
        buffer.writeInteger(0x12128787);
        buffer.readByte();
    }

}