package me.nvliu.management.utils;
/**
 * @Author:mvp
 * @ProjectName: management
 * @PacketName: me.nvliu.management.utils
 * @Description:
 * @Date: Create in 14:14 2018/9/28
 * @Modified By:
 */


import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jsoup.helper.StringUtil;

import javax.crypto.Cipher;
import java.io.File;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * RSA算法加密/解密工具类。
 */
public abstract class RSAUtil {

    /**
     * 算法名称
     */
    private static final String ALGORITHOM = "RSA";
    /**
     * 保存生成的密钥对的文件名称。
     */
//    private static final String RSA_PAIR_FILENAME = "/__RSA_PAIR.txt";
    /**
     * 密钥大小
     */
    private static final int KEY_SIZE = 65537;
    /**
     * 默认的安全服务提供者
     */
    private static final Provider DEFAULT_PROVIDER = new BouncyCastleProvider();

    private static KeyPairGenerator keyPairGen = null;
    private static KeyFactory keyFactory = null;
    /**
     * 缓存的密钥对。
     */
    private static KeyPair oneKeyPair = null;

    private static File rsaPairFile = null;

    static {
        try {
            keyPairGen = KeyPairGenerator.getInstance(ALGORITHOM, DEFAULT_PROVIDER);
            keyFactory = KeyFactory.getInstance(ALGORITHOM, DEFAULT_PROVIDER);
        } catch (NoSuchAlgorithmException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private RSAUtil() {
    }
    /**
     * 返回RSA密钥对。
     */
    public static KeyPair getKeyPair() {
        // 首先判断是否需要重新生成新的密钥对文件
        if (oneKeyPair != null) {
            return oneKeyPair;
        }else {
            return generateKeyPair();
        }
    }
    /**
     * 生成并返回RSA密钥对。
     */
    private static synchronized KeyPair generateKeyPair() {
        try {
            keyPairGen.initialize(KEY_SIZE, new SecureRandom(new SimpleDateFormat("yyyyMMdd").format(new Date()).getBytes()));
            oneKeyPair = keyPairGen.generateKeyPair();
            return oneKeyPair;
        } catch (InvalidParameterException ex) {
            System.out.println("KeyPairGenerator does not support a key length of " + KEY_SIZE + ".");
            ex.printStackTrace();
        } catch (NullPointerException ex) {
            System.out.println("RSAUtils#KEY_PAIR_GEN is null, can not generate KeyPairGenerator instance.");
            ex.printStackTrace();
        }
        return null;
    }



    /**
     * 根据给定的系数和专用指数构造一个RSA专用的公钥对象。
     *
     * @param modulus        系数。
     * @param publicExponent 专用指数。
     * @return RSA专用公钥对象。
     */
    public static RSAPublicKey generateRSAPublicKey(byte[] modulus, byte[] publicExponent) {
        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(new BigInteger(modulus),
                new BigInteger(publicExponent));
        try {
            return (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);
        } catch (InvalidKeySpecException ex) {
            System.out.println("RSAPublicKeySpec is unavailable.");
            ex.printStackTrace();
        } catch (NullPointerException ex) {
            System.out.println("RSAUtils#KEY_FACTORY is null, can not generate KeyFactory instance.");
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 根据给定的系数和专用指数构造一个RSA专用的私钥对象。
     *
     * @param modulus         系数。
     * @param privateExponent 专用指数。
     * @return RSA专用私钥对象。
     */
    public static RSAPrivateKey generateRSAPrivateKey(byte[] modulus, byte[] privateExponent) {
        RSAPrivateKeySpec privateKeySpec = new RSAPrivateKeySpec(new BigInteger(modulus),
                new BigInteger(privateExponent));
        try {
            return (RSAPrivateKey) keyFactory.generatePrivate(privateKeySpec);
        } catch (InvalidKeySpecException ex) {
            System.out.println("RSAPrivateKeySpec is unavailable.");
            ex.printStackTrace();
        } catch (NullPointerException ex) {
            System.out.println("RSAUtils#KEY_FACTORY is null, can not generate KeyFactory instance.");
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 根据给定的16进制系数和专用指数字符串构造一个RSA专用的私钥对象。
     *
     * @param hexModulus         系数。
     * @param hexPrivateExponent 专用指数。
     * @return RSA专用私钥对象。
     */
    public static RSAPrivateKey getRSAPrivateKey(String hexModulus, String hexPrivateExponent) {
        if (StringUtil.isBlank(hexModulus) || StringUtil.isBlank(hexPrivateExponent)) {
            System.out.println("hexModulus and hexPrivateExponent cannot be empty. RSAPrivateKey value is null to return.");
            return null;
        }
        byte[] modulus = null;
        byte[] privateExponent = null;
        try {
            modulus = HexUtil.decodeHex(hexModulus.toCharArray());
            privateExponent = HexUtil.decodeHex(hexPrivateExponent.toCharArray());
        } catch (Exception ex) {
            System.out.println("hexModulus or hexPrivateExponent value is invalid. return null(RSAPrivateKey).");
            ex.printStackTrace();
        }
        if (modulus != null && privateExponent != null) {
            return generateRSAPrivateKey(modulus, privateExponent);
        }
        return null;
    }

    /**
     * 根据给定的16进制系数和专用指数字符串构造一个RSA专用的公钥对象。
     *
     * @param hexModulus        系数。
     * @param hexPublicExponent 专用指数。
     * @return RSA专用公钥对象。
     */
    public static RSAPublicKey getRSAPublicKey(String hexModulus, String hexPublicExponent) {
        if (StringUtil.isBlank(hexModulus) || StringUtil.isBlank(hexPublicExponent)) {
            System.out.println("hexModulus and hexPublicExponent cannot be empty. return null(RSAPublicKey).");
            return null;
        }
        /*byte[] modulus = null;
        byte[] publicExponent = null;
        try {
            modulus = HexUtil.decodeHex(hexModulus.toCharArray());
            publicExponent = HexUtil.decodeHex(hexPublicExponent.toCharArray());
        } catch (Exception ex) {
            System.out.println("hexModulus or hexPublicExponent value is invalid. return null(RSAPublicKey).");
            ex.printStackTrace();
        }
        if (modulus != null && publicExponent != null) {
            return generateRSAPublicKey(modulus, publicExponent);
        }
        return null;*/
        BigInteger modulus = new BigInteger(hexModulus,16);
        BigInteger publicExponent = new BigInteger(hexPublicExponent,16);

        try {
            KeyFactory k = KeyFactory.getInstance(ALGORITHOM);
            RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(modulus,publicExponent);
            return (RSAPublicKey) k.generatePublic(publicKeySpec);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 使用指定的公钥加密数据。
     *
     * @param publicKey 给定的公钥。
     * @param data      要加密的数据。
     * @return 加密后的数据。
     */
    public static byte[] encrypt(PublicKey publicKey, byte[] data) throws Exception {
        Cipher ci = Cipher.getInstance(ALGORITHOM, DEFAULT_PROVIDER);
        ci.init(Cipher.ENCRYPT_MODE, publicKey);
        return ci.doFinal(data);
    }
    public static byte[] encrypt1(PublicKey publicKey, byte[] data) throws Exception {
        Cipher ci = Cipher.getInstance(ALGORITHOM);
        ci.init(Cipher.ENCRYPT_MODE, publicKey);
        return ci.doFinal(data);
    }

    /**
     * 使用指定的私钥解密数据。
     *
     * @param privateKey 给定的私钥。
     * @param data       要解密的数据。
     * @return 原数据。
     */
    public static byte[] decrypt(PrivateKey privateKey, byte[] data) throws Exception {
        Cipher ci = Cipher.getInstance(ALGORITHOM, DEFAULT_PROVIDER);
        ci.init(Cipher.DECRYPT_MODE, privateKey);
        return ci.doFinal(data);
    }

    /**
     * 使用给定的公钥加密给定的字符串。
     * <p/>
     * 若 {@code publicKey} 为 {@code null}，或者 {@code plaintext} 为 {@code null} 则返回 {@code
     * null}。
     *
     * @param publicKey 给定的公钥。
     * @param plaintext 字符串。
     * @return 给定字符串的密文。
     */
    public static String encryptString(PublicKey publicKey, String plaintext) {
        if (publicKey == null || plaintext == null) {
            return null;
        }
        byte[] data = plaintext.getBytes();
        try {
            byte[] en_data = encrypt(publicKey, data);
            return new String(HexUtil.encodeHex(en_data));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 使用默认的公钥加密给定的字符串。
     * <p/>
     * 若{@code plaintext} 为 {@code null} 则返回 {@code null}。
     *
     * @param plaintext 字符串。
     * @return 给定字符串的密文。
     */
    public static String encryptString(String plaintext) {
        if (plaintext == null) {
            return null;
        }
        byte[] data = plaintext.getBytes();
        KeyPair keyPair = getKeyPair();
        try {
            byte[] en_data = encrypt((RSAPublicKey) keyPair.getPublic(), data);
            return new String(HexUtil.encodeHex(en_data));
        } catch (NullPointerException ex) {
            System.out.println("keyPair cannot be null.");
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 生成由JS的RSA加密的字符串。
     *
     * @param publicKey 公钥
     * @param plaintext 原文字符串
     * @return 加密后的字符串
     */
    /*public static String encryptStringByJs(PublicKey publicKey, String plaintext) {
        if (plaintext == null) {
            return null;
        }
        String text = encryptString(publicKey, StringUtil.reverse(plaintext));

        return text;
    }*/

    /**
     * 用默认公钥生成由JS的RSA加密的字符串。
     *
     * @param publicKey 公钥
     * @param plaintext 原文字符串
     * @return 加密后的字符串
     */
    /*public static String encryptStringByJs(String plaintext) {
        if (plaintext == null) {
            return null;
        }
        String text = encryptString(StringUtil.reverse(plaintext));

        return text;
    }*/

    /**
     * 使用给定的私钥解密给定的字符串。
     * <p/>
     * 若私钥为 {@code null}，或者 {@code encrypttext} 为 {@code null}或空字符串则返回 {@code null}。
     * 私钥不匹配时，返回 {@code null}。
     *
     * @param privateKey  给定的私钥。
     * @param encrypttext 密文。
     * @return 原文字符串。
     */
    public static String decryptString(PrivateKey privateKey, String encrypttext) {
        if (privateKey == null || StringUtil.isBlank(encrypttext)) {
            return null;
        }
        try {
            byte[] en_data = HexUtil.decodeHex(encrypttext.toCharArray());
            byte[] data = decrypt(privateKey, en_data);
            return new String(data);
        } catch (Exception ex) {
            System.out.println(String.format("\"%s\" Decryption failed. Cause: %s", encrypttext, ex.getCause().getMessage()));

        }
        return null;
    }

    /**
     * 使用默认的私钥解密给定的字符串。
     * <p/>
     * 若{@code encrypttext} 为 {@code null}或空字符串则返回 {@code null}。
     * 私钥不匹配时，返回 {@code null}。
     *
     * @param encrypttext 密文。
     * @return 原文字符串。
     */
    public static String decryptString(String encrypttext) {
        if (StringUtil.isBlank(encrypttext)) {
            return null;
        }
        KeyPair keyPair = getKeyPair();
        try {
            byte[] en_data = HexUtil.decodeHex(encrypttext.toCharArray());
            byte[] data = decrypt((RSAPrivateKey) keyPair.getPrivate(), en_data);
            return new String(data);
        } catch (NullPointerException ex) {
            System.out.println("keyPair cannot be null.");
            ex.printStackTrace();
        } catch (Exception ex) {
            System.out.println(String.format("\"%s\" Decryption failed. Cause: %s", encrypttext, ex.getMessage()));
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 使用指定的私钥解密由JS加密的字符串。
     *
     * @param privateKey  私钥
     * @param encrypttext 密文
     * @return {@code encrypttext} 的原文字符串
     */
    /*public static String decryptStringByJs(PrivateKey privateKey, String encrypttext) {
        String text = decryptString(privateKey, encrypttext);
        if (text == null) {
            return null;
        }
        return StringUtil.reverse(text);
    }*/

    /**
     * 使用默认的私钥解密由JS加密（使用此类提供的公钥加密）的字符串。
     *
     * @param encrypttext 密文。
     * @return {@code encrypttext} 的原文字符串。
     */
    /*public static String decryptStringByJs(String encrypttext) {
        String text = decryptString(encrypttext);
        if (text == null) {
            return null;
        }
        return StringUtil.reverse(text);
    }*/

    /**
     * 返回已初始化的默认的公钥。
     */
    public static RSAPublicKey getDefaultPublicKey() {
        KeyPair keyPair = getKeyPair();
        if (keyPair != null) {
            return (RSAPublicKey) keyPair.getPublic();
        }
        return null;
    }

    /**
     * 返回已初始化的默认的私钥。
     */
    public static RSAPrivateKey getDefaultPrivateKey() {
        KeyPair keyPair = getKeyPair();
        if (keyPair != null) {
            return (RSAPrivateKey) keyPair.getPrivate();
        }
        return null;
    }


}
