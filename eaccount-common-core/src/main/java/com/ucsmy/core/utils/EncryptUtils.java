package com.ucsmy.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加密工具类
 * Created by 钟廷员 on 2016/9/13.
 */
public class EncryptUtils {
    private static Logger log = LoggerFactory.getLogger(EncryptUtils.class);
    /**
     * MD5加密
     * @param info 加密字符串
     * @return 返回不可逆的32位小写加密字符串
     */
    public static String MD5(final String info) {
        return encryption(info, "MD5", null);
    }

    /**
     * SHA-256加密
     * @param info 加密字符串
     * @return 返回不可逆的64位小写加密字符串
     */
    public static String SHA256(final String info) {
        return encryption(info, "SHA-256", null);
    }

    /**
     * SHA-512加密
     * @param info 加密字符串
     * @return 返回不可逆的128位小写加密字符串
     */
    public static String SHA512(final String info) {
        return encryption(info, "SHA-512", null);
    }

    private static String encryption(final String info, final String type, String charsetName) {
        try {
            // 创建加密对象 并傳入加密類型
            MessageDigest messageDigest = MessageDigest.getInstance(type);
            messageDigest.update(info.getBytes(charsetName == null ? "UTF-8" : charsetName));
            byte[] encryption = messageDigest.digest();

            StringBuilder strBuf = new StringBuilder();
            for (int i = 0; i < encryption.length; i++) {
                if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
                    strBuf.append("0");
                }
                strBuf.append(Integer.toHexString(0xff & encryption[i]));
            }
            return strBuf.toString();
        } catch (NoSuchAlgorithmException e) {
            log.error("不存在\"" + type + "\"该加密方法", e);
            return null;
        } catch (UnsupportedEncodingException e) {
            log.error("转码错误：" + charsetName, e);
            return null;
        }
    }
}
