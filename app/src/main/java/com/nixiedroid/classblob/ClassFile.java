package com.nixiedroid.classblob;

public class ClassFile  {

    final byte[] classBytes;
    String name;

    public ClassFile(byte[] classBytes,String name)  {
        this.classBytes = classBytes;
        this.name = name;
    }

}
