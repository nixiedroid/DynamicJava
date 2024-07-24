package com.nixiedroid.bytes.converter;

/**
 *   32-bit integer 0x12345678
 *  would be stored in bytes as<br>
 *  0x12 0x34 0x56 0x78 (Big endian)<br>
 *  0x78 0x56 0x34 0x12 (Little endian)<br>
 * Number is 43981 (0xABCD)<br>
 * In BE it will be stored as ABCD<br>
 * In LE as CDAB<br>
 * Endiannes means location of THE HIGHEST byte (12 in 1234)
 * <br><br>
 * _MSB__________LSB <br>
 * _0x12__34__56__78
 */

public enum Endianness {
    LITTLE_ENDIAN,
    BIG_ENDIAN
}

