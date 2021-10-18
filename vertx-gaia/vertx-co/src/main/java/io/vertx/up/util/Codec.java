package io.vertx.up.util;

import io.vertx.up.eon.Values;
import io.vertx.up.fn.Fn;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Base64;

final class Codec {
    private Codec() {
    }

    /**
     * MD5 encript for input string.
     *
     * @param input input string that will be encoded
     *
     * @return The encoded string with MD5
     */
    static String md5(final String input) {
        return Fn.getJvm(() -> {
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            final byte[] source = input.getBytes(Values.DEFAULT_CHARSET);
            digest.update(source);
            final byte[] middle = digest.digest();
            final char[] middleStr = new char[16 * 2];
            int position = 0;
            for (int idx = 0; idx < 16; idx++) {
                final byte byte0 = middle[idx];
                middleStr[position++] = Values.HEX_ARR[byte0 >>> 4 & 0xF];
                middleStr[position++] = Values.HEX_ARR[byte0 & 0xF];
            }
            return new String(middleStr);
        }, input);
    }

    /**
     * SHA-256
     *
     * @param input input string that will be encoded
     *
     * @return The encoded string with sha256
     */
    static String sha256(final String input) {
        return sha(input, "SHA-256");
    }

    /**
     * SHA-512
     *
     * @param input input string that will be encoded
     *
     * @return The encoded string with sha512
     */
    static String sha512(final String input) {
        return sha(input, "SHA-512");
    }

    @SuppressWarnings("all")
    private static String sha(final String strText, final String strType) {
        return Fn.getJvm(() -> {
            final MessageDigest messageDigest = MessageDigest.getInstance(strType);
            messageDigest.update(strText.getBytes());
            final byte[] byteBuffer = messageDigest.digest();
            final StringBuilder strHexString = new StringBuilder();
            for (int i = 0; i < byteBuffer.length; i++) {
                final String hex = Integer.toHexString(0xff & byteBuffer[i]);
                if (hex.length() == 1) {
                    strHexString.append('0' );
                }
                strHexString.append(hex);
            }
            return strHexString.toString();
        }, strText, strType);
    }

    /**
     * Base-64
     */
    static String base64(final String input, final boolean encript) {
        return Fn.getNull(null, () -> {
            if (encript) {
                return Base64.getEncoder().encodeToString(input.getBytes());
            } else {
                return new String(Base64.getDecoder().decode(input.getBytes()), Values.DEFAULT_CHARSET);
            }
        }, input);
    }

    static String url(final String input, final boolean encript) {
        return Fn.getJvm(null, () -> {
            if (encript) {
                return URLEncoder.encode(input, Values.ENCODING);
            } else {
                return URLDecoder.decode(input, Values.ENCODING);
            }
        }, input);
    }
}
