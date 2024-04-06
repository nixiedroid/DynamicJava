package com.nixiedroid.classloaders;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class CryptedClassLoader extends FileClassLoader {
    private static final Cipher cipherEnc;
    private static final Cipher cipherDec;
    private static final String ALG = "AES/ECB/PKCS5Padding";
    private static final String keyStr = "ULTRA-SECRET-KEY";
    private static final SecretKey key = new SecretKeySpec(keyStr.getBytes(StandardCharsets.UTF_8), "AES");

    static {
        try {
            cipherEnc = Cipher.getInstance(ALG);
            cipherDec = Cipher.getInstance(ALG);
            cipherEnc.init(Cipher.ENCRYPT_MODE, key);
            cipherDec.init(Cipher.DECRYPT_MODE, key);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] encrypt(byte[] data) {
        try {
            return cipherEnc.doFinal(data);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] decrypt(byte[] data) {
        try {
            return cipherDec.doFinal(data);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getName() {
        return "crypted";
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] classBytes = decrypt(readFile(getFileName(name, ".enc")));
        String realClassName = getRealClassName(classBytes);
        return defineClass(realClassName, classBytes, 0, classBytes.length); //Name must be equal to inside class
    }
}
