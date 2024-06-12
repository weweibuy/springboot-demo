package org.example.springbootdemo.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.tomcat.util.buf.HexUtils;
import org.example.springbootdemo.model.eum.RSAKeyType;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @date 2024/6/1
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RSAUtils {

    private static final String PUBLIC_KEY = "PUBLIC KEY";

    private static final String PRIVATE_KEY = "PRIVATE KEY";

    private static final Pattern PEM_DATA = Pattern.compile("-----BEGIN (.*)-----(.*)-----END (.*)-----", Pattern.DOTALL);

    public static final String ALGORITHM = "RSA";

    public static final String SHA_256 = "SHA-256";

    public static final String SHA_1 = "SHA-256";

    public static final String MGF1 = "MGF1";

    public static final String DEFAULT_ALGORITHM_WITH_MODE_PADDING
            = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";


    public static PublicKey loadRSAX509PublicKey(String filePath) {
        String fileContent = ClassPathFileUtils.classPathFileContent(filePath);
        return (PublicKey) parseRSAKey(fileContent, RSAKeyType.PUBLIC_KEY);
    }


    public static PrivateKey loadRSAPKCS8PrivateKey(String filePath) {
        String fileContent = ClassPathFileUtils.classPathFileContent(filePath);
        return (PrivateKey) parseRSAKey(fileContent, RSAKeyType.PRIVATE_KEY);
    }


    /**
     * 加密后转 16进制
     *
     * @param key
     * @param data
     * @return
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws InvalidKeyException
     */
    public static String encryptToHex(PublicKey key, String data) throws InvalidKeyException {
        return encryptToHex(key, data.getBytes());
    }

    /**
     * 加密后转 16进制
     *
     * @param key
     * @param data
     * @return
     * @throws InvalidKeyException
     */
    public static String encryptToHex(PublicKey key, byte[] data) throws InvalidKeyException {
        return HexUtils.toHexString(encrypt(key, data));
    }

    /**
     * 加密
     *
     * @param key
     * @param data
     * @return
     * @throws InvalidKeyException
     */
    public static byte[] encrypt(Key key, byte[] data) throws InvalidKeyException {
        final Cipher cipher;
        try {
            cipher = Cipher.getInstance(DEFAULT_ALGORITHM_WITH_MODE_PADDING);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new IllegalStateException(e);
        }
        OAEPParameterSpec parameterSpec = new OAEPParameterSpec(SHA_256, MGF1,
                new MGF1ParameterSpec(SHA_1), PSource.PSpecified.DEFAULT);
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);
        } catch (InvalidAlgorithmParameterException e) {
            throw new IllegalStateException(e);
        }
        try {
            return cipher.doFinal(data);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new IllegalArgumentException("RSA加密参数错误", e);
        }
    }

    /**
     * 解密  16进制数据
     *
     * @param key
     * @param encryptedHex
     * @return
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws InvalidKeyException
     */
    public static String decryptHex(PrivateKey key, String encryptedHex) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        byte[] decrypt = decrypt(key, HexUtils.fromHexString(encryptedHex));
        return new String(decrypt);
    }

    /**
     * 解密
     *
     * @param key
     * @param encryptedData
     * @return
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static byte[] decrypt(Key key, byte[] encryptedData) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        final Cipher cipher;
        try {
            cipher = Cipher.getInstance(DEFAULT_ALGORITHM_WITH_MODE_PADDING);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new IllegalStateException(e);
        }
        OAEPParameterSpec parameterSpec = new OAEPParameterSpec(SHA_256, MGF1,
                new MGF1ParameterSpec(SHA_1), PSource.PSpecified.DEFAULT);
        try {
            cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);
        } catch (InvalidAlgorithmParameterException e) {
            throw new IllegalStateException(e);
        }
        return cipher.doFinal(encryptedData);
    }


    /**
     * 提取并解析密钥
     *
     * @param pemContent
     * @param rsaKeyType
     * @return
     */
    public static Key parseRSAKey(String pemContent, RSAKeyType rsaKeyType) {
        Matcher m = PEM_DATA.matcher(pemContent.trim());
        if (!m.matches()) {
            throw new IllegalArgumentException("秘钥格式不正确");
        }
        String type = m.group(1);

        String title = switch (rsaKeyType) {
            case PUBLIC_KEY -> PUBLIC_KEY;
            case PRIVATE_KEY -> PRIVATE_KEY;
        };

        if (!type.equals(title)) {
            throw new IllegalArgumentException(type + " 不是正确的秘钥格式");
        }

        // Base64.getMimeDecoder() 可以处理换行符
        // Base64.getDecoder() 适用于没有换行符的内容, 因此要提前处理换行符
        byte[] utf8Content = utf8Encode(m.group(2));

        byte[] content = Base64.getMimeDecoder().decode(utf8Content);

        try {
            return switch (rsaKeyType) {
                case PUBLIC_KEY -> getPublicKey(content);
                case PRIVATE_KEY -> getPrivateKey(content);
            };
        } catch (Exception e) {
            throw new IllegalArgumentException("无法解析RSA密钥");
        }

    }


    public static PublicKey getPublicKey(byte[] encryptedPublicKey) throws Exception {
        return KeyFactory.getInstance(RSAUtils.ALGORITHM)
                .generatePublic(new X509EncodedKeySpec(encryptedPublicKey));
    }

    public static PrivateKey getPrivateKey(byte[] encryptedPrivateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return KeyFactory.getInstance(RSAUtils.ALGORITHM)
                .generatePrivate(new PKCS8EncodedKeySpec(encryptedPrivateKey));
    }

    public static byte[] utf8Encode(String string) {
        return string.getBytes(StandardCharsets.UTF_8);
    }


}
