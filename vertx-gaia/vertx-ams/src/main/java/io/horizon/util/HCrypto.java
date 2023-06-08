package io.horizon.util;

import io.horizon.eon.VValue;
import io.horizon.fn.HFn;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

final class HCrypto {
    private HCrypto() {
    }

    static String md5(final String input) {
        return HFn.failOr(() -> {
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            final byte[] source = input.getBytes(io.horizon.eon.VValue.DFT.CHARSET);
            digest.update(source);
            final byte[] middle = digest.digest();
            final char[] middleStr = new char[16 * 2];
            int position = 0;
            for (int idx = 0; idx < 16; idx++) {
                final byte byte0 = middle[idx];
                middleStr[position++] = VValue.ARR_HEX[byte0 >>> 4 & 0xF];
                middleStr[position++] = VValue.ARR_HEX[byte0 & 0xF];
            }
            return new String(middleStr);
        }, input);
    }

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
        return HFn.failOr(() -> {
            final MessageDigest messageDigest = MessageDigest.getInstance(strType);
            messageDigest.update(strText.getBytes());
            final byte[] byteBuffer = messageDigest.digest();
            final StringBuilder strHexString = new StringBuilder();
            for (int i = 0; i < byteBuffer.length; i++) {
                final String hex = Integer.toHexString(0xff & byteBuffer[i]);
                if (hex.length() == 1) {
                    strHexString.append('0');
                }
                strHexString.append(hex);
            }
            return strHexString.toString();
        }, strText, strType);
    }

    static String base64(final String input, final boolean encript) {
        return HFn.runOr(() -> {
            if (encript) {
                return Base64.getEncoder().encodeToString(input.getBytes());
            } else {
                return new String(Base64.getDecoder().decode(input.getBytes()), io.horizon.eon.VValue.DFT.CHARSET);
            }
        }, input);
    }

    static String url(final String input, final boolean encript) {
        return HFn.failOr(() -> {
            if (encript) {
                return URLEncoder.encode(input, StandardCharsets.UTF_8);
            } else {
                return URLDecoder.decode(input, StandardCharsets.UTF_8);
            }
        }, input);
    }
}
