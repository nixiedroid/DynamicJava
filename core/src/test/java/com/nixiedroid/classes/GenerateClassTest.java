package com.nixiedroid.classes;

import com.nixiedroid.classes.assembler.StringSupplierFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.invoke.MethodHandles;
import java.util.function.Supplier;

class GenerateClassTest {

    @Test
    public void testClassGeneration() {
        try {
            final String hello = "Hello Java";
            StringSupplierFactory f = new StringSupplierFactory(MethodHandles.lookup());
            Supplier<String> sup = f.getStringSupplier(hello);
            Supplier<String> sup2 = f.getStringSupplier(hello+"JAVA");
            Assertions.assertEquals(hello,sup.get());
            Assertions.assertEquals(hello+"JAVA",sup2.get());
        } catch (Throwable t){
            Assertions.fail("Should not have thrown any exception",t);
        }
    }
}