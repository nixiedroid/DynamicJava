package com.nixiedroid.premain;

import com.nixiedroid.unsafe.type.Size;

public class SizeOf {
    public Size getSizeOf(Object o){
        try {
            return new Size((int)Premain.sizeOf(o));
        } catch (IllegalStateException e){
            return Size.INVALID_SIZE;
        }
    }
}
