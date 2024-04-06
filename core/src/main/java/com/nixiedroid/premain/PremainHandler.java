package com.nixiedroid.premain;

import java.lang.instrument.Instrumentation;

public interface PremainHandler {
    void handle(String args, Instrumentation inst);
}
