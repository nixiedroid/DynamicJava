// ASM: a very small and fast Java bytecode manipulation framework
// Copyright (c) 2000-2011 INRIA, France Telecom
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
// 1. Redistributions of source code must retain the above copyright
//    notice, this list of conditions and the following disclaimer.
// 2. Redistributions in binary form must reproduce the above copyright
//    notice, this list of conditions and the following disclaimer in the
//    documentation and/or other materials provided with the distribution.
// 3. Neither the name of the copyright holders nor the names of its
//    contributors may be used to endorse or promote products derived from
//    this software without specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
// THE POSSIBILITY OF SUCH DAMAGE.
package com.nixiedroid.asm;

/**
 * A {@link ClassVisitor} that generates a corresponding ClassFile structure, as defined in the Java
 * Virtual Machine Specification (JVMS). It can be used alone, to generate a Java class "from
 * scratch", or with one or more {@link ClassReader} and adapter {@link ClassVisitor} to generate a
 * modified class from one or more existing Java classes.
 *
 * @author Eric Bruneton
 * @see <a href="https://docs.oracle.com/javase/specs/jvms/se9/html/jvms-4.html">JVMS 4</a>
 */
public class ClassWriter extends ClassVisitor {

    /**
     * A flag to automatically compute the maximum stack size and the maximum number of local
     * variables of methods. If this flag is set, then the arguments of the {@link
     * MethodVisitor#visitMaxs} method of the {@link MethodVisitor} returned by the {@link
     * #visitMethod} method will be ignored, and computed automatically from the signature and the
     * bytecode of each method.
     *
     * <p><b>Note:</b> for classes whose version is {@link Opcodes#V1_7} of more, this option requires
     * valid stack map frames. The maximum stack size is then computed from these frames, and from the
     * bytecode instructions in between. If stack map frames are not present or must be recomputed,
     * used {@link #COMPUTE_FRAMES} instead.
     *
     * @see #ClassWriter(int)
     */
    public static final int COMPUTE_MAXS = 1;

    /**
     * A flag to automatically compute the stack map frames of methods from scratch. If this flag is
     * set, then the calls to the {@link MethodVisitor#visitFrame} method are ignored, and the stack
     * map frames are recomputed from the methods bytecode. The arguments of the {@link
     * MethodVisitor#visitMaxs} method are also ignored and recomputed from the bytecode. In other
     * words, {@link #COMPUTE_FRAMES} implies {@link #COMPUTE_MAXS}.
     *
     * @see #ClassWriter(int)
     */
    public static final int COMPUTE_FRAMES = 2;

    /**
     * The flags passed to the constructor. Must be zero or more of {@link #COMPUTE_MAXS} and {@link
     * #COMPUTE_FRAMES}.
     */
    private final int flags;

    // Note: fields are ordered as in the ClassFile structure, and those related to attributes are
    // ordered as in Section 4.7 of the JVMS.
    /**
     * The symbol table for this class (contains the constant_pool and the BootstrapMethods).
     */
    private final SymbolTable symbolTable;
    /**
     * The minor_version and major_version fields of the JVMS ClassFile structure. minor_version is
     * stored in the 16 most significant bits, and major_version in the 16 least significant bits.
     */
    private int version;
    /**
     * The access_flags field of the JVMS ClassFile structure. This field can contain ASM specific
     * access flags, such as {@link Opcodes#ACC_DEPRECATED} or {@link Opcodes#ACC_RECORD}, which are
     * removed when generating the ClassFile structure.
     */
    private int accessFlags;

    /**
     * The this_class field of the JVMS ClassFile structure.
     */
    private int thisClass;

    /**
     * The super_class field of the JVMS ClassFile structure.
     */
    private int superClass;

    /**
     * The interface_count field of the JVMS ClassFile structure.
     */
    private int interfaceCount;

    /**
     * The 'interfaces' array of the JVMS ClassFile structure.
     */
    private int[] interfaces;

    /**
     * The fields of this class, stored in a linked list of {@link FieldWriter} linked via their
     * {@link FieldWriter#fv} field. This field stores the first element of this list.
     */
    private FieldWriter firstField;

    /**
     * The fields of this class, stored in a linked list of {@link FieldWriter} linked via their
     * {@link FieldWriter#fv} field. This field stores the last element of this list.
     */
    private FieldWriter lastField;

    /**
     * The methods of this class, stored in a linked list of {@link MethodWriter} linked via their
     * {@link MethodWriter#mv} field. This field stores the first element of this list.
     */
    private MethodWriter firstMethod;

    /**
     * The methods of this class, stored in a linked list of {@link MethodWriter} linked via their
     * {@link MethodWriter#mv} field. This field stores the last element of this list.
     */
    private MethodWriter lastMethod;

    /**
     * The number_of_classes field of the InnerClasses attribute, or 0.
     */
    private int numberOfInnerClasses;

    /**
     * The 'classes' array of the InnerClasses attribute, or {@literal null}.
     */
    private ByteVector innerClasses;

    /**
     * The class_index field of the EnclosingMethod attribute, or 0.
     */
    private int enclosingClassIndex;

    /**
     * The method_index field of the EnclosingMethod attribute.
     */
    private int enclosingMethodIndex;

    /**
     * The signature_index field of the Signature attribute, or 0.
     */
    private int signatureIndex;

    /**
     * The source_file_index field of the SourceFile attribute, or 0.
     */
    private int sourceFileIndex;

    /**
     * The debug_extension field of the SourceDebugExtension attribute, or {@literal null}.
     */
    private ByteVector debugExtension;

    /**
     * The last runtime visible annotation of this class. The previous ones can be accessed with the
     * {@link AnnotationWriter#previousAnnotation} field. May be {@literal null}.
     */
    private AnnotationWriter lastRuntimeVisibleAnnotation;

    /**
     * The last runtime invisible annotation of this class. The previous ones can be accessed with the
     * {@link AnnotationWriter#previousAnnotation} field. May be {@literal null}.
     */
    private AnnotationWriter lastRuntimeInvisibleAnnotation;

    /**
     * The last runtime visible type annotation of this class. The previous ones can be accessed with
     * the {@link AnnotationWriter#previousAnnotation} field. May be {@literal null}.
     */
    private AnnotationWriter lastRuntimeVisibleTypeAnnotation;

    /**
     * The last runtime invisible type annotation of this class. The previous ones can be accessed
     * with the {@link AnnotationWriter#previousAnnotation} field. May be {@literal null}.
     */
    private AnnotationWriter lastRuntimeInvisibleTypeAnnotation;

    /**
     * The Module attribute of this class, or {@literal null}.
     */
    private ModuleWriter moduleWriter;

    /**
     * The host_class_index field of the NestHost attribute, or 0.
     */
    private int nestHostClassIndex;

    /**
     * The number_of_classes field of the NestMembers attribute, or 0.
     */
    private int numberOfNestMemberClasses;

    /**
     * The 'classes' array of the NestMembers attribute, or {@literal null}.
     */
    private ByteVector nestMemberClasses;

    /**
     * The number_of_classes field of the PermittedSubclasses attribute, or 0.
     */
    private int numberOfPermittedSubclasses;

    /**
     * The 'classes' array of the PermittedSubclasses attribute, or {@literal null}.
     */
    private ByteVector permittedSubclasses;

    /**
     * The record components of this class, stored in a linked list of {@link RecordComponentWriter}
     * linked via their {@link RecordComponentWriter#delegate} field. This field stores the first
     * element of this list.
     */
    private RecordComponentWriter firstRecordComponent;

    /**
     * The record components of this class, stored in a linked list of {@link RecordComponentWriter}
     * linked via their {@link RecordComponentWriter#delegate} field. This field stores the last
     * element of this list.
     */
    private RecordComponentWriter lastRecordComponent;

    /**
     * The first non standard attribute of this class. The next ones can be accessed with the {@link
     * Attribute#nextAttribute} field. May be {@literal null}.
     *
     * <p><b>WARNING</b>: this list stores the attributes in the <i>reverse</i> order of their visit.
     * firstAttribute is actually the last attribute visited in {@link #visitAttribute}. The {@link
     * #toByteArray} method writes the attributes in the order defined by this list, i.e. in the
     * reverse order specified by the user.
     */
    private Attribute firstAttribute;

    /**
     * Indicates what must be automatically computed in {@link MethodWriter}. Must be one of {@link
     * MethodWriter#COMPUTE_NOTHING}, {@link MethodWriter#COMPUTE_MAX_STACK_AND_LOCAL}, {@link
     * MethodWriter#COMPUTE_MAX_STACK_AND_LOCAL_FROM_FRAMES}, {@link
     * MethodWriter#COMPUTE_INSERTED_FRAMES}, or {@link MethodWriter#COMPUTE_ALL_FRAMES}.
     */
    private int compute;

    // -----------------------------------------------------------------------------------------------
    // Constructor
    // -----------------------------------------------------------------------------------------------

    /**
     * Constructs a new {@link ClassWriter} object.
     *
     * @param flags option flags that can be used to modify the default behavior of this class. Must
     *              be zero or more of {@link #COMPUTE_MAXS} and {@link #COMPUTE_FRAMES}.
     */
    public ClassWriter(final int flags) {
        this(null, flags);
    }

    /**
     * Constructs a new {@link ClassWriter} object and enables optimizations for "mostly add" bytecode
     * transformations. These optimizations are the following:
     *
     * <ul>
     *   <li>The constant pool and bootstrap methods from the original class are copied as is in the
     *       new class, which saves time. New constant pool entries and new bootstrap methods will be
     *       added at the end if necessary, but unused constant pool entries or bootstrap methods
     *       <i>won't be removed</i>.
     *   <li>Methods that are not transformed are copied as is in the new class, directly from the
     *       original class bytecode (i.e. without emitting visit events for all the method
     *       instructions), which saves a <i>lot</i> of time. Untransformed methods are detected by
     *       the fact that the {@link ClassReader} receives {@link MethodVisitor} objects that come
     *       from a {@link ClassWriter} (and not from any other {@link ClassVisitor} instance).
     * </ul>
     *
     * @param classReader the {@link ClassReader} used to read the original class. It will be used to
     *                    copy the entire constant pool and bootstrap methods from the original class and also to
     *                    copy other fragments of original bytecode where applicable.
     * @param flags       option flags that can be used to modify the default behavior of this class. Must
     *                    be zero or more of {@link #COMPUTE_MAXS} and {@link #COMPUTE_FRAMES}. <i>These option flags
     *                    do not affect methods that are copied as is in the new class. This means that neither the
     *                    maximum stack size nor the stack frames will be computed for these methods</i>.
     */
    public ClassWriter(final ClassReader classReader, final int flags) {
        super(/* latest api = */ Opcodes.ASM9);
        this.flags = flags;
        this.symbolTable = classReader == null ? new SymbolTable(this) : new SymbolTable(this, classReader);
        if ((flags & COMPUTE_FRAMES) != 0) {
            this.compute = MethodWriter.COMPUTE_ALL_FRAMES;
        } else if ((flags & COMPUTE_MAXS) != 0) {
            this.compute = MethodWriter.COMPUTE_MAX_STACK_AND_LOCAL;
        } else {
            this.compute = MethodWriter.COMPUTE_NOTHING;
        }
    }

    // -----------------------------------------------------------------------------------------------
    // Accessors
    // -----------------------------------------------------------------------------------------------

    /**
     * Returns true if all the given flags were passed to the constructor.
     *
     * @param flags some option flags. Must be zero or more of {@link #COMPUTE_MAXS} and {@link
     *              #COMPUTE_FRAMES}.
     * @return true if all the given flags, or more, were passed to the constructor.
     */
    public boolean hasFlags(final int flags) {
        return (this.flags & flags) == flags;
    }

    // -----------------------------------------------------------------------------------------------
    // Implementation of the ClassVisitor abstract class
    // -----------------------------------------------------------------------------------------------

    @Override
    public final void visit(
            final int version,
            final int access,
            final String name,
            final String signature,
            final String superName,
            final String[] interfaces) {
        this.version = version;
        this.accessFlags = access;
        this.thisClass = this.symbolTable.setMajorVersionAndClassName(version & 0xFFFF, name);
        if (signature != null) {
            this.signatureIndex = this.symbolTable.addConstantUtf8(signature);
        }
        this.superClass = superName == null ? 0 : this.symbolTable.addConstantClass(superName).index;
        if (interfaces != null && interfaces.length > 0) {
            this.interfaceCount = interfaces.length;
            this.interfaces = new int[this.interfaceCount];
            for (int i = 0; i < this.interfaceCount; ++i) {
                this.interfaces[i] = this.symbolTable.addConstantClass(interfaces[i]).index;
            }
        }
        if (this.compute == MethodWriter.COMPUTE_MAX_STACK_AND_LOCAL && (version & 0xFFFF) >= Opcodes.V1_7) {
            this.compute = MethodWriter.COMPUTE_MAX_STACK_AND_LOCAL_FROM_FRAMES;
        }
    }

    @Override
    public final void visitSource(final String file, final String debug) {
        if (file != null) {
            this.sourceFileIndex = this.symbolTable.addConstantUtf8(file);
        }
        if (debug != null) {
            this.debugExtension = new ByteVector().encodeUtf8(debug, 0, Integer.MAX_VALUE);
        }
    }

    @Override
    public final ModuleVisitor visitModule(
            final String name, final int access, final String version) {
        return this.moduleWriter =
                new ModuleWriter(
                        this.symbolTable,
                        this.symbolTable.addConstantModule(name).index,
                        access,
                        version == null ? 0 : this.symbolTable.addConstantUtf8(version));
    }

    @Override
    public final void visitNestHost(final String nestHost) {
        this.nestHostClassIndex = this.symbolTable.addConstantClass(nestHost).index;
    }

    @Override
    public final void visitOuterClass(
            final String owner, final String name, final String descriptor) {
        this.enclosingClassIndex = this.symbolTable.addConstantClass(owner).index;
        if (name != null && descriptor != null) {
            this.enclosingMethodIndex = this.symbolTable.addConstantNameAndType(name, descriptor);
        }
    }

    @Override
    public final AnnotationVisitor visitAnnotation(final String descriptor, final boolean visible) {
        if (visible) {
            return this.lastRuntimeVisibleAnnotation =
                    AnnotationWriter.create(this.symbolTable, descriptor, this.lastRuntimeVisibleAnnotation);
        } else {
            return this.lastRuntimeInvisibleAnnotation =
                    AnnotationWriter.create(this.symbolTable, descriptor, this.lastRuntimeInvisibleAnnotation);
        }
    }

    @Override
    public final AnnotationVisitor visitTypeAnnotation(
            final int typeRef, final TypePath typePath, final String descriptor, final boolean visible) {
        if (visible) {
            return this.lastRuntimeVisibleTypeAnnotation =
                    AnnotationWriter.create(
                            this.symbolTable, typeRef, typePath, descriptor, this.lastRuntimeVisibleTypeAnnotation);
        } else {
            return this.lastRuntimeInvisibleTypeAnnotation =
                    AnnotationWriter.create(
                            this.symbolTable, typeRef, typePath, descriptor, this.lastRuntimeInvisibleTypeAnnotation);
        }
    }

    @Override
    public final void visitAttribute(final Attribute attribute) {
        // Store the attributes in the <i>reverse</i> order of their visit by this method.
        attribute.nextAttribute = this.firstAttribute;
        this.firstAttribute = attribute;
    }

    @Override
    public final void visitNestMember(final String nestMember) {
        if (this.nestMemberClasses == null) {
            this.nestMemberClasses = new ByteVector();
        }
        ++this.numberOfNestMemberClasses;
        this.nestMemberClasses.putShort(this.symbolTable.addConstantClass(nestMember).index);
    }

    @Override
    public final void visitPermittedSubclass(final String permittedSubclass) {
        if (this.permittedSubclasses == null) {
            this.permittedSubclasses = new ByteVector();
        }
        ++this.numberOfPermittedSubclasses;
        this.permittedSubclasses.putShort(this.symbolTable.addConstantClass(permittedSubclass).index);
    }

    @Override
    public final void visitInnerClass(
            final String name, final String outerName, final String innerName, final int access) {
        if (this.innerClasses == null) {
            this.innerClasses = new ByteVector();
        }
        // Section 4.7.6 of the JVMS states "Every CONSTANT_Class_info entry in the constant_pool table
        // which represents a class or interface C that is not a package member must have exactly one
        // corresponding entry in the classes array". To avoid duplicates we keep track in the info
        // field of the Symbol of each CONSTANT_Class_info entry C whether an inner class entry has
        // already been added for C. If so, we store the index of this inner class entry (plus one) in
        // the info field. This trick allows duplicate detection in O(1) time.
        Symbol nameSymbol = this.symbolTable.addConstantClass(name);
        if (nameSymbol.info == 0) {
            ++this.numberOfInnerClasses;
            this.innerClasses.putShort(nameSymbol.index);
            this.innerClasses.putShort(outerName == null ? 0 : this.symbolTable.addConstantClass(outerName).index);
            this.innerClasses.putShort(innerName == null ? 0 : this.symbolTable.addConstantUtf8(innerName));
            this.innerClasses.putShort(access);
            nameSymbol.info = this.numberOfInnerClasses;
        }
        // Else, compare the inner classes entry nameSymbol.info - 1 with the arguments of this method
        // and throw an exception if there is a difference?
    }

    @Override
    public final RecordComponentVisitor visitRecordComponent(
            final String name, final String descriptor, final String signature) {
        RecordComponentWriter recordComponentWriter =
                new RecordComponentWriter(this.symbolTable, name, descriptor, signature);
        if (this.firstRecordComponent == null) {
            this.firstRecordComponent = recordComponentWriter;
        } else {
            this.lastRecordComponent.delegate = recordComponentWriter;
        }
        return this.lastRecordComponent = recordComponentWriter;
    }

    @Override
    public final FieldVisitor visitField(
            final int access,
            final String name,
            final String descriptor,
            final String signature,
            final Object value) {
        FieldWriter fieldWriter =
                new FieldWriter(this.symbolTable, access, name, descriptor, signature, value);
        if (this.firstField == null) {
            this.firstField = fieldWriter;
        } else {
            this.lastField.fv = fieldWriter;
        }
        return this.lastField = fieldWriter;
    }

    @Override
    public final MethodVisitor visitMethod(
            final int access,
            final String name,
            final String descriptor,
            final String signature,
            final String[] exceptions) {
        MethodWriter methodWriter =
                new MethodWriter(this.symbolTable, access, name, descriptor, signature, exceptions, this.compute);
        if (this.firstMethod == null) {
            this.firstMethod = methodWriter;
        } else {
            this.lastMethod.mv = methodWriter;
        }
        return this.lastMethod = methodWriter;
    }

    @Override
    public final void visitEnd() {
        // Nothing to do.
    }

    // -----------------------------------------------------------------------------------------------
    // Other public methods
    // -----------------------------------------------------------------------------------------------

    /**
     * Returns the content of the class file that was built by this ClassWriter.
     *
     * @return the binary content of the JVMS ClassFile structure that was built by this ClassWriter.
     * @throws ClassTooLargeException  if the constant pool of the class is too large.
     * @throws MethodTooLargeException if the Code attribute of a method is too large.
     */
    public byte[] toByteArray() {
        // First step: compute the size in bytes of the ClassFile structure.
        // The magic field uses 4 bytes, 10 mandatory fields (minor_version, major_version,
        // constant_pool_count, access_flags, this_class, super_class, interfaces_count, fields_count,
        // methods_count and attributes_count) use 2 bytes each, and each interface uses 2 bytes too.
        int size = 24 + 2 * this.interfaceCount;
        int fieldsCount = 0;
        FieldWriter fieldWriter = this.firstField;
        while (fieldWriter != null) {
            ++fieldsCount;
            size += fieldWriter.computeFieldInfoSize();
            fieldWriter = (FieldWriter) fieldWriter.fv;
        }
        int methodsCount = 0;
        MethodWriter methodWriter = this.firstMethod;
        while (methodWriter != null) {
            ++methodsCount;
            size += methodWriter.computeMethodInfoSize();
            methodWriter = (MethodWriter) methodWriter.mv;
        }

        // For ease of reference, we use here the same attribute order as in Section 4.7 of the JVMS.
        int attributesCount = 0;
        if (this.innerClasses != null) {
            ++attributesCount;
            size += 8 + this.innerClasses.length;
            this.symbolTable.addConstantUtf8(Constants.INNER_CLASSES);
        }
        if (this.enclosingClassIndex != 0) {
            ++attributesCount;
            size += 10;
            this.symbolTable.addConstantUtf8(Constants.ENCLOSING_METHOD);
        }
        if ((this.accessFlags & Opcodes.ACC_SYNTHETIC) != 0 && (this.version & 0xFFFF) < Opcodes.V1_5) {
            ++attributesCount;
            size += 6;
            this.symbolTable.addConstantUtf8(Constants.SYNTHETIC);
        }
        if (this.signatureIndex != 0) {
            ++attributesCount;
            size += 8;
            this.symbolTable.addConstantUtf8(Constants.SIGNATURE);
        }
        if (this.sourceFileIndex != 0) {
            ++attributesCount;
            size += 8;
            this.symbolTable.addConstantUtf8(Constants.SOURCE_FILE);
        }
        if (this.debugExtension != null) {
            ++attributesCount;
            size += 6 + this.debugExtension.length;
            this.symbolTable.addConstantUtf8(Constants.SOURCE_DEBUG_EXTENSION);
        }
        if ((this.accessFlags & Opcodes.ACC_DEPRECATED) != 0) {
            ++attributesCount;
            size += 6;
            this.symbolTable.addConstantUtf8(Constants.DEPRECATED);
        }
        if (this.lastRuntimeVisibleAnnotation != null) {
            ++attributesCount;
            size +=
                    this.lastRuntimeVisibleAnnotation.computeAnnotationsSize(
                            Constants.RUNTIME_VISIBLE_ANNOTATIONS);
        }
        if (this.lastRuntimeInvisibleAnnotation != null) {
            ++attributesCount;
            size +=
                    this.lastRuntimeInvisibleAnnotation.computeAnnotationsSize(
                            Constants.RUNTIME_INVISIBLE_ANNOTATIONS);
        }
        if (this.lastRuntimeVisibleTypeAnnotation != null) {
            ++attributesCount;
            size +=
                    this.lastRuntimeVisibleTypeAnnotation.computeAnnotationsSize(
                            Constants.RUNTIME_VISIBLE_TYPE_ANNOTATIONS);
        }
        if (this.lastRuntimeInvisibleTypeAnnotation != null) {
            ++attributesCount;
            size +=
                    this.lastRuntimeInvisibleTypeAnnotation.computeAnnotationsSize(
                            Constants.RUNTIME_INVISIBLE_TYPE_ANNOTATIONS);
        }
        if (this.symbolTable.computeBootstrapMethodsSize() > 0) {
            ++attributesCount;
            size += this.symbolTable.computeBootstrapMethodsSize();
        }
        if (this.moduleWriter != null) {
            attributesCount += this.moduleWriter.getAttributeCount();
            size += this.moduleWriter.computeAttributesSize();
        }
        if (this.nestHostClassIndex != 0) {
            ++attributesCount;
            size += 8;
            this.symbolTable.addConstantUtf8(Constants.NEST_HOST);
        }
        if (this.nestMemberClasses != null) {
            ++attributesCount;
            size += 8 + this.nestMemberClasses.length;
            this.symbolTable.addConstantUtf8(Constants.NEST_MEMBERS);
        }
        if (this.permittedSubclasses != null) {
            ++attributesCount;
            size += 8 + this.permittedSubclasses.length;
            this.symbolTable.addConstantUtf8(Constants.PERMITTED_SUBCLASSES);
        }
        int recordComponentCount = 0;
        int recordSize = 0;
        if ((this.accessFlags & Opcodes.ACC_RECORD) != 0 || this.firstRecordComponent != null) {
            RecordComponentWriter recordComponentWriter = this.firstRecordComponent;
            while (recordComponentWriter != null) {
                ++recordComponentCount;
                recordSize += recordComponentWriter.computeRecordComponentInfoSize();
                recordComponentWriter = (RecordComponentWriter) recordComponentWriter.delegate;
            }
            ++attributesCount;
            size += 8 + recordSize;
            this.symbolTable.addConstantUtf8(Constants.RECORD);
        }
        if (this.firstAttribute != null) {
            attributesCount += this.firstAttribute.getAttributeCount();
            size += this.firstAttribute.computeAttributesSize(this.symbolTable);
        }
        // IMPORTANT: this must be the last part of the ClassFile size computation, because the previous
        // statements can add attribute names to the constant pool, thereby changing its size!
        size += this.symbolTable.getConstantPoolLength();
        int constantPoolCount = this.symbolTable.getConstantPoolCount();
        if (constantPoolCount > 0xFFFF) {
            throw new ClassTooLargeException(this.symbolTable.getClassName(), constantPoolCount);
        }

        // Second step: allocate a ByteVector of the correct size (in order to avoid any array copy in
        // dynamic resizes) and fill it with the ClassFile content.
        ByteVector result = new ByteVector(size);
        result.putInt(0xCAFEBABE).putInt(this.version);
        this.symbolTable.putConstantPool(result);
        int mask = (this.version & 0xFFFF) < Opcodes.V1_5 ? Opcodes.ACC_SYNTHETIC : 0;
        result.putShort(this.accessFlags & ~mask).putShort(this.thisClass).putShort(this.superClass);
        result.putShort(this.interfaceCount);
        for (int i = 0; i < this.interfaceCount; ++i) {
            result.putShort(this.interfaces[i]);
        }
        result.putShort(fieldsCount);
        fieldWriter = this.firstField;
        while (fieldWriter != null) {
            fieldWriter.putFieldInfo(result);
            fieldWriter = (FieldWriter) fieldWriter.fv;
        }
        result.putShort(methodsCount);
        boolean hasFrames = false;
        boolean hasAsmInstructions = false;
        methodWriter = this.firstMethod;
        while (methodWriter != null) {
            hasFrames |= methodWriter.hasFrames();
            hasAsmInstructions |= methodWriter.hasAsmInstructions();
            methodWriter.putMethodInfo(result);
            methodWriter = (MethodWriter) methodWriter.mv;
        }
        // For ease of reference, we use here the same attribute order as in Section 4.7 of the JVMS.
        result.putShort(attributesCount);
        if (this.innerClasses != null) {
            result
                    .putShort(this.symbolTable.addConstantUtf8(Constants.INNER_CLASSES))
                    .putInt(this.innerClasses.length + 2)
                    .putShort(this.numberOfInnerClasses)
                    .putByteArray(this.innerClasses.data, 0, this.innerClasses.length);
        }
        if (this.enclosingClassIndex != 0) {
            result
                    .putShort(this.symbolTable.addConstantUtf8(Constants.ENCLOSING_METHOD))
                    .putInt(4)
                    .putShort(this.enclosingClassIndex)
                    .putShort(this.enclosingMethodIndex);
        }
        if ((this.accessFlags & Opcodes.ACC_SYNTHETIC) != 0 && (this.version & 0xFFFF) < Opcodes.V1_5) {
            result.putShort(this.symbolTable.addConstantUtf8(Constants.SYNTHETIC)).putInt(0);
        }
        if (this.signatureIndex != 0) {
            result
                    .putShort(this.symbolTable.addConstantUtf8(Constants.SIGNATURE))
                    .putInt(2)
                    .putShort(this.signatureIndex);
        }
        if (this.sourceFileIndex != 0) {
            result
                    .putShort(this.symbolTable.addConstantUtf8(Constants.SOURCE_FILE))
                    .putInt(2)
                    .putShort(this.sourceFileIndex);
        }
        if (this.debugExtension != null) {
            int length = this.debugExtension.length;
            result
                    .putShort(this.symbolTable.addConstantUtf8(Constants.SOURCE_DEBUG_EXTENSION))
                    .putInt(length)
                    .putByteArray(this.debugExtension.data, 0, length);
        }
        if ((this.accessFlags & Opcodes.ACC_DEPRECATED) != 0) {
            result.putShort(this.symbolTable.addConstantUtf8(Constants.DEPRECATED)).putInt(0);
        }
        AnnotationWriter.putAnnotations(
                this.symbolTable,
                this.lastRuntimeVisibleAnnotation,
                this.lastRuntimeInvisibleAnnotation,
                this.lastRuntimeVisibleTypeAnnotation,
                this.lastRuntimeInvisibleTypeAnnotation,
                result);
        this.symbolTable.putBootstrapMethods(result);
        if (this.moduleWriter != null) {
            this.moduleWriter.putAttributes(result);
        }
        if (this.nestHostClassIndex != 0) {
            result
                    .putShort(this.symbolTable.addConstantUtf8(Constants.NEST_HOST))
                    .putInt(2)
                    .putShort(this.nestHostClassIndex);
        }
        if (this.nestMemberClasses != null) {
            result
                    .putShort(this.symbolTable.addConstantUtf8(Constants.NEST_MEMBERS))
                    .putInt(this.nestMemberClasses.length + 2)
                    .putShort(this.numberOfNestMemberClasses)
                    .putByteArray(this.nestMemberClasses.data, 0, this.nestMemberClasses.length);
        }
        if (this.permittedSubclasses != null) {
            result
                    .putShort(this.symbolTable.addConstantUtf8(Constants.PERMITTED_SUBCLASSES))
                    .putInt(this.permittedSubclasses.length + 2)
                    .putShort(this.numberOfPermittedSubclasses)
                    .putByteArray(this.permittedSubclasses.data, 0, this.permittedSubclasses.length);
        }
        if ((this.accessFlags & Opcodes.ACC_RECORD) != 0 || this.firstRecordComponent != null) {
            result
                    .putShort(this.symbolTable.addConstantUtf8(Constants.RECORD))
                    .putInt(recordSize + 2)
                    .putShort(recordComponentCount);
            RecordComponentWriter recordComponentWriter = this.firstRecordComponent;
            while (recordComponentWriter != null) {
                recordComponentWriter.putRecordComponentInfo(result);
                recordComponentWriter = (RecordComponentWriter) recordComponentWriter.delegate;
            }
        }
        if (this.firstAttribute != null) {
            this.firstAttribute.putAttributes(this.symbolTable, result);
        }

        // Third step: replace the ASM specific instructions, if any.
        if (hasAsmInstructions) {
            return replaceAsmInstructions(result.data, hasFrames);
        } else {
            return result.data;
        }
    }

    /**
     * Returns the equivalent of the given class file, with the ASM specific instructions replaced
     * with standard ones. This is done with a ClassReader -&gt; ClassWriter round trip.
     *
     * @param classFile a class file containing ASM specific instructions, generated by this
     *                  ClassWriter.
     * @param hasFrames whether there is at least one stack map frames in 'classFile'.
     * @return an equivalent of 'classFile', with the ASM specific instructions replaced with standard
     * ones.
     */
    private byte[] replaceAsmInstructions(final byte[] classFile, final boolean hasFrames) {
        final Attribute[] attributes = getAttributePrototypes();
        this.firstField = null;
        this.lastField = null;
        this.firstMethod = null;
        this.lastMethod = null;
        this.lastRuntimeVisibleAnnotation = null;
        this.lastRuntimeInvisibleAnnotation = null;
        this.lastRuntimeVisibleTypeAnnotation = null;
        this.lastRuntimeInvisibleTypeAnnotation = null;
        this.moduleWriter = null;
        this.nestHostClassIndex = 0;
        this.numberOfNestMemberClasses = 0;
        this.nestMemberClasses = null;
        this.numberOfPermittedSubclasses = 0;
        this.permittedSubclasses = null;
        this.firstRecordComponent = null;
        this.lastRecordComponent = null;
        this.firstAttribute = null;
        this.compute = hasFrames ? MethodWriter.COMPUTE_INSERTED_FRAMES : MethodWriter.COMPUTE_NOTHING;
        new ClassReader(classFile, 0, /* checkClassVersion= */ false)
                .accept(
                        this,
                        attributes,
                        (hasFrames ? ClassReader.EXPAND_FRAMES : 0) | ClassReader.EXPAND_ASM_INSNS);
        return toByteArray();
    }

    /**
     * Returns the prototypes of the attributes used by this class, its fields and its methods.
     *
     * @return the prototypes of the attributes used by this class, its fields and its methods.
     */
    private Attribute[] getAttributePrototypes() {
        Attribute.Set attributePrototypes = new Attribute.Set();
        attributePrototypes.addAttributes(this.firstAttribute);
        FieldWriter fieldWriter = this.firstField;
        while (fieldWriter != null) {
            fieldWriter.collectAttributePrototypes(attributePrototypes);
            fieldWriter = (FieldWriter) fieldWriter.fv;
        }
        MethodWriter methodWriter = this.firstMethod;
        while (methodWriter != null) {
            methodWriter.collectAttributePrototypes(attributePrototypes);
            methodWriter = (MethodWriter) methodWriter.mv;
        }
        RecordComponentWriter recordComponentWriter = this.firstRecordComponent;
        while (recordComponentWriter != null) {
            recordComponentWriter.collectAttributePrototypes(attributePrototypes);
            recordComponentWriter = (RecordComponentWriter) recordComponentWriter.delegate;
        }
        return attributePrototypes.toArray();
    }

    // -----------------------------------------------------------------------------------------------
    // Utility methods: constant pool management for Attribute sub classes
    // -----------------------------------------------------------------------------------------------

    /**
     * Adds a number or string constant to the constant pool of the class being build. Does nothing if
     * the constant pool already contains a similar item. <i>This method is intended for {@link
     * Attribute} sub classes, and is normally not needed by class generators or adapters.</i>
     *
     * @param value the value of the constant to be added to the constant pool. This parameter must be
     *              an {@link Integer}, a {@link Float}, a {@link Long}, a {@link Double} or a {@link String}.
     * @return the index of a new or already existing constant item with the given value.
     */
    public int newConst(final Object value) {
        return this.symbolTable.addConstant(value).index;
    }

    /**
     * Adds an UTF8 string to the constant pool of the class being build. Does nothing if the constant
     * pool already contains a similar item. <i>This method is intended for {@link Attribute} sub
     * classes, and is normally not needed by class generators or adapters.</i>
     *
     * @param value the String value.
     * @return the index of a new or already existing UTF8 item.
     */
    // DontCheck(AbbreviationAsWordInName): can't be renamed (for backward binary compatibility).
    public int newUTF8(final String value) {
        return this.symbolTable.addConstantUtf8(value);
    }

    /**
     * Adds a class reference to the constant pool of the class being build. Does nothing if the
     * constant pool already contains a similar item. <i>This method is intended for {@link Attribute}
     * sub classes, and is normally not needed by class generators or adapters.</i>
     *
     * @param value the internal name of the class (see {@link Type#getInternalName()}).
     * @return the index of a new or already existing class reference item.
     */
    public int newClass(final String value) {
        return this.symbolTable.addConstantClass(value).index;
    }

    /**
     * Adds a method type reference to the constant pool of the class being build. Does nothing if the
     * constant pool already contains a similar item. <i>This method is intended for {@link Attribute}
     * sub classes, and is normally not needed by class generators or adapters.</i>
     *
     * @param methodDescriptor method descriptor of the method type.
     * @return the index of a new or already existing method type reference item.
     */
    public int newMethodType(final String methodDescriptor) {
        return this.symbolTable.addConstantMethodType(methodDescriptor).index;
    }

    /**
     * Adds a module reference to the constant pool of the class being build. Does nothing if the
     * constant pool already contains a similar item. <i>This method is intended for {@link Attribute}
     * sub classes, and is normally not needed by class generators or adapters.</i>
     *
     * @param moduleName name of the module.
     * @return the index of a new or already existing module reference item.
     */
    public int newModule(final String moduleName) {
        return this.symbolTable.addConstantModule(moduleName).index;
    }

    /**
     * Adds a package reference to the constant pool of the class being build. Does nothing if the
     * constant pool already contains a similar item. <i>This method is intended for {@link Attribute}
     * sub classes, and is normally not needed by class generators or adapters.</i>
     *
     * @param packageName name of the package in its internal form.
     * @return the index of a new or already existing module reference item.
     */
    public int newPackage(final String packageName) {
        return this.symbolTable.addConstantPackage(packageName).index;
    }

    /**
     * Adds a handle to the constant pool of the class being build. Does nothing if the constant pool
     * already contains a similar item. <i>This method is intended for {@link Attribute} sub classes,
     * and is normally not needed by class generators or adapters.</i>
     *
     * @param tag        the kind of this handle. Must be {@link Opcodes#H_GETFIELD}, {@link
     *                   Opcodes#H_GETSTATIC}, {@link Opcodes#H_PUTFIELD}, {@link Opcodes#H_PUTSTATIC}, {@link
     *                   Opcodes#H_INVOKEVIRTUAL}, {@link Opcodes#H_INVOKESTATIC}, {@link Opcodes#H_INVOKESPECIAL},
     *                   {@link Opcodes#H_NEWINVOKESPECIAL} or {@link Opcodes#H_INVOKEINTERFACE}.
     * @param owner      the internal name of the field or method owner class (see {@link
     *                   Type#getInternalName()}).
     * @param name       the name of the field or method.
     * @param descriptor the descriptor of the field or method.
     * @return the index of a new or already existing method type reference item.
     * @deprecated this method is superseded by {@link #newHandle(int, String, String, String,
     * boolean)}.
     */
    @Deprecated
    public int newHandle(
            final int tag, final String owner, final String name, final String descriptor) {
        return newHandle(tag, owner, name, descriptor, tag == Opcodes.H_INVOKEINTERFACE);
    }

    /**
     * Adds a handle to the constant pool of the class being build. Does nothing if the constant pool
     * already contains a similar item. <i>This method is intended for {@link Attribute} sub classes,
     * and is normally not needed by class generators or adapters.</i>
     *
     * @param tag         the kind of this handle. Must be {@link Opcodes#H_GETFIELD}, {@link
     *                    Opcodes#H_GETSTATIC}, {@link Opcodes#H_PUTFIELD}, {@link Opcodes#H_PUTSTATIC}, {@link
     *                    Opcodes#H_INVOKEVIRTUAL}, {@link Opcodes#H_INVOKESTATIC}, {@link Opcodes#H_INVOKESPECIAL},
     *                    {@link Opcodes#H_NEWINVOKESPECIAL} or {@link Opcodes#H_INVOKEINTERFACE}.
     * @param owner       the internal name of the field or method owner class (see {@link
     *                    Type#getInternalName()}).
     * @param name        the name of the field or method.
     * @param descriptor  the descriptor of the field or method.
     * @param isInterface true if the owner is an interface.
     * @return the index of a new or already existing method type reference item.
     */
    public int newHandle(
            final int tag,
            final String owner,
            final String name,
            final String descriptor,
            final boolean isInterface) {
        return this.symbolTable.addConstantMethodHandle(tag, owner, name, descriptor, isInterface).index;
    }

    /**
     * Adds a dynamic constant reference to the constant pool of the class being build. Does nothing
     * if the constant pool already contains a similar item. <i>This method is intended for {@link
     * Attribute} sub classes, and is normally not needed by class generators or adapters.</i>
     *
     * @param name                     name of the invoked method.
     * @param descriptor               field descriptor of the constant type.
     * @param bootstrapMethodHandle    the bootstrap method.
     * @param bootstrapMethodArguments the bootstrap method constant arguments.
     * @return the index of a new or already existing dynamic constant reference item.
     */
    public int newConstantDynamic(
            final String name,
            final String descriptor,
            final Handle bootstrapMethodHandle,
            final Object... bootstrapMethodArguments) {
        return this.symbolTable.addConstantDynamic(
                name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments)
                .index;
    }

    /**
     * Adds an invokedynamic reference to the constant pool of the class being build. Does nothing if
     * the constant pool already contains a similar item. <i>This method is intended for {@link
     * Attribute} sub classes, and is normally not needed by class generators or adapters.</i>
     *
     * @param name                     name of the invoked method.
     * @param descriptor               descriptor of the invoke method.
     * @param bootstrapMethodHandle    the bootstrap method.
     * @param bootstrapMethodArguments the bootstrap method constant arguments.
     * @return the index of a new or already existing invokedynamic reference item.
     */
    public int newInvokeDynamic(
            final String name,
            final String descriptor,
            final Handle bootstrapMethodHandle,
            final Object... bootstrapMethodArguments) {
        return this.symbolTable.addConstantInvokeDynamic(
                name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments)
                .index;
    }

    /**
     * Adds a field reference to the constant pool of the class being build. Does nothing if the
     * constant pool already contains a similar item. <i>This method is intended for {@link Attribute}
     * sub classes, and is normally not needed by class generators or adapters.</i>
     *
     * @param owner      the internal name of the field's owner class (see {@link Type#getInternalName()}).
     * @param name       the field's name.
     * @param descriptor the field's descriptor.
     * @return the index of a new or already existing field reference item.
     */
    public int newField(final String owner, final String name, final String descriptor) {
        return this.symbolTable.addConstantFieldref(owner, name, descriptor).index;
    }

    /**
     * Adds a method reference to the constant pool of the class being build. Does nothing if the
     * constant pool already contains a similar item. <i>This method is intended for {@link Attribute}
     * sub classes, and is normally not needed by class generators or adapters.</i>
     *
     * @param owner       the internal name of the method's owner class (see {@link
     *                    Type#getInternalName()}).
     * @param name        the method's name.
     * @param descriptor  the method's descriptor.
     * @param isInterface {@literal true} if {@code owner} is an interface.
     * @return the index of a new or already existing method reference item.
     */
    public int newMethod(
            final String owner, final String name, final String descriptor, final boolean isInterface) {
        return this.symbolTable.addConstantMethodref(owner, name, descriptor, isInterface).index;
    }

    /**
     * Adds a name and type to the constant pool of the class being build. Does nothing if the
     * constant pool already contains a similar item. <i>This method is intended for {@link Attribute}
     * sub classes, and is normally not needed by class generators or adapters.</i>
     *
     * @param name       a name.
     * @param descriptor a type descriptor.
     * @return the index of a new or already existing name and type item.
     */
    public int newNameType(final String name, final String descriptor) {
        return this.symbolTable.addConstantNameAndType(name, descriptor);
    }

    // -----------------------------------------------------------------------------------------------
    // Default method to compute common super classes when computing stack map frames
    // -----------------------------------------------------------------------------------------------

    /**
     * Returns the common super type of the two given types. The default implementation of this method
     * <i>loads</i> the two given classes and uses the java.lang.Class methods to find the common
     * super class. It can be overridden to compute this common super type in other ways, in
     * particular without actually loading any class, or to take into account the class that is
     * currently being generated by this ClassWriter, which can of course not be loaded since it is
     * under construction.
     *
     * @param type1 the internal name of a class (see {@link Type#getInternalName()}).
     * @param type2 the internal name of another class (see {@link Type#getInternalName()}).
     * @return the internal name of the common super class of the two given classes (see {@link
     * Type#getInternalName()}).
     */
    protected String getCommonSuperClass(final String type1, final String type2) {
        ClassLoader classLoader = getClassLoader();
        Class<?> class1;
        try {
            class1 = Class.forName(type1.replace('/', '.'), false, classLoader);
        } catch (ClassNotFoundException e) {
            throw new TypeNotPresentException(type1, e);
        }
        Class<?> class2;
        try {
            class2 = Class.forName(type2.replace('/', '.'), false, classLoader);
        } catch (ClassNotFoundException e) {
            throw new TypeNotPresentException(type2, e);
        }
        if (class1.isAssignableFrom(class2)) {
            return type1;
        }
        if (class2.isAssignableFrom(class1)) {
            return type2;
        }
        if (class1.isInterface() || class2.isInterface()) {
            return "java/lang/Object";
        } else {
            do {
                class1 = class1.getSuperclass();
            } while (!class1.isAssignableFrom(class2));
            return class1.getName().replace('.', '/');
        }
    }

    /**
     * Returns the {@link ClassLoader} to be used by the default implementation of {@link
     * #getCommonSuperClass(String, String)}, that of this {@link ClassWriter}'s runtime type by
     * default.
     *
     * @return ClassLoader
     */
    protected ClassLoader getClassLoader() {
        return getClass().getClassLoader();
    }
}
