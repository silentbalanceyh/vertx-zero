package io.vertx.up.uca.crypto;

/**
 * Encrypt / Decrypt Interface for
 *
 * Mode 1: ( Default )
 * 1) encrypt by Public Key
 * 2) decrypt by Private Key
 * Mode 2: ( Support in Future )
 * 1) encrypt by Private Key
 * 2) decrypt by Public Key
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface ED {

    String encrypt(String source);

    String encrypt(String source, String keyContent);

    String encryptIO(String source, String keyPath);

    String decrypt(String source);

    String decrypt(String source, String keyContent);

    String decryptIO(String source, String keyPath);
}
