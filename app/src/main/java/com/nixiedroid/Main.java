package com.nixiedroid;

import com.nixiedroid.asm.ClassWriter;
import com.nixiedroid.asm.MethodVisitor;
import com.nixiedroid.exceptions.Thrower;
import com.nixiedroid.reflection.Classes;

import static com.nixiedroid.asm.Opcodes.*;

@SuppressWarnings("all")
public final class Main {


    Main() throws Throwable {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        cw.visit(V1_8, ACC_PUBLIC + ACC_SUPER, "com/nixiedroid/A",
                null, "java/lang/Object", null);
        addStandardConstructor(cw);
        addMainMethod(cw);
        cw.visitEnd();
        loadClass(cw.toByteArray());

        //new ClassReader();
    }




    void addStandardConstructor(ClassWriter cw) {
        MethodVisitor mv =
                cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(
                INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        mv.visitInsn(RETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();
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

    /**
     * @see jdk.internal.misc.MainMethodFinder;
     */
    public static void main(String[] args) {
        try {
            new Main();
        } catch (Throwable th) {
            Thrower.throwException(th);
        }
    }


    //com.sun.tools.javac.launcher <executes *.java files. Not *.class
    //java.util.jar jar reader

    public Object loadClass(byte[] data) {
        Object o = null;
        try {
            Class<?> clazz = Classes.defineClass(this.getClass(), data);
            o = clazz.getDeclaredConstructor().newInstance();
            clazz.getMethod("main",String[].class).invoke(null,new Object[]{new String[]{}});
        } catch (ReflectiveOperationException e) {
            Thrower.throwException(e);
        }
        return o;
    }
}



