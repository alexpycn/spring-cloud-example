package cn.alexpy.common.utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.Security;

/**
 * @author Alexpy
 */
public class AESUtil {
    /**
     * AES128 算法
     * <p>
     * CBC 模式
     * <p>
     * PKCS7Padding 填充模式
     * <p>
     * CBC模式需要添加偏移量参数iv，必须16位
     * 密钥 sessionKey，必须16位
     * <p>
     * 介于java 不支持PKCS7Padding，只支持PKCS5Padding 但是PKCS7Padding 和 PKCS5Padding 没有什么区别
     * 要实现在java端用PKCS7Padding填充，需要用到bouncycastle组件来实现
     */
    private static final String SESSION_KEY = "0102030405060708";
    // 偏移量 16位
    private static final String OFFSET = "0102030405060708";
    // 算法名称
    private static final String KEY_ALGORITHM = "AES";
    // 加解密算法/模式/填充方式
    private static final String AES_TYPE = "AES/CBC/PKCS7Padding";
    // 加解密 密钥 16位

    private static byte[] OFFSET_BYTE;
    private static byte[] KEY_BYTES;
    private static Key KEY;
    private static Cipher CIPHER;

    private static void init() {
        // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
        KEY_BYTES = SESSION_KEY.getBytes();
        OFFSET_BYTE = OFFSET.getBytes();
        // 初始化
        Security.addProvider(new BouncyCastleProvider());
        // 转化成JAVA的密钥格式
        KEY = new SecretKeySpec(KEY_BYTES, KEY_ALGORITHM);
        try {
            // 初始化cipher
            CIPHER = Cipher.getInstance(AES_TYPE, "BC");
        } catch (Exception ignored) {
        }
    }

    /**
     * 加密方法
     *
     * @param content 要加密的字符串
     */
    public static String encrypt(String content) {
        byte[] encryptedText = null;
        byte[] contentByte = content.getBytes();
        init();
        try {
            CIPHER.init(Cipher.ENCRYPT_MODE, KEY, new IvParameterSpec(OFFSET_BYTE));
            encryptedText = CIPHER.doFinal(contentByte);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert encryptedText != null;
        return new String(Hex.encode(encryptedText));
    }

    /**
     * 解密方法
     *
     * @param encryptedData 要解密的字符串
     */
    public static String decrypt(String encryptedData) {
        byte[] encryptedText = null;
        byte[] encryptedDataByte = Hex.decode(encryptedData);
        init();
        try {
            CIPHER.init(Cipher.DECRYPT_MODE, KEY, new IvParameterSpec(OFFSET_BYTE));
            encryptedText = CIPHER.doFinal(encryptedDataByte);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert encryptedText != null;
        return new String(encryptedText);
    }

//    public static void main(String[] args) {
//
//        //加密字符串
//        String content = "1=1";
//        System.out.println("加密前的：" + content);
//
//        // 加密方法
//        String enc = AESUtil.encrypt(content);
//        System.out.println("加密后的内容：" + enc);
//
//        // 解密方法
//        String dec = AESUtil.decrypt("37249390F8F13D7D43246419D98C334A");
//        System.out.println("解密后的内容：" + dec);
//    }
}