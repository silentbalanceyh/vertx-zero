package io.vertx.up.uca.crypto;

import io.aeon.experiment.channel.Pocket;
import io.aeon.experiment.specification.KPair;
import io.horizon.eon.VValue;
import io.horizon.specification.modeler.HED;
import io.horizon.specification.runtime.Macrocosm;
import io.vertx.up.eon.bridge.Strings;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
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

    @Override
    @SuppressWarnings("unchecked")
    public KPair generate(final int size) {
        return Fn.orJvm(() -> {
            final KeyPairGenerator generate = KeyPairGenerator.getInstance(VValue.DFT.ALGORITHM_RSA);
            generate.initialize(size);
            final KeyPair pair = generate.generateKeyPair();
            final P publicKey = (P) pair.getPublic();
            final V privateKey = (V) pair.getPrivate();
            // Base64 encoding
            final String publicKeyString = Base64.encodeBase64String(publicKey.getEncoded());
            final String privateKeyString = Base64.encodeBase64String(privateKey.getEncoded());
            return new KPair(publicKeyString, privateKeyString);
        });
    }

    // --------------- Child Method Inherit
    protected String runHED(final String source, final Function<HED, String> executor) {
        /*
         * Rapid Tool Usage for Z_HED environment part
         * 1. Set the Z_HED overwrite the default
         * 2. Extract the default ( jar -> Service Loader )
         * 3. Extract the app ( Classpath )
         */
        final String hedCls = Ut.envWith(Macrocosm.HED_COMPONENT, Strings.EMPTY);
        HED hed = null;


        // Z_HED
        if (Ut.notNil(hedCls)) {
            hed = Ut.instance(hedCls);
        }


        // META-INF/services/io.vertx.up.experiment.mixture.HED
        if (Objects.isNull(hed)) {
            hed = Pocket.lookup(HED.class);
        }
        final Annal logger = Annal.get(this.getClass());
        if (Objects.isNull(hed)) {
            logger.warn("[ HED ] Missed `HED` component in service loader: META-INF/services/{0}", HED.class.getName());
            return source;
        } else {
            logger.info("[ HED ] `HED` component: {0}", hed.getClass().getName());
        }
        return executor.apply(hed);
    }

    protected String runEncrypt(final String source, final Key key) {
        return Fn.orJvm(() -> {
            final Cipher cipher = Cipher.getInstance(this.algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return Base64.encodeBase64String(cipher.doFinal(source.getBytes()));
        }, source, key);
    }

    /*
     * RSA Fix:
     * javax.crypto.IllegalBlockSizeException: Data must not be longer than 128 bytes
     * at java.base/com.sun.crypto.provider.RSACipher.doFinal(RSACipher.java:348)
     */
    protected String runDecrypt(final String source, final Key key) {
        return Fn.orJvm(() -> {
            // RSA Decrypt
            final Cipher cipher = Cipher.getInstance(this.algorithm);
            cipher.init(Cipher.DECRYPT_MODE, key);

            final byte[] inputBytes = Base64.decodeBase64(source.getBytes(StandardCharsets.UTF_8));
            final int inputLength = inputBytes.length;
            // The Max Block Bytes of decrypt
            final int MAX_ENCRYPT_BLOCK = 128;
            int offSet = 0;
            byte[] resultBytes = {};
            byte[] cache = {};
            while (inputLength - offSet > 0) {
                if (inputLength - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(inputBytes, offSet, MAX_ENCRYPT_BLOCK);
                    offSet += MAX_ENCRYPT_BLOCK;
                } else {
                    cache = cipher.doFinal(inputBytes, offSet, inputLength - offSet);
                    offSet = inputLength;
                }
                resultBytes = Arrays.copyOf(resultBytes, resultBytes.length + cache.length);
                System.arraycopy(cache, 0, resultBytes, resultBytes.length - cache.length, cache.length);
            }
            return new String(resultBytes);
        }, source, key);
    }

    protected PublicKey x509(final String keyContent) {
        // Generate Public Key Object
        return Fn.orJvm(() -> {
            final byte[] buffer = Base64.decodeBase64(keyContent);
            final KeyFactory keyFactory = KeyFactory.getInstance(this.algorithm);
            final X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            return keyFactory.generatePublic(keySpec);
        });
    }

    protected PrivateKey pKCS8(final String keyContent) {
        // Generate Private Key Object
        return Fn.orJvm(() -> {
            final byte[] buffer = Base64.decodeBase64(keyContent);
            final KeyFactory keyFactory = KeyFactory.getInstance(this.algorithm);
            final PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            return keyFactory.generatePrivate(keySpec);
        });
    }
}
