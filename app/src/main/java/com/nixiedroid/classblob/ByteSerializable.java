package com.nixiedroid.classblob;

import com.nixiedroid.bytes.ByteArrayConverter;
import com.nixiedroid.bytes.ByteArrayConverterDefault;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public abstract class ByteSerializable<T> {
    protected final ByteArrayConverter converter = new ByteArrayConverterDefault();

   abstract T deserialize(ByteArrayInputStream stream) throws IOException;
   abstract public void serialize(ByteArrayOutputStream stream)throws IOException;
    ByteSerializable(ByteArrayInputStream stream) throws IOException {
      deserialize(stream);
   }
   ByteSerializable(){}
}
