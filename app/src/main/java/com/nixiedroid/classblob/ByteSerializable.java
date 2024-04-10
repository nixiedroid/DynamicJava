package com.nixiedroid.classblob;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public abstract class ByteSerializable<T> {
   abstract T deserialize(ByteArrayInputStream stream) throws IOException;
   abstract void serialize(ByteArrayOutputStream stream)throws IOException;
}
