package com.nixiedroid.premain;

public class SizeOf {
    public long getSizeOf(Object o){
        try {
            return Premain.sizeOf(o);
        } catch (IllegalStateException e){
            return -1;
        }
    }
}
