package com.nixiedroid.asm;

import com.nixiedroid.exceptions.Thrower;
import com.nixiedroid.reflection.Classes;
import com.nixiedroid.reflection.Modules;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.MethodVisitor ;
import static jdk.internal.org.objectweb.asm.Opcodes.*;

@SuppressWarnings("unused")
public class Asm {
    public Asm() throws ReflectiveOperationException {
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        classWriter.visit(
                V9, ACC_PUBLIC | ACC_SUPER,
                "com/nixiedroid/asm/HelloWorld", null, "java/lang/Object", null
        );
        addStandardConstructor(classWriter);
        addMainMethod(classWriter);
        classWriter.visitEnd();
        byte[] bytes =  classWriter.toByteArray();

        loadClass(bytes);
    }

    void loadClass(byte[] bytes)  {
        try {
            Class<?> clazz = Classes.defineClass(this.getClass(), bytes);

            Object o = clazz.getDeclaredConstructor().newInstance();

            clazz.getMethod("main", String[].class).invoke(o, new Object[] {new String[] {}});
        } catch (ReflectiveOperationException e){
            Thrower.throwException(e);
        }
    }

    void addMainMethod(ClassWriter cw) {
        MethodVisitor mv =
                cw.visitMethod(ACC_PUBLIC + ACC_STATIC,
                        "main", "([Ljava/lang/String;)V", null, null);
        mv.visitCode();
        mv.visitFieldInsn(GETSTATIC, "java/lang/System",
                "out", "Ljava/io/PrintStream;");
        mv.visitLdcInsn("Hello World!");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream",
                "println", "(Ljava/lang/String;)V", false);
        mv.visitInsn(RETURN);
        mv.visitMaxs(3, 3);
        mv.visitEnd();
    }

    void addStandardConstructor(ClassWriter cw) {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(
                INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        mv.visitInsn(RETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();
    }


    public static void main(String[] args) throws ReflectiveOperationException {
        Modules.exportPackage("java.base", "nixiedroid.dynamic.app", "jdk.internal.org.objectweb.asm");
        new Asm();
    }
}

