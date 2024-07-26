package com.nixiedroid.classes;

import com.nixiedroid.classes.assembler.ByteVector;
import com.nixiedroid.classes.assembler.ByteVectorFactory;
import com.nixiedroid.classes.assembler.ClassFileAssembler;
import com.nixiedroid.exceptions.Thrower;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.invoke.MethodHandles;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
class GenerateClassTest {
    public static byte[] generateHelloSupplier(String helloString){
        ByteVector vec = ByteVectorFactory.create();
        ClassFileAssembler asm = new ClassFileAssembler(vec);

        asm.emitMagicAndVersion();

        //ConstantPool
        asm.emitShort((short) 21); //CpCount
        asm.emitConstantPoolMethodref((short) 2, (short) 3); //1
        asm.emitConstantPoolClass((short) 4); //2
        asm.emitConstantPoolNameAndType((short) 5, (short) 6); //3
        asm.emitConstantPoolUTF8("java/lang/Object"); //4
        asm.emitConstantPoolUTF8("<init>"); //5
        asm.emitConstantPoolUTF8("()V"); //6
        asm.emitConstantPoolString((short) 8); //7
        asm.emitConstantPoolUTF8(helloString); //8
        asm.emitConstantPoolMethodref((short) 10, (short) 11);//9
        asm.emitConstantPoolClass((short) 12); //10
        asm.emitConstantPoolNameAndType((short) 13, (short) 14); //11
        asm.emitConstantPoolUTF8("com/nixiedroid/classes/A"); //12
        asm.emitConstantPoolUTF8("get"); //13
        asm.emitConstantPoolUTF8("()Ljava/lang/String;"); //14
        asm.emitConstantPoolClass((short) 16); //15
        asm.emitConstantPoolUTF8("java/util/function/Supplier"); //16
        asm.emitConstantPoolUTF8("Code"); //17
        asm.emitConstantPoolUTF8("()Ljava/lang/Object;"); //18
        asm.emitConstantPoolUTF8("Signature");//19
        asm.emitConstantPoolUTF8("Ljava/lang/Object;" +
                "Ljava/util/function/Supplier<Ljava/lang/String;>;");//20

        asm.emitShort((short) (Modifiers.PUBLIC | Modifiers.SUPER)); //access_flags
        asm.emitShort((short) 10); //this_class
        asm.emitShort((short) 2); //super_class
        asm.emitShort((short) 1); //interfaces_count
        //Interfaces
        asm.emitShort((short) 15);
        asm.emitShort((short) 0); //fields_count
        asm.emitShort((short) 3); //methods_count

        //Method 1
        asm.emitShort((short) (Modifiers.PUBLIC)); //access_flags
        asm.emitShort((short) 5); //name_index
        asm.emitShort((short) 6); //descriptor_index
        asm.emitShort((short) 1); //attributes_count

        //Method Code
        asm.emitShort((short) 17); //attribute_name_index
        asm.emitInt(17); //attribute_length
        asm.emitShort((short) 01);//max_stack
        asm.emitShort((short) 01);//max_locals
        asm.emitInt(5);//code_length
        asm.opc_aload_0();
        asm.opc_invokespecial((short) 1, 0, 1);
        asm.opc_return();
        asm.emitShort((short) 0); //exception_table_length
        asm.emitShort((short) 0); //attributes_count

        //Method 2
        asm.emitShort((short) (Modifiers.PUBLIC)); //access_flags
        asm.emitShort((short) 13); //name_index
        asm.emitShort((short) 14); //descriptor_index
        asm.emitShort((short) 1); //attributes_count

        //Method Code
        asm.emitShort((short) 17); //attribute_name_index
        asm.emitInt(15); //attribute_length
        asm.emitShort((short) 01);//max_stack
        asm.emitShort((short) 01);//max_locals
        asm.emitInt(3);//code_length
        asm.opc_ldc((byte) 7);
        asm.opc_areturn();
        asm.emitShort((short) 0); //exception_table_length
        asm.emitShort((short) 0); //attributes_count

        //Method 3
        asm.emitShort((short) (Modifiers.PUBLIC | Modifiers.SYNTHETIC | Modifiers.BRIDGE)); //access_flags
        asm.emitShort((short) 13); //name_index
        asm.emitShort((short) 18); //descriptor_index
        asm.emitShort((short) 1); //attributes_count

        //Method Code
        asm.emitShort((short) 17); //attribute_name_index
        asm.emitInt(17); //attribute_length
        asm.emitShort((short) 01);//max_stack
        asm.emitShort((short) 01);//max_locals
        asm.emitInt(5);//code_length
        asm.opc_aload_0();
        asm.opc_invokevirtual((short) 9,1,0);
        asm.opc_areturn();
        asm.emitShort((short) 0); //exception_table_length
        asm.emitShort((short) 0); //attributes_count

        asm.emitShort((short) 1);//attributes_count
        asm.emitShort((short) 19); //attribute_name_index
        asm.emitInt(2); //attribute_length
        asm.emitShort((short) 20);

        vec.trim();
        return asm.getData().getData();
    }

    public static byte[] generateHelloClass(String helloString) {
        ByteVector vec = ByteVectorFactory.create();
        ClassFileAssembler asm = new ClassFileAssembler(vec);
        asm.emitMagicAndVersion();

        //ConstantPool
        asm.emitShort((short) 14); //CpCount
        asm.emitConstantPoolMethodref((short) 2, (short) 3); //1
        asm.emitConstantPoolClass((short) 4); //2
        asm.emitConstantPoolNameAndType((short) 5, (short) 6); //3
        asm.emitConstantPoolUTF8("java/lang/Object"); //4
        asm.emitConstantPoolUTF8("<init>"); //5
        asm.emitConstantPoolUTF8("()V"); //6
        asm.emitConstantPoolString((short) 8); //7
        asm.emitConstantPoolUTF8(helloString); //8
        asm.emitConstantPoolClass((short) 10); //9
        asm.emitConstantPoolUTF8("com/nixiedroid/classes/B"); //10
        asm.emitConstantPoolUTF8("Code"); //11
        asm.emitConstantPoolUTF8("get"); //12
        asm.emitConstantPoolUTF8("()Ljava/lang/String;"); //13


        asm.emitShort((short) (Modifiers.PUBLIC | Modifiers.SUPER)); //access_flags
        asm.emitShort((short) 9); //this_class
        asm.emitShort((short) 2); //super_class
        asm.emitShort((short) 0); //interfaces_count
        asm.emitShort((short) 0); //fields_count
        asm.emitShort((short) 2); //methods_count

        //Method 1
        asm.emitShort((short) (Modifiers.PUBLIC)); //access_flags
        asm.emitShort((short) 5); //name_index
        asm.emitShort((short) 6); //descriptor_index
        asm.emitShort((short) 1); //attributes_count

        //Method Code
        asm.emitShort((short) 11); //attribute_name_index
        asm.emitInt(17); //attribute_length
        asm.emitShort((short) 01);//max_stack
        asm.emitShort((short) 01);//max_locals
        asm.emitInt(5);//code_length
        asm.opc_aload_0();
        asm.opc_invokespecial((short) 1, 0, 1);
        asm.opc_return();
        asm.emitShort((short) 0); //exception_table_length
        asm.emitShort((short) 0); //attributes_count

        //Method2
        asm.emitShort((short) (Modifiers.PUBLIC | Modifiers.STATIC)); //access_flags
        asm.emitShort((short) 12); //name_index
        asm.emitShort((short) 13); //descriptor_index
        asm.emitShort((short) 1); //attributes_count

        //Method Code
        asm.emitShort((short) 11); //attribute_name_index
        asm.emitInt(15); //attribute_length
        asm.emitShort((short) 01);//max_stack
        asm.emitShort((short) 00);//max_locals
        asm.emitInt(3);//code_length
        asm.opc_ldc((byte) 7);
        asm.opc_areturn();
        asm.emitShort((short) 0); //exception_table_length
        asm.emitShort((short) 0); //attributes_count

        asm.emitShort((short) 0);//attributes_count

        vec.trim();
        return asm.getData().getData();
    }

    @Test
    public void testClassGeneration() throws Throwable{
        try {
            final String hello = "Hello Java";
            byte[] clBytes = generateHelloSupplier(hello);
            MethodHandles.Lookup classLookup = MethodHandles.lookup();
            Class<?> clazz = classLookup.defineClass(clBytes);
            Supplier<String> supplier = (Supplier<String>) clazz.getDeclaredConstructor().newInstance();
            Assertions.assertEquals(hello,supplier.get());
        } catch (Throwable t){
            Assertions.fail("Should not have thrown any exception");
            Thrower.throwException(t);
        }
    }
}