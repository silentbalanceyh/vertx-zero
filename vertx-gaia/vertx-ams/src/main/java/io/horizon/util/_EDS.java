package io.horizon.util;

import io.horizon.uca.crypto.ED;
import io.vertx.core.json.JsonObject;

/**
 * @author lang : 2023/4/28
 */
class _EDS extends _Compare {
    protected _EDS() {
    }

    /**
     * MD5加密算法（全大写）
     *
     * @param input 输入
     *
     * @return 加密后的字符串
     */
    public static String encryptMD5(final String input) {
        return HCrypto.md5(input);
    }

    /**
     * SHA256加密算法
     *
     * @param input 输入
     *
     * @return 加密后的字符串
     */
    public static String encryptSHA256(final String input) {
        return HCrypto.sha256(input);
    }

    /**
     * SHA512加密算法
     *
     * @param input 输入
     *
     * @return 加密后的字符串
     */
    public static String encryptSHA512(final String input) {
        return HCrypto.sha512(input);
    }

    /**
     * Base64加密算法
     *
     * @param input 输入
     *
     * @return 加密后的字符串
     */
    public static String encryptBase64(final String input) {
        return HCrypto.base64(input, true);
    }

    /**
     * Base64解密算法
     *
     * @param input 输入
     *
     * @return 加密后的字符串
     */
    public static String decryptBase64(final String input) {
        return HCrypto.base64(input, false);
    }

    /**
     * URL Encoding 专用加密
     *
     * @param input 输入
     *
     * @return 加密后的字符串
     */
    public static String encryptUrl(final String input) {
        return HCrypto.url(input, true);
    }

    /**
     * URL Decoding 专用解密
     *
     * @param input 输入
     *
     * @return 加密后的字符串
     */
    public static String encryptUrl(final JsonObject input) {
        final JsonObject sure = HJson.valueJObject(input, false);
        return HCrypto.url(sure.encode(), true);
    }

    /**
     * URL Decoding 专用解密
     *
     * @param input 输入
     *
     * @return 加密后的字符串
     */
    public static String decryptUrl(final String input) {
        return HCrypto.url(input, false);
    }

    /**
     * 公钥加密 RSA 加密算法
     *
     * @param input     输入
     * @param keyPublic 公钥内容
     *
     * @return 加密后的字符串
     */
    public static String encryptRSAP(final String input, final String keyPublic) {
        return ED.rsa(true).encrypt(input, keyPublic);
    }

    /**
     * 公钥加密 RSA 加密算法 / 此处调用HED模块提取公钥
     *
     * @param input 输入
     *
     * @return 加密后的字符串
     */
    public static String encryptRSAP(final String input) {
        return ED.rsa(true).encrypt(input);
    }

    /**
     * 私钥解密 RSA 解密算法
     *
     * @param input      输入
     * @param keyPrivate 私钥内容
     *
     * @return 解密后的字符串
     */
    public static String decryptRSAV(final String input, final String keyPrivate) {
        return ED.rsa(true).decrypt(input, keyPrivate);
    }

    /**
     * 私钥解密 RSA 解密算法 / 此处调用HED模块提取私钥
     *
     * @param input 输入
     *
     * @return 解密后的字符串
     */
    public static String decryptRSAV(final String input) {
        return ED.rsa(true).decrypt(input);
    }

    /**
     * 私钥加密 RSA 加密算法
     *
     * @param input      输入
     * @param keyPrivate 私钥内容
     *
     * @return 加密后的字符串
     */
    public static String encryptRSAV(final String input, final String keyPrivate) {
        return ED.rsa(false).encrypt(input, keyPrivate);
    }

    /**
     * 私钥加密 RSA 加密算法 / 此处调用HED模块提取私钥
     *
     * @param input 输入
     *
     * @return 加密后的字符串
     */
    public static String encryptRSAV(final String input) {
        return ED.rsa(false).encrypt(input);
    }

    /**
     * 公钥解密 RSA 解密算法
     *
     * @param input     输入
     * @param keyPublic 公钥内容
     *
     * @return 解密后的字符串
     */
    public static String decryptRSAP(final String input, final String keyPublic) {
        return ED.rsa(false).decrypt(input, keyPublic);
    }

    /**
     * 公钥解密 RSA 解密算法 / 此处调用HED模块提取公钥
     *
     * @param input 输入
     *
     * @return 解密后的字符串
     */
    public static String decryptRSAP(final String input) {
        return ED.rsa(false).decrypt(input);
    }

}
