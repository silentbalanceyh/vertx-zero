package io.vertx.up.uca.crypto;

import io.vertx.up.uca.cache.Cc;

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

    Cc<String, ED> CC_ED = Cc.openThread();

    static ED rsa(final boolean p2v) {
        if (p2v) {
            // Public -> Private
            return CC_ED.pick(EDPVRsa::new, EDPVRsa.class.getName());
        } else {
            // Private -> Public
            return CC_ED.pick(EDVPRsa::new, EDVPRsa.class.getName());
        }
    }

    String encrypt(String source);

    String encrypt(String source, String keyContent);

    String decrypt(String source);

    String decrypt(String source, String keyContent);
}
