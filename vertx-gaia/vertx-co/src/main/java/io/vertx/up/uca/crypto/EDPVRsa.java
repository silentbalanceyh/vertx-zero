package io.vertx.up.uca.crypto;

import io.vertx.up.eon.Constants;
import io.vertx.up.experiment.specification.KPair;

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
        super(Constants.ALGORITHM_RSA);
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
        return null;
    }

    @Override
    public String decrypt(final String source, final String keyContent) {
        return null;
    }
}
