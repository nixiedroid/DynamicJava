package com.nixiedroid.classloaders.finder;

import com.nixiedroid.classes.JavaClassParser;

@FunctionalInterface
public interface Criteria {
    boolean matches(JavaClassParser.ClassInfo info);
}
