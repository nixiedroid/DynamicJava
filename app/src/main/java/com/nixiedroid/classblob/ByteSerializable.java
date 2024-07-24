package com.nixiedroid.classblob;

import com.nixiedroid.bytes.streams.PrimitiveInputStream;
import com.nixiedroid.bytes.streams.PrimitiveOutputStream;

import java.io.IOException;

public abstract class ByteSerializable<T> {


   abstract void deserialize(PrimitiveInputStream stream) throws IOException;
   abstract public void serialize(PrimitiveOutputStream stream)throws IOException;
   public ByteSerializable(PrimitiveInputStream stream) throws IOException {
      deserialize(stream);
   }
   ByteSerializable(){}
}
