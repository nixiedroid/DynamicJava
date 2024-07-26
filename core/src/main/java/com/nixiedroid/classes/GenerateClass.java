package com.nixiedroid.classes;


import com.nixiedroid.classes.assembler.ByteVector;
import com.nixiedroid.classes.assembler.ByteVectorFactory;
import com.nixiedroid.classes.assembler.ClassFileAssembler;
import com.nixiedroid.reflection.Modules;

public class GenerateClass {
    public static void main(String[] args) throws Throwable {
        ByteVector vec = ByteVectorFactory.create();
        ClassFileAssembler asm = new ClassFileAssembler(vec);
        asm.emitMagicAndVersion();
        vec = asm.getData();
        System.out.println(vec);
        Modules.exportAllToAll();


    }
}
