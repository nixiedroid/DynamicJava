package com.nixiedroid.classes.assembler;

import com.nixiedroid.classes.Modifiers;
import com.nixiedroid.classes.assembler.internal.ByteVector;
import com.nixiedroid.classes.assembler.internal.ByteVectorFactory;
import com.nixiedroid.classes.assembler.internal.ClassFileAssembler;
import com.nixiedroid.reflection.Classes;

import java.lang.invoke.MethodHandles;
import java.util.Random;
import java.util.function.Supplier;

public class StringSupplierFactory {

    private ClassFileAssembler asm;

    private final MethodHandles.Lookup lookup;

    private String className;

    private final Random r = new Random();

    public StringSupplierFactory(MethodHandles.Lookup lookup) {
        this.lookup = lookup;
    }

    @SuppressWarnings("unchecked")
    public Supplier<String> getStringSupplier(String suppliedString) throws IllegalAccessException {
        this.asm = new ClassFileAssembler(ByteVectorFactory.create());
        this.className = this.lookup.lookupClass().getPackageName().replace(".","/") + "/A" + this.r.nextInt();
        byte[] classBytes = generateByteCode(suppliedString);
        Class<?> clazz = this.lookup.defineClass(classBytes);
        try {
            return  (Supplier<String>) clazz.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] generateByteCode(String suppliedString){
        emitClassMagicAndVersion();
        emitConstantPool(suppliedString);
        emitClassInfo();
        emitInterfaces();
        emitFields();
        emitMethods();
        ByteVector vec = this.asm.getData();
        vec.trim();
        return vec.getData();
    }

    private void emitClassMagicAndVersion() {
        this.asm.emitMagicAndVersion();
    }

    private void emitConstantPool(String suppliedString) {
        //ConstantPool
        this.asm.emitShort((short) 21); //CpCount
        this.asm.emitConstantPoolMethodref((short) 2, (short) 3); //1
        this.asm.emitConstantPoolClass((short) 4); //2
        this.asm.emitConstantPoolNameAndType((short) 5, (short) 6); //3
        this.asm.emitConstantPoolUTF8("java/lang/Object"); //4
        this.asm.emitConstantPoolUTF8("<init>"); //5
        this.asm.emitConstantPoolUTF8("()V"); //6
        this.asm.emitConstantPoolString((short) 8); //7
        this.asm.emitConstantPoolUTF8(suppliedString); //8
        this.asm.emitConstantPoolMethodref((short) 10, (short) 11);//9
        this.asm.emitConstantPoolClass((short) 12); //10
        this.asm.emitConstantPoolNameAndType((short) 13, (short) 14); //11
        this.asm.emitConstantPoolUTF8(this.className); //12
        this.asm.emitConstantPoolUTF8("get"); //13
        this.asm.emitConstantPoolUTF8("()Ljava/lang/String;"); //14
        this.asm.emitConstantPoolClass((short) 16); //15
        this.asm.emitConstantPoolUTF8("java/util/function/Supplier"); //16
        this.asm.emitConstantPoolUTF8("Code"); //17
        this.asm.emitConstantPoolUTF8("()Ljava/lang/Object;"); //18
        this.asm.emitConstantPoolUTF8("Signature");//19
        this.asm.emitConstantPoolUTF8("Ljava/lang/Object;" +
                "Ljava/util/function/Supplier<Ljava/lang/String;>;");//20

    }

    private void emitClassInfo() {
        this.asm.emitShort((short) (Modifiers.PUBLIC | Modifiers.SUPER)); //access_flags
        this.asm.emitShort((short) 10); //this_class
        this.asm.emitShort((short) 2); //super_class
    }

    private void emitInterfaces() {
        this.asm.emitShort((short) 1); //interfaces_count
        //Interfaces
        this.asm.emitShort((short) 15);
    }

    private void emitFields(){
        this.asm.emitShort((short) 0); //fields_count
    }

    private void emitMethods(){
        this.asm.emitShort((short) 3); //methods_count

        //Method 1
        this.asm.emitShort((short) (Modifiers.PUBLIC)); //access_flags
        this.asm.emitShort((short) 5); //name_index
        this.asm.emitShort((short) 6); //descriptor_index
        this.asm.emitShort((short) 1); //attributes_count

        //Method Code
        this.asm.emitShort((short) 17); //attribute_name_index
        this.asm.emitInt(17); //attribute_length
        this.asm.emitShort((short) 01);//max_stack
        this.asm.emitShort((short) 01);//max_locals
        this.asm.emitInt(5);//code_length
        this.asm.opc_aload_0();
        this.asm.opc_invokespecial((short) 1, 0, 1);
        this.asm.opc_return();
        this.asm.emitShort((short) 0); //exception_table_length
        this.asm.emitShort((short) 0); //attributes_count

        //Method 2
        this.asm.emitShort((short) (Modifiers.PUBLIC)); //access_flags
        this.asm.emitShort((short) 13); //name_index
        this.asm.emitShort((short) 14); //descriptor_index
        this.asm.emitShort((short) 1); //attributes_count

        //Method Code
        this.asm.emitShort((short) 17); //attribute_name_index
        this.asm.emitInt(15); //attribute_length
        this.asm.emitShort((short) 01);//max_stack
        this.asm.emitShort((short) 01);//max_locals
        this.asm.emitInt(3);//code_length
        this.asm.opc_ldc((byte) 7);
        this.asm.opc_areturn();
        this.asm.emitShort((short) 0); //exception_table_length
        this.asm.emitShort((short) 0); //attributes_count

        //Method 3
        this.asm.emitShort((short) (Modifiers.PUBLIC | Modifiers.SYNTHETIC | Modifiers.BRIDGE)); //access_flags
        this.asm.emitShort((short) 13); //name_index
        this.asm.emitShort((short) 18); //descriptor_index
        this.asm.emitShort((short) 1); //attributes_count

        //Method Code
        this.asm.emitShort((short) 17); //attribute_name_index
        this.asm.emitInt(17); //attribute_length
        this.asm.emitShort((short) 01);//max_stack
        this.asm.emitShort((short) 01);//max_locals
        this.asm.emitInt(5);//code_length
        this.asm.opc_aload_0();
        this.asm.opc_invokevirtual((short) 9,1,0);
        this.asm.opc_areturn();
        this.asm.emitShort((short) 0); //exception_table_length
        this.asm.emitShort((short) 0); //attributes_count

        this.asm.emitShort((short) 1);//attributes_count
        this.asm.emitShort((short) 19); //attribute_name_index
        this.asm.emitInt(2); //attribute_length
        this.asm.emitShort((short) 20);
    }
}
