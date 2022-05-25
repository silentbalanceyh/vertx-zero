package io.vertx.up.uca.crypto;

import io.vertx.up.experiment.channel.Pocket;
import io.vertx.up.experiment.mixture.HED;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;
import org.apache.commons.codec.binary.Base64;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Objects;
import java.util.function.Function;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractED<P extends PublicKey, V extends PrivateKey> implements ED {
    protected String algorithm;

    public AbstractED(final String algorithm) {
        this.algorithm = algorithm;
    }

    // --------------- Public Key Building
    @Override
    public String encryptIO(final String source, final String keyPath) {
        // 1. Loading Public Key String by Given Path
        final String keyContent = Ut.ioString(keyPath);
        // 2. Encrypt by Public Key Content
        return this.encrypt(source, keyContent);
    }

    @Override
    public String decryptIO(final String source, final String keyPath) {
        // 1. Loading Private Key String by Given Path
        final String keyContent = Ut.ioString(keyPath);
        // 2. Decrypt by Private Key Content
        return this.decrypt(source, keyContent);
    }

    // --------------- Child Method Inherit
    protected String runHED(final String source, final Function<HED, String> executor) {
        final HED hed = Pocket.lookup(HED.class);
        if (Objects.isNull(hed)) {
            final Annal logger = Annal.get(this.getClass());
            logger.warn("[ HED ] Missed `HED` component in service loader: META-INF/services/{0}", HED.class.getName());
            return source;
        }
        return executor.apply(hed);
    }

    protected PublicKey x509Public(final String keyContent) {
        // Generate Public Key Object
        final byte[] buffer = Base64.decodeBase64(keyContent);
        final KeyFactory keyFactory = KeyFactory
    }
}
