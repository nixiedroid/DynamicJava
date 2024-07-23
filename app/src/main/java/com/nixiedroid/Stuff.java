package com.nixiedroid;

import com.nixiedroid.exceptions.Thrower;

public class Stuff {
    static {
        Thrower.throwExceptionAndDie("Dead");
    }

}
