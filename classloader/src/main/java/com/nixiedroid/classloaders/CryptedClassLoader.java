package com.nixiedroid.classloaders;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class CryptedClassLoader extends FileClassLoader{
    private static Cipher cipherEnc;
    private static Cipher cipherDec;
    private static SecretKey key;
    static {
        String keyStr = "ULTRA-SECRET-KEY";
        SecretKey key = new SecretKeySpec(keyStr.getBytes(StandardCharsets.UTF_8),"AES");
        try {
            cipherEnc = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipherDec = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipherEnc.init(Cipher.ENCRYPT_MODE,key);
            cipherDec.init(Cipher.DECRYPT_MODE,key);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public String getName() {
        return "crypted";
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        String classname = name.substring(name.lastIndexOf('.')+1);
        byte[] classBytes = decrypt(readFile("/" + classname+".enc"));
        return defineClass(name, classBytes, 0, classBytes.length); //Name must be equal to inside class
    }
    @Override
    protected byte[] readFile(String name) throws ClassNotFoundException {
        byte[] fileBytes;
        try ( var res = this.getClass().getResourceAsStream(name)) {
            fileBytes = new byte[1];
            if (res != null) fileBytes = res.readAllBytes();
            if (fileBytes.length>4) {
                return fileBytes;
            } else throw new IOException("Invalid class file");
        } catch (IOException e) {
            throw new ClassNotFoundException();
        }
    }
    public static byte[] encrypt (byte[] data){
        try {
            return cipherEnc.doFinal(data);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }
    public static byte[] decrypt (byte[] data){
        try {
            return cipherDec.doFinal(data);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }


}
