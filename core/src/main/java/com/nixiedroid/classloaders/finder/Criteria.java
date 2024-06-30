package com.nixiedroid.classloaders.finder;

import com.nixiedroid.classloaders.parser.JavaClassParser;

@FunctionalInterface
public interface Criteria {
    boolean matches(JavaClassParser.ClassInfo info);
}
