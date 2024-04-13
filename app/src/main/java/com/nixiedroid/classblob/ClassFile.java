package com.nixiedroid.classblob;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ClassFile  {

    final byte[] classBytes;

    public ClassFile(byte[] classBytes)  {
        this.classBytes = classBytes;
    }
}
