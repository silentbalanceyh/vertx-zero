package io.vertx.up.uca.crypto;

import io.vertx.up.experiment.channel.Pocket;
import io.vertx.up.experiment.mixture.HED;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Objects;
import java.util.function.Function;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractED implements ED {
    protected String algorithm;

    public AbstractED(final String algorithm) {
        this.algorithm = algorithm;
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

    protected String runFinal(final String source, final int mode, final Key key) {
        return Fn.getJvm(() -> {
            final Cipher cipher = Cipher.getInstance(this.algorithm);
            cipher.init(mode, key);
            return Base64.encodeBase64String(cipher.doFinal(source.getBytes()));
        }, source, key);
    }

    protected PublicKey x509(final String keyContent) {
        // Generate Public Key Object
        return Fn.getJvm(() -> {
            final byte[] buffer = Base64.decodeBase64(keyContent);
            final KeyFactory keyFactory = KeyFactory.getInstance(this.algorithm);
            final X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            return keyFactory.generatePublic(keySpec);
        });
    }

    protected PrivateKey pKCS8(final String keyContent) {
        // Generate Private Key Object
        return Fn.getJvm(() -> {
            final byte[] buffer = Base64.decodeBase64(keyContent);
            final KeyFactory keyFactory = KeyFactory.getInstance(this.algorithm);
            final PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            return keyFactory.generatePrivate(keySpec);
        });
    }
}
