package com.nixiedroid.classloaders.finder;

import com.nixiedroid.classes.JavaClassParser;

public class PackageCriteria implements Criteria{
    private final String packageName;

    public PackageCriteria(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public boolean matches(JavaClassParser.ClassInfo info) {
        return info.getPackageName().equals(this.packageName);
    }
}
