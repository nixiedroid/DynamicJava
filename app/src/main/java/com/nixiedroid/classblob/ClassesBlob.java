package com.nixiedroid.classblob;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

public class ClassesBlob extends ByteSerializable {
    BlobHeader blobHeader;
    List<ClassRecord> classes  = new LinkedList<>();

    @Override
    ClassesBlob deserialize(ByteArrayInputStream stream) throws IOException {
        this.blobHeader = new BlobHeader(stream);
        for (int i = 0; i < this.blobHeader.getClassesCount(); i++) {
            classes.add(new ClassRecord(stream));
        }
        return this;
    }

    @Override
    void serialize(ByteArrayOutputStream stream) throws IOException {
        for (int i = this.blobHeader.getClassesCount(); i  > 0; i++) {
           classes.get(i).serialize(stream);
        }
    }
}
