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
 * A {@link ModuleVisitor} that generates the corresponding Module, ModulePackages and
 * ModuleMainClass attributes, as defined in the Java Virtual Machine Specification (JVMS).
 *
 * @author Remi Forax
 * @author Eric Bruneton
 * @see <a href="https://docs.oracle.com/javase/specs/jvms/se9/html/jvms-4.html#jvms-4.7.25">JVMS
 * 4.7.25</a>
 * @see <a href="https://docs.oracle.com/javase/specs/jvms/se9/html/jvms-4.html#jvms-4.7.26">JVMS
 * 4.7.26</a>
 * @see <a href="https://docs.oracle.com/javase/specs/jvms/se9/html/jvms-4.html#jvms-4.7.27">JVMS
 * 4.7.27</a>
 */
@SuppressWarnings("MagicNumber")
final class ModuleWriter extends ModuleVisitor {

    /**
     * Where the constants used in this AnnotationWriter must be stored.
     */
    private final SymbolTable symbolTable;

    /**
     * The module_name_index field of the JVMS Module attribute.
     */
    private final int moduleNameIndex;

    /**
     * The module_flags field of the JVMS Module attribute.
     */
    private final int moduleFlags;

    /**
     * The module_version_index field of the JVMS Module attribute.
     */
    private final int moduleVersionIndex;
    /**
     * The binary content of the 'requires' array of the JVMS Module attribute.
     */
    private final ByteVector requires;
    /**
     * The binary content of the 'exports' array of the JVMS Module attribute.
     */
    private final ByteVector exports;
    /**
     * The binary content of the 'opens' array of the JVMS Module attribute.
     */
    private final ByteVector opens;
    /**
     * The binary content of the 'uses_index' array of the JVMS Module attribute.
     */
    private final ByteVector usesIndex;
    /**
     * The binary content of the 'provides' array of the JVMS Module attribute.
     */
    private final ByteVector provides;
    /**
     * The binary content of the 'package_index' array of the JVMS ModulePackages attribute.
     */
    private final ByteVector packageIndex;
    /**
     * The requires_count field of the JVMS Module attribute.
     */
    private int requiresCount;
    /**
     * The exports_count field of the JVMS Module attribute.
     */
    private int exportsCount;
    /**
     * The opens_count field of the JVMS Module attribute.
     */
    private int opensCount;
    /**
     * The uses_count field of the JVMS Module attribute.
     */
    private int usesCount;
    /**
     * The provides_count field of the JVMS Module attribute.
     */
    private int providesCount;
    /**
     * The provides_count field of the JVMS ModulePackages attribute.
     */
    private int packageCount;
    /**
     * The main_class_index field of the JVMS ModuleMainClass attribute, or 0.
     */
    private int mainClassIndex;

    ModuleWriter(final SymbolTable symbolTable, final int name, final int access, final int version) {
        super(/* latest api = */ Opcodes.ASM9);
        this.symbolTable = symbolTable;
        this.moduleNameIndex = name;
        this.moduleFlags = access;
        this.moduleVersionIndex = version;
        this.requires = new ByteVector();
        this.exports = new ByteVector();
        this.opens = new ByteVector();
        this.usesIndex = new ByteVector();
        this.provides = new ByteVector();
        this.packageIndex = new ByteVector();
    }

    @Override
    public void visitMainClass(final String mainClass) {
        this.mainClassIndex = this.symbolTable.addConstantClass(mainClass).index;
    }

    @Override
    public void visitPackage(final String packaze) {
        this.packageIndex.putShort(this.symbolTable.addConstantPackage(packaze).index);
        this.packageCount++;
    }

    @Override
    public void visitRequire(final String module, final int access, final String version) {
        this.requires
                .putShort(this.symbolTable.addConstantModule(module).index)
                .putShort(access)
                .putShort(version == null ? 0 : this.symbolTable.addConstantUtf8(version));
        this.requiresCount++;
    }

    @Override
    public void visitExport(final String packaze, final int access, final String... modules) {
        this.exports.putShort(this.symbolTable.addConstantPackage(packaze).index).putShort(access);
        if (modules == null) {
            this.exports.putShort(0);
        } else {
            this.exports.putShort(modules.length);
            for (String module : modules) {
                this.exports.putShort(this.symbolTable.addConstantModule(module).index);
            }
        }
        this.exportsCount++;
    }

    @Override
    public void visitOpen(final String packaze, final int access, final String... modules) {
        this.opens.putShort(this.symbolTable.addConstantPackage(packaze).index).putShort(access);
        if (modules == null) {
            this.opens.putShort(0);
        } else {
            this.opens.putShort(modules.length);
            for (String module : modules) {
                this.opens.putShort(this.symbolTable.addConstantModule(module).index);
            }
        }
        this.opensCount++;
    }

    @Override
    public void visitUse(final String service) {
        this.usesIndex.putShort(this.symbolTable.addConstantClass(service).index);
        this.usesCount++;
    }

    @Override
    public void visitProvide(final String service, final String... providers) {
        this.provides.putShort(this.symbolTable.addConstantClass(service).index);
        this.provides.putShort(providers.length);
        for (String provider : providers) {
            this.provides.putShort(this.symbolTable.addConstantClass(provider).index);
        }
        this.providesCount++;
    }

    @Override
    public void visitEnd() {
        // Nothing to do.
    }

    /**
     * Returns the number of Module, ModulePackages and ModuleMainClass attributes generated by this
     * ModuleWriter.
     *
     * @return the number of Module, ModulePackages and ModuleMainClass attributes (between 1 and 3).
     */
    int getAttributeCount() {
        return 1 + (this.packageCount > 0 ? 1 : 0) + (this.mainClassIndex > 0 ? 1 : 0);
    }

    /**
     * Returns the size of the Module, ModulePackages and ModuleMainClass attributes generated by this
     * ModuleWriter. Also add the names of these attributes in the constant pool.
     *
     * @return the size in bytes of the Module, ModulePackages and ModuleMainClass attributes.
     */
    int computeAttributesSize() {
        this.symbolTable.addConstantUtf8(Constants.MODULE);
        // 6 attribute header bytes, 6 bytes for name, flags and version, and 5 * 2 bytes for counts.
        int size =
                22 + this.requires.length + this.exports.length + this.opens.length + this.usesIndex.length + this.provides.length;
        if (this.packageCount > 0) {
            this.symbolTable.addConstantUtf8(Constants.MODULE_PACKAGES);
            // 6 attribute header bytes, and 2 bytes for package_count.
            size += 8 + this.packageIndex.length;
        }
        if (this.mainClassIndex > 0) {
            this.symbolTable.addConstantUtf8(Constants.MODULE_MAIN_CLASS);
            // 6 attribute header bytes, and 2 bytes for main_class_index.
            size += 8;
        }
        return size;
    }

    /**
     * Puts the Module, ModulePackages and ModuleMainClass attributes generated by this ModuleWriter
     * in the given ByteVector.
     *
     * @param output where the attributes must be put.
     */
    void putAttributes(final ByteVector output) {
        // 6 bytes for name, flags and version, and 5 * 2 bytes for counts.
        int moduleAttributeLength =
                16 + this.requires.length + this.exports.length + this.opens.length + this.usesIndex.length + this.provides.length;
        output
                .putShort(this.symbolTable.addConstantUtf8(Constants.MODULE))
                .putInt(moduleAttributeLength)
                .putShort(this.moduleNameIndex)
                .putShort(this.moduleFlags)
                .putShort(this.moduleVersionIndex)
                .putShort(this.requiresCount)
                .putByteArray(this.requires.data, 0, this.requires.length)
                .putShort(this.exportsCount)
                .putByteArray(this.exports.data, 0, this.exports.length)
                .putShort(this.opensCount)
                .putByteArray(this.opens.data, 0, this.opens.length)
                .putShort(this.usesCount)
                .putByteArray(this.usesIndex.data, 0, this.usesIndex.length)
                .putShort(this.providesCount)
                .putByteArray(this.provides.data, 0, this.provides.length);
        if (this.packageCount > 0) {
            output
                    .putShort(this.symbolTable.addConstantUtf8(Constants.MODULE_PACKAGES))
                    .putInt(2 + this.packageIndex.length)
                    .putShort(this.packageCount)
                    .putByteArray(this.packageIndex.data, 0, this.packageIndex.length);
        }
        if (this.mainClassIndex > 0) {
            output
                    .putShort(this.symbolTable.addConstantUtf8(Constants.MODULE_MAIN_CLASS))
                    .putInt(2)
                    .putShort(this.mainClassIndex);
        }
    }
}
