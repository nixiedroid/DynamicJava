package com.nixiedroid.modules.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public final class Resources {
    private Resources() {
    }
    public static byte[] getResourceBytes(String resName){
        try (InputStream res =  Resources.class.getResourceAsStream(resName)) {
            if (res == null) throw new IOException("Resource not found");
            return res.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
