package com.nixiedroid.classblob;

public class ClassBytes  {

    byte[] classBytes;
    String name;

    public byte[] getClassBytes() {
        return this.classBytes;
    }

    public void setClassBytes(byte[] classBytes) {
        this.classBytes = classBytes;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ClassBytes(byte[] classBytes, String name)  {
        this.classBytes = classBytes;
        this.name = name;
    }

    public ClassBytes(byte[] classBytes) {
        this.classBytes = classBytes;
        this.name = "";
    }
}
