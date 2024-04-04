package com.nixiedroid.unsafe;

import com.nixiedroid.javaagent.Premain;
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
