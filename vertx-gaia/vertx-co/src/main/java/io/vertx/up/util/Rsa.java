package io.vertx.up.util;

import io.vertx.up.fn.Fn;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;

class Rsa {
    private Rsa() {
    }

    /**
     * rsa encript for input string.
     *
     * @param strText input string that will be encoded
     *
     * @return The encoded string with rsa
     */
    static String encrypt(final String strText, final String keyPath) {
        return Fn.getJvm(() ->
            encrypt(strText, loadRSAPublicKeyByFile(keyPath)), strText);
    }

    static String encrypt(final String strText, final RSAPublicKey publicKey) {
        return Fn.getJvm(() -> {
            final Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return Base64.encodeBase64String(cipher.doFinal(strText.getBytes()));
        }, strText, publicKey);
    }

    private static RSAPublicKey loadRSAPublicKeyByFile(final String keyPath)
        throws Exception {
        // 1. loading Public Key string by given path
        final String publicKeyStr = IO.getString(keyPath);
        //2. generate Public Key Object
        final byte[] buffer = Base64.decodeBase64(publicKeyStr);
        final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        final X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }
}
