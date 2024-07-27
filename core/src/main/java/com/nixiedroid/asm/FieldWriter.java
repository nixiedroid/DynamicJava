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
 * A {@link FieldVisitor} that generates a corresponding 'field_info' structure, as defined in the
 * Java Virtual Machine Specification (JVMS).
 *
 * @author Eric Bruneton
 * @see <a href="https://docs.oracle.com/javase/specs/jvms/se9/html/jvms-4.html#jvms-4.5">JVMS
 * 4.5</a>
 */
final class FieldWriter extends FieldVisitor {

    /**
     * Where the constants used in this FieldWriter must be stored.
     */
    private final SymbolTable symbolTable;

    // Note: fields are ordered as in the field_info structure, and those related to attributes are
    // ordered as in Section 4.7 of the JVMS.

    /**
     * The access_flags field of the field_info JVMS structure. This field can contain ASM specific
     * access flags, such as {@link Opcodes#ACC_DEPRECATED}, which are removed when generating the
     * ClassFile structure.
     */
    private final int accessFlags;

    /**
     * The name_index field of the field_info JVMS structure.
     */
    private final int nameIndex;

    /**
     * The descriptor_index field of the field_info JVMS structure.
     */
    private final int descriptorIndex;

    /**
     * The signature_index field of the Signature attribute of this field_info, or 0 if there is no
     * Signature attribute.
     */
    private int signatureIndex;

    /**
     * The constantvalue_index field of the ConstantValue attribute of this field_info, or 0 if there
     * is no ConstantValue attribute.
     */
    private int constantValueIndex;

    /**
     * The last runtime visible annotation of this field. The previous ones can be accessed with the
     * {@link AnnotationWriter#previousAnnotation} field. May be {@literal null}.
     */
    private AnnotationWriter lastRuntimeVisibleAnnotation;

    /**
     * The last runtime invisible annotation of this field. The previous ones can be accessed with the
     * {@link AnnotationWriter#previousAnnotation} field. May be {@literal null}.
     */
    private AnnotationWriter lastRuntimeInvisibleAnnotation;

    /**
     * The last runtime visible type annotation of this field. The previous ones can be accessed with
     * the {@link AnnotationWriter#previousAnnotation} field. May be {@literal null}.
     */
    private AnnotationWriter lastRuntimeVisibleTypeAnnotation;

    /**
     * The last runtime invisible type annotation of this field. The previous ones can be accessed
     * with the {@link AnnotationWriter#previousAnnotation} field. May be {@literal null}.
     */
    private AnnotationWriter lastRuntimeInvisibleTypeAnnotation;

    /**
     * The first non standard attribute of this field. The next ones can be accessed with the {@link
     * Attribute#nextAttribute} field. May be {@literal null}.
     *
     * <p><b>WARNING</b>: this list stores the attributes in the <i>reverse</i> order of their visit.
     * firstAttribute is actually the last attribute visited in {@link #visitAttribute}. The {@link
     * #putFieldInfo} method writes the attributes in the order defined by this list, i.e. in the
     * reverse order specified by the user.
     */
    private Attribute firstAttribute;

    // -----------------------------------------------------------------------------------------------
    // Constructor
    // -----------------------------------------------------------------------------------------------

    /**
     * Constructs a new {@link FieldWriter}.
     *
     * @param symbolTable   where the constants used in this FieldWriter must be stored.
     * @param access        the field's access flags (see {@link Opcodes}).
     * @param name          the field's name.
     * @param descriptor    the field's descriptor (see {@link Type}).
     * @param signature     the field's signature. May be {@literal null}.
     * @param constantValue the field's constant value. May be {@literal null}.
     */
    FieldWriter(
            final SymbolTable symbolTable,
            final int access,
            final String name,
            final String descriptor,
            final String signature,
            final Object constantValue) {
        super(/* latest api = */ Opcodes.ASM9);
        this.symbolTable = symbolTable;
        this.accessFlags = access;
        this.nameIndex = symbolTable.addConstantUtf8(name);
        this.descriptorIndex = symbolTable.addConstantUtf8(descriptor);
        if (signature != null) {
            this.signatureIndex = symbolTable.addConstantUtf8(signature);
        }
        if (constantValue != null) {
            this.constantValueIndex = symbolTable.addConstant(constantValue).index;
        }
    }

    // -----------------------------------------------------------------------------------------------
    // Implementation of the FieldVisitor abstract class
    // -----------------------------------------------------------------------------------------------

    @Override
    public AnnotationVisitor visitAnnotation(final String descriptor, final boolean visible) {
        if (visible) {
            return this.lastRuntimeVisibleAnnotation =
                    AnnotationWriter.create(this.symbolTable, descriptor, this.lastRuntimeVisibleAnnotation);
        } else {
            return this.lastRuntimeInvisibleAnnotation =
                    AnnotationWriter.create(this.symbolTable, descriptor, this.lastRuntimeInvisibleAnnotation);
        }
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(
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
    public void visitAttribute(final Attribute attribute) {
        // Store the attributes in the <i>reverse</i> order of their visit by this method.
        attribute.nextAttribute = this.firstAttribute;
        this.firstAttribute = attribute;
    }

    @Override
    public void visitEnd() {
        // Nothing to do.
    }

    // -----------------------------------------------------------------------------------------------
    // Utility methods
    // -----------------------------------------------------------------------------------------------

    /**
     * Returns the size of the field_info JVMS structure generated by this FieldWriter. Also adds the
     * names of the attributes of this field in the constant pool.
     *
     * @return the size in bytes of the field_info JVMS structure.
     */
    int computeFieldInfoSize() {
        // The access_flags, name_index, descriptor_index and attributes_count fields use 8 bytes.
        int size = 8;
        // For ease of reference, we use here the same attribute order as in Section 4.7 of the JVMS.
        if (this.constantValueIndex != 0) {
            // ConstantValue attributes always use 8 bytes.
            this.symbolTable.addConstantUtf8(Constants.CONSTANT_VALUE);
            size += 8;
        }
        size += Attribute.computeAttributesSize(this.symbolTable, this.accessFlags, this.signatureIndex);
        size +=
                AnnotationWriter.computeAnnotationsSize(
                        this.lastRuntimeVisibleAnnotation,
                        this.lastRuntimeInvisibleAnnotation,
                        this.lastRuntimeVisibleTypeAnnotation,
                        this.lastRuntimeInvisibleTypeAnnotation);
        if (this.firstAttribute != null) {
            size += this.firstAttribute.computeAttributesSize(this.symbolTable);
        }
        return size;
    }

    /**
     * Puts the content of the field_info JVMS structure generated by this FieldWriter into the given
     * ByteVector.
     *
     * @param output where the field_info structure must be put.
     */
    void putFieldInfo(final ByteVector output) {
        boolean useSyntheticAttribute = this.symbolTable.getMajorVersion() < Opcodes.V1_5;
        // Put the access_flags, name_index and descriptor_index fields.
        int mask = useSyntheticAttribute ? Opcodes.ACC_SYNTHETIC : 0;
        output.putShort(this.accessFlags & ~mask).putShort(this.nameIndex).putShort(this.descriptorIndex);
        // Compute and put the attributes_count field.
        // For ease of reference, we use here the same attribute order as in Section 4.7 of the JVMS.
        int attributesCount = 0;
        if (this.constantValueIndex != 0) {
            ++attributesCount;
        }
        if ((this.accessFlags & Opcodes.ACC_SYNTHETIC) != 0 && useSyntheticAttribute) {
            ++attributesCount;
        }
        if (this.signatureIndex != 0) {
            ++attributesCount;
        }
        if ((this.accessFlags & Opcodes.ACC_DEPRECATED) != 0) {
            ++attributesCount;
        }
        if (this.lastRuntimeVisibleAnnotation != null) {
            ++attributesCount;
        }
        if (this.lastRuntimeInvisibleAnnotation != null) {
            ++attributesCount;
        }
        if (this.lastRuntimeVisibleTypeAnnotation != null) {
            ++attributesCount;
        }
        if (this.lastRuntimeInvisibleTypeAnnotation != null) {
            ++attributesCount;
        }
        if (this.firstAttribute != null) {
            attributesCount += this.firstAttribute.getAttributeCount();
        }
        output.putShort(attributesCount);
        // Put the field_info attributes.
        // For ease of reference, we use here the same attribute order as in Section 4.7 of the JVMS.
        if (this.constantValueIndex != 0) {
            output
                    .putShort(this.symbolTable.addConstantUtf8(Constants.CONSTANT_VALUE))
                    .putInt(2)
                    .putShort(this.constantValueIndex);
        }
        Attribute.putAttributes(this.symbolTable, this.accessFlags, this.signatureIndex, output);
        AnnotationWriter.putAnnotations(
                this.symbolTable,
                this.lastRuntimeVisibleAnnotation,
                this.lastRuntimeInvisibleAnnotation,
                this.lastRuntimeVisibleTypeAnnotation,
                this.lastRuntimeInvisibleTypeAnnotation,
                output);
        if (this.firstAttribute != null) {
            this.firstAttribute.putAttributes(this.symbolTable, output);
        }
    }

    /**
     * Collects the attributes of this field into the given set of attribute prototypes.
     *
     * @param attributePrototypes a set of attribute prototypes.
     */
    void collectAttributePrototypes(final Attribute.Set attributePrototypes) {
        attributePrototypes.addAttributes(this.firstAttribute);
    }
}
