/*
 * Created by IntelliJ IDEA.
 * User: jbirchfield
 * Date: Mar 19, 2002
 * Time: 9:33:22 AM
 */
package com.tcs.ebw.security;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSACryptoTest {

    public static final String PUB_FILE = "public.key";
    public static final String PRI_FILE = "private.key";
    public static final int PRIVATE = 0;
    public static final int PUBLIC = 1;
    public static final String ALGORITHM = "RSA/ECB/OAEPPadding";

    public static void main(String[] args) {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        new RSACryptoTest();
    }

    public RSACryptoTest() {
        PrivateKey priKey = null;
        PublicKey pubKey = null;
        try {
            priKey = (PrivateKey)getKey(PRI_FILE, PRIVATE);
            pubKey = (PublicKey)getKey(PUB_FILE, PUBLIC);
        } catch (FileNotFoundException e) {
            KeyPairGenerator kpg = initKeys();
            try {
                System.err.println("Keys not found....Generating.");
                priKey = (PrivateKey)getKey(PRI_FILE, PRIVATE);
                pubKey = (PublicKey)getKey(PUB_FILE, PUBLIC);
            } catch (FileNotFoundException e1) {
                System.err.println("Error creating keys...");
                System.exit(-1);
            }
        }

        Cipher c = null;
        try {
            c = Cipher.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();

        }

        String message = generateMessage();

        byte[] text = message.getBytes();

        System.out.println("Encrypting message...");
        text = crypt(c, pubKey, text, Cipher.ENCRYPT_MODE);
        System.out.println("text = " + new String(text));
        System.out.println("Decrypting message...");
        text = crypt(c, priKey, text, Cipher.DECRYPT_MODE);
        System.out.println("text = " + new String(text));

    }


    private KeyPairGenerator initKeys() {
        System.out.println("Generating KeyPairGnerator...");
        KeyPairGenerator kpg = null;
        try {
            kpg = KeyPairGenerator.getInstance("RSA", "BC");
            kpg.initialize(1024, new SecureRandom());
            KeyPair kp = kpg.generateKeyPair();
            PrivateKey priKey = kp.getPrivate();
            writeKey(PRI_FILE, priKey.getEncoded());
            PublicKey pubKey = kp.getPublic();
            writeKey(PUB_FILE, pubKey.getEncoded());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return kpg;
    }

    private void writeKey(String filename, byte[] contents) {
        try {
            FileOutputStream fos = new FileOutputStream(filename);
            fos.write(contents);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] crypt(Cipher cipher, Key key, byte[] text, int type) {
        ByteArrayOutputStream out = null;
        try {
            cipher.init(type, key);
            int bzise = cipher.getBlockSize();
            out = new ByteArrayOutputStream();
            int s = cipher.getBlockSize();
            int r = 0;
            for (int t = 0; t < text.length; t += s) {
                if (text.length - t <= s) {
                    r = text.length - t;
                } else {
                    r = s;
                }
                out.write(cipher.doFinal(text, t, r));
            }
            out.flush();
            out.close();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

    private Key getKey(String filename, int type) throws FileNotFoundException {
        FileInputStream fis = null;
        fis = new FileInputStream(filename);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int b;
        try {
            while ((b = fis.read()) != -1) {
                baos.write(b);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] keydata = baos.toByteArray();

        Key key = null;
        try {
            KeyFactory kf = KeyFactory.getInstance("RSA", "BC");
            switch (type) {
                case PRIVATE:
                    PKCS8EncodedKeySpec encodedPrivateKey = new PKCS8EncodedKeySpec(keydata);
                    key = kf.generatePrivate(encodedPrivateKey);
                    return key;
                case PUBLIC:
                    X509EncodedKeySpec encodedPublicKey = new X509EncodedKeySpec(keydata);
                    key = kf.generatePublic(encodedPublicKey);
                    return key;
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return key;
    }

    private String generateMessage() {
        StringBuffer buf = new StringBuffer();
        buf.append("<doc>");
       buf.append(System.getProperty("line.separator"));
        for (int i = 0; i < 5; i++) {
            buf.append("<tag>Hello World!</tag>");
            buf.append(System.getProperty("line.separator"));
        }
        buf.append("</doc>");
        return buf.toString();
    }
}

