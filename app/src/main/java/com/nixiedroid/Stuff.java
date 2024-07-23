package com.nixiedroid;

import com.nixiedroid.exceptions.Thrower;

public class Stuff {
    static {
        System.out.println("AAAAAAA");
        Thrower.throwExceptionAndDie("Dead");
    }

}
