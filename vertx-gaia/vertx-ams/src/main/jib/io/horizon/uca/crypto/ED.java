package io.horizon.uca.crypto;

import io.horizon.annotations.Memory;
import io.horizon.atom.common.KPair;
import io.horizon.uca.cache.Cc;

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


    static ED rsa(final boolean p2v) {
        if (p2v) {
            // Public -> Private
            return CACHE.CCT_ED.pick(EDPVRsa::new, EDPVRsa.class.getName());
        } else {
            // Private -> Public
            return CACHE.CCT_ED.pick(EDVPRsa::new, EDVPRsa.class.getName());
        }
    }

    KPair generate(int size);

    String encrypt(String source);

    String encrypt(String source, String keyContent);

    String decrypt(String source);

    String decrypt(String source, String keyContent);
}

interface CACHE {
    /**
     * ED 加密解密组件专用缓存池
     */
    @Memory(ED.class)
    Cc<String, ED> CCT_ED = Cc.openThread();
}
