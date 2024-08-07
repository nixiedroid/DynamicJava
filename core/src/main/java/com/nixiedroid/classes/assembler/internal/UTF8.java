package com.nixiedroid.classes.assembler.internal;

@SuppressWarnings("MagicNumber")
public final class UTF8 {
    private UTF8() {
    }

    // This encoder is not quite correct.  It does not handle surrogate pairs.
    static byte[] encode(String str) {
        int len = str.length();
        byte[] res = new byte[utf8Length(str)];
        int utf8Idx = 0;
        try {
            for (int i = 0; i < len; i++) {
                int c = str.charAt(i) & 0xFFFF;
                if (c >= 0x0001 && c <= 0x007F) {
                    res[utf8Idx++] = (byte) c;
                } else if (c <= 0x07FF) {
                    res[utf8Idx++] = (byte) (0xC0 + (c >> 6));
                    res[utf8Idx++] = (byte) (0x80 + (c & 0x3F));
                } else {
                    res[utf8Idx++] = (byte) (0xE0 + (c >> 12));
                    res[utf8Idx++] = (byte) (0x80 + ((c >> 6) & 0x3F));
                    res[utf8Idx++] = (byte) (0x80 + (c & 0x3F));
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new InternalError
                    ("Bug in sun.reflect bootstrap UTF-8 encoder", e);
        }
        return res;
    }

    private static int utf8Length(String str) {
        int len = str.length();
        int utf8Len = 0;
        for (int i = 0; i < len; i++) {
            int c = str.charAt(i) & 0xFFFF;
            if (c >= 0x0001 && c <= 0x007F) {
                utf8Len += 1;
            } else if (c <= 0x07FF) {
                utf8Len += 2;
            } else {
                utf8Len += 3;
            }
        }
        return utf8Len;
    }
}
