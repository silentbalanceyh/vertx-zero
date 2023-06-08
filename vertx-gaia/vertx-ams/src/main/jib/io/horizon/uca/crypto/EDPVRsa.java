package io.horizon.uca.crypto;

import io.horizon.atom.common.KPair;
import io.horizon.eon.VValue;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * 1. Encrypt by RSAPublicKey
 * 2. Decrypt by RSAPrivateKey
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class EDPVRsa extends AbstractED<RSAPublicKey, RSAPrivateKey> {

    public EDPVRsa() {
        super(VValue.DFT.ALGORITHM_RSA);
    }

    @Override
    public String encrypt(final String source) {
        return this.runHED(source, hed -> {
            final KPair pair = hed.loadRSA();
            return this.encrypt(source, pair.getPublicKey());
        });
    }

    @Override
    public String decrypt(final String source) {
        return this.runHED(source, hed -> {
            final KPair pair = hed.loadRSA();
            return this.decrypt(source, pair.getPrivateKey());
        });
    }

    @Override
    public String encrypt(final String source, final String keyContent) {
        // Get Public Key
        final RSAPublicKey publicKey = (RSAPublicKey) this.x509(keyContent);
        // Encrypt Code Logical
        return this.runEncrypt(source, publicKey);
    }

    @Override
    public String decrypt(final String source, final String keyContent) {
        // Get Private Key
        final RSAPrivateKey privateKey = (RSAPrivateKey) this.pKCS8(keyContent);
        // Encrypt Code Logical
        return this.runDecrypt(source, privateKey);
    }
}
