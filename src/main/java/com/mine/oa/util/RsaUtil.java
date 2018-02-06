package com.mine.oa.util;

import java.io.*;
import java.math.BigInteger;
import java.security.*;

import javax.crypto.Cipher;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mine.oa.entity.UserPO;

public final class RsaUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(RsaUtil.class);
    /** 密钥对文件位置 */
    private static String KEY_FILE_URL = System.getProperty("user.dir") + "/rsaKey.ini";
    /** 密钥长度 */
    private static final int KEY_LENGTH = 512;

    /***
     * 生成密钥对并存储成文件
     * 
     * @return 密钥对
     */
    private static KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", new BouncyCastleProvider());
            keyPairGenerator.initialize(KEY_LENGTH, new SecureRandom());
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            saveKeyPair(keyPair);
            return keyPair;
        } catch (Exception e) {
            LOGGER.error("RSA密钥对生成异常", e);
            return null;
        }
    }

    /***
     * 将密钥对保存成文件
     * 
     * @param keyPair 密钥对
     * @throws IOException 文件操作异常
     */
    private static void saveKeyPair(KeyPair keyPair) throws IOException {
        OutputStream fileOutputStream = new FileOutputStream(KEY_FILE_URL);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(keyPair);
        objectOutputStream.close();
        fileOutputStream.close();
    }

    /***
     * 获取密钥对
     * 
     * @return 密钥对
     * @throws Exception 文件操作异常
     */
    public static KeyPair getKeyPair() throws Exception {
        File keyFile = new File(KEY_FILE_URL);
        if (!keyFile.exists()) {
            return generateKeyPair();
        }
        InputStream inputStream = new FileInputStream(keyFile);
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        KeyPair keyPair = (KeyPair) objectInputStream.readObject();
        objectInputStream.close();
        inputStream.close();
        return keyPair;
    }

    /***
     * 使用公钥加密字符串
     * 
     * @param publicKey 公钥
     * @param plainText 明文
     * @return 加密后字节数组
     */
    private static byte[] encrypt(PublicKey publicKey, String plainText) {
        try {
            Cipher cipher = Cipher.getInstance("RSA", new BouncyCastleProvider());
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            int blockSize = cipher.getBlockSize();// 获得加密块大小，如：加密前数据为128个byte，而key_size=1024
            // 加密块大小为127
            // byte,加密后为128个byte;因此共有2个加密块，第一个127
            // byte第二个为1个byte
            byte[] data = plainText.getBytes();
            int outputSize = cipher.getOutputSize(data.length);// 获得加密块加密后块大小
            int leavedSize = data.length % blockSize;
            int blocksSize = leavedSize != 0 ? data.length / blockSize + 1 : data.length / blockSize;
            byte[] raw = new byte[outputSize * blocksSize];
            int i = 0;
            while (data.length - i * blockSize > 0) {
                if (data.length - i * blockSize > blockSize)
                    cipher.doFinal(data, i * blockSize, blockSize, raw, i * outputSize);
                else
                    cipher.doFinal(data, i * blockSize, data.length - i * blockSize, raw, i * outputSize);
                // 这里面doUpdate方法不可用，查看源代码后发现每次doUpdate后并没有什么实际动作除了把byte[]放到
                // ByteArrayOutputStream中，而最后doFinal的时候才将所有的byte[]进行加密，可是到了此时加密块大小很可能已经超出了
                // OutputSize所以只好用dofinal方法。

                i++;
            }
            return raw;
        } catch (Exception e) {
            LOGGER.error("rsa公钥加密异常", e);
            return null;
        }
    }

    /***
     * 使用私钥解密
     * 
     * @param privateKey 私钥
     * @param raw 密文字节数组
     * @return 明文
     */
    private static String decrypt(PrivateKey privateKey, byte[] raw) {
        try {
            Cipher cipher = Cipher.getInstance("RSA", new BouncyCastleProvider());
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            int blockSize = cipher.getBlockSize();
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            int j = 0;
            while (raw.length - j * blockSize > 0) {
                bout.write(cipher.doFinal(raw, j * blockSize, blockSize));
                j++;
            }
            return new String(bout.toByteArray());
        } catch (Exception e) {
            LOGGER.error("rsa私钥解密异常", e);
            return null;
        }
    }

    // byte数组转十六进制字符串
    // private static String bytesToHexString(byte[] bytes) {
    // StringBuilder sb = new StringBuilder(bytes.length);
    // String sTemp;
    // for (int i = 0; i < bytes.length; i++) {
    // sTemp = Integer.toHexString(0xFF & bytes[i]);
    // if (sTemp.length() < 2)
    // sb.append(0);
    // sb.append(sTemp.toUpperCase());
    // }
    // return sb.toString();
    // }

    // 十六进制字符串转byte数组
    private static byte[] hexString2Bytes(String hex) {
        int len = (hex.length() / 2);
        hex = hex.toUpperCase();
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    private static byte toByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    @SuppressWarnings("ConstantConditions")
    public static String encrypt(String plain) {
        try {
            return new BigInteger(1, encrypt(getKeyPair().getPublic(), plain)).toString(16);
        } catch (Exception e) {
            LOGGER.error("字符串加密异常");
            throw new RuntimeException(e);
        }
    }

    public static String decrypt(String cipher) {
        try {
            return decrypt(getKeyPair().getPrivate(), hexString2Bytes(cipher));
        } catch (Exception e) {
            LOGGER.error("字符串解密异常");
            throw new RuntimeException(e);
        }
    }

    public static UserPO getUserByToken(String token) {
        try {
            String tokenPlain = decrypt(getKeyPair().getPrivate(), hexString2Bytes(token));
            assert tokenPlain != null;
            String[] array = tokenPlain.split(" ");
            UserPO user = new UserPO();
            user.setId(Integer.parseInt(array[0]));
            user.setUserName(array[1]);
            return user;
        } catch (Exception e) {
            LOGGER.error("token解密异常");
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        String test = "12你1312好 32不%324&改变3g fbg啊";
        byte[] en_test = encrypt(getKeyPair().getPublic(), test);
        assert en_test != null;
        String cipherText = new BigInteger(1, en_test).toString(16);
        System.out.println(cipherText);
        String de_test = decrypt(getKeyPair().getPrivate(), en_test);
        System.out.println(de_test);
        de_test = decrypt(getKeyPair().getPrivate(), hexString2Bytes(cipherText));
        System.out.println(de_test);
    }

}
