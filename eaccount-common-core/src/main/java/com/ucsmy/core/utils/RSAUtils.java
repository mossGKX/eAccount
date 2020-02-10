package com.ucsmy.core.utils;

import com.ucsmy.core.vo.RetMsg;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.Base64Utils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.crypto.Cipher;
import javax.servlet.http.HttpSession;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * Utils - RSA加密解密
 *
 * @author Hengtiansoft Team
 * @version 1.0_beta
 */
public final class RSAUtils {

	private static final String PUBLICKEY_EMEPTY = "公钥不能为空";
	private static final String PRIVATEKEY_EMEPTY = "私钥不能为空";
	private static final String DATA_EMPTY = "数据不能为空";

	/** 安全服务提供者. */
	private static final Provider PROVIDER = new BouncyCastleProvider();
	private static final String ALGORITHM = "RSA";

	private static final String KEY_ATTRIBUTE_NAME = "rsaKey";
	private static Logger log = LoggerFactory.getLogger(RSAUtils.class);

	/** 密钥大小. */
	private static final int KEY_SIZE = 1024;

	/**
	 * 不可实例化.
	 */
	private RSAUtils() {
	}

	public static Map<String, String> getRsaPubKeyBySession() {
		HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
		return getRsaPubKeyBySession(session);
	}

	public static Map<String, String> getRsaPubKeyBySession(HttpSession session) {
		Map<String, String> data = new HashMap<>();
		KeyPair keyPair = (KeyPair) session.getAttribute(KEY_ATTRIBUTE_NAME);
		if(keyPair == null) {
			keyPair = RSAUtils.generateKeyPair();
			session.setAttribute(KEY_ATTRIBUTE_NAME, keyPair);
		}
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		data.put("modulus", Base64Utils.encodeToString(publicKey.getModulus().toByteArray()));
		data.put("exponent", Base64Utils.encodeToString(publicKey.getPublicExponent().toByteArray()));
		return data;
	}

	public static RetMsg<String> decryptBySession(String text) {
		HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
		return decryptBySession(text, session);
	}

	public static RetMsg<String> decryptBySession(String text, HttpSession session) {
		KeyPair keyPair = (KeyPair) session.getAttribute(KEY_ATTRIBUTE_NAME);
		if (keyPair == null) {
			return RetMsg.error("密钥不存在");
		}
		return RetMsg.success("", decrypt(keyPair.getPrivate(), text));
	}

	/**
	 * 生成密钥对.
	 *
	 * @return 密钥对
	 */
	public static KeyPair generateKeyPair() {
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM, PROVIDER);
			keyPairGenerator.initialize(KEY_SIZE, new SecureRandom());
			return keyPairGenerator.generateKeyPair();
		} catch (NoSuchAlgorithmException e) {
			log.error(e.getMessage());
			return null;
		}
	}

	/**
	 * 加密.
	 *
	 * @param publicKey
	 *            公钥
	 * @param data
	 *            数据
	 * @return 加密后的数据
	 */
	public static byte[] encrypt(PublicKey publicKey, byte[] data) {
		Assert.notNull(publicKey, PUBLICKEY_EMEPTY);
		Assert.notNull(data, DATA_EMPTY);
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM, PROVIDER);
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			return cipher.doFinal(data);
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}
	}

	/**
	 * 加密.
	 *
	 * @param publicKey
	 *            公钥
	 * @param text
	 *            字符串
	 *
	 * @return Base64编码字符串
	 */
	public static String encrypt(PublicKey publicKey, String text) {
		Assert.notNull(publicKey, PUBLICKEY_EMEPTY);
		Assert.notNull(text, DATA_EMPTY);
		byte[] data = encrypt(publicKey, text.getBytes());
		return data != null ? Base64Utils.encodeToString(data) : null;
	}

	/**
	 * 解密.
	 *
	 * @param privateKey
	 *            私钥
	 * @param data
	 *            数据
	 * @return 解密后的数据
	 */
	public static byte[] decrypt(PrivateKey privateKey, byte[] data) {
		Assert.notNull(privateKey, PRIVATEKEY_EMEPTY);
		Assert.notNull(data, DATA_EMPTY);
		try {
			Cipher cipher = Cipher
					.getInstance("RSA/ECB/PKCS1Padding", PROVIDER);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			return cipher.doFinal(data);
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}
	}

	/**
	 * 解密.
	 *
	 * @param privateKey
	 *            私钥
	 * @param text
	 *            Base64编码字符串
	 * @return 解密后的数据
	 */
	public static String decrypt(PrivateKey privateKey, String text) {
		Assert.notNull(privateKey, PRIVATEKEY_EMEPTY);
		Assert.notNull(text, DATA_EMPTY);
		byte[] data = decrypt(privateKey, Base64Utils.decodeFromString(text));
		return data != null ? new String(data) : null;
	}

	/**
	 * 解密
	 *
	 * @param privateKey Base64加密的私钥
	 * @param text 待解密内容
	 * @return
	 */
	public static String decrypt(byte[] privateKey, String text) {
        Assert.notNull(privateKey, PRIVATEKEY_EMEPTY);
        Assert.notNull(text, DATA_EMPTY);
        try {
            byte[] keyBytes = Base64Utils.decode(privateKey);
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            PrivateKey key = keyFactory.generatePrivate(pkcs8KeySpec);
            byte[] data = decrypt(key, Base64Utils.decodeFromString(text));
            return data != null ? new String(data) : null;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
	}
}
