package io.horizon.util;

import io.vertx.core.json.JsonObject;

/**
 * @author lang : 2023/4/28
 */
class _EDS extends _Color {
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
}
