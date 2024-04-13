package com.nixiedroid.classblob;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public abstract class ByteSerializable<T> {
   abstract T deserialize(ByteArrayInputStream stream) throws IOException;
   abstract public void serialize(ByteArrayOutputStream stream)throws IOException;
    ByteSerializable(ByteArrayInputStream stream) throws IOException {
      deserialize(stream);
   }
   ByteSerializable(){}
}
