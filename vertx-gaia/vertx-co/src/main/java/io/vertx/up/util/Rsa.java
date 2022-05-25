package io.vertx.up.util;

import io.vertx.up.eon.Constants;
import io.vertx.up.experiment.channel.Pocket;
import io.vertx.up.experiment.mixture.HED;
import io.vertx.up.experiment.specification.KPair;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Objects;
import java.util.function.Function;

@SuppressWarnings("all")
class Rsa {
    private static final Annal LOGGER = Annal.get(Rsa.class);

    private Rsa() {
    }

    // -------------- Encrypt Public / Decrypt Private --------------
    /*
     * For HED Encrypt/Decrypt Component for
     *
     * - Runtime:
     * 1) Database Password
     * 2) Neo4j Password
     * 3) Elastic Password
     * 4) Workflow Password
     *
     * - Configure:
     * 1) Liquibase Password
     *
     * Because Jooq often stored in `script/code-up` part, it means that these all configuration will
     * be effect in development only, after you have developed in runtime, all the configuration files
     * will not put into packaged jar, in this kind of situation the jooq-gen tool configuration could
     * be ignored.
     *
     * The Key Point is liquibase password encrypt/decrypt process here is very difficult to execute
     * our code, but this extension will let you do the password decrypt process
     * The workflow is as following:
     *
     * - 1) Read the password from configured file.
     * - 2) Load the KPair from the system ( PublicKey / PrivateKey ).
     * - 3) When the HED existing and KPair valid, returned decrypt result, otherwise return the
     *      original string content
     */
    static String encryptP(final String source) {
        return runHED(source, hed -> {
            final KPair pair = hed.loadRSA();
            return encryptP(source, pair.getPublicKey());
        });
    }

    static String decryptV(final String source) {
        return runHED(source, hed -> {
            final KPair pair = hed.loadRSA();
            return decryptV(source, pair.getPrivateKey());
        });
    }

    static String encryptP(final String source, final String keyContent) {
        final PublicKey publicKey = x509Public(keyContent, Constants.ALGORITHM_RSA);
        Objects.requireNonNull(publicKey);
        return runCipher(source, Constants.ALGORITHM_RSA, Cipher.ENCRYPT_MODE, publicKey);
    }

    static String decryptV(final String source, final String keyContent) {
        final PrivateKey privateKey = pkcs8Private(keyContent, Constants.ALGORITHM_RSA);
        Objects.requireNonNull(privateKey);
        return runCipher(source, Constants.ALGORITHM_RSA, Cipher.DECRYPT_MODE, privateKey);
    }

    // -------------- Encrypt Private / Decrypt Public --------------

    static String encryptV(final String source) {
        return runHED(source, hed -> {
            final KPair pair = hed.loadRSA();
            return encryptV(source, pair.getPrivateKey());
        });
    }

    static String decryptP(final String source) {
        return runHED(source, hed -> {
            final KPair pair = hed.loadRSA();
            return decryptP(source, pair.getPublicKey());
        });
    }

    static String encryptV(final String source, final String keyContent) {
        final PrivateKey privateKey = pkcs8Private(keyContent, Constants.ALGORITHM_RSA);
        Objects.requireNonNull(privateKey);
        return runCipher(source, Constants.ALGORITHM_RSA, Cipher.ENCRYPT_MODE, privateKey);
    }

    static String decryptP(final String source, final String keyContent) {
        final PublicKey publicKey = x509Public(keyContent, Constants.ALGORITHM_RSA);
        Objects.requireNonNull(publicKey);
        return runCipher(source, Constants.ALGORITHM_RSA, Cipher.DECRYPT_MODE, publicKey);
    }

    // -------------- RSA PublicKey / PrivateKey Hard Coding Internal --------------
    static KPair generate(final int size) {
        return Fn.getJvm(() -> {
            final KeyPairGenerator generate = KeyPairGenerator.getInstance(Constants.ALGORITHM_RSA);
            generate.initialize(size);
            final KeyPair pair = generate.generateKeyPair();
            final RSAPublicKey publicKey = (RSAPublicKey) pair.getPublic();
            final RSAPrivateKey privateKey = (RSAPrivateKey) pair.getPrivate();
            // Base64 encoding
            final String publicKeyString = Base64.encodeBase64String(publicKey.getEncoded());
            final String privateKeyString = Base64.encodeBase64String(privateKey.getEncoded());
            return new KPair(publicKeyString, privateKeyString);
        });
    }


    // -------------- Private Method --------------
    private static PrivateKey pkcs8Private(final String keyContent, final String algorithm) {
        return Fn.getJvm(() -> {
            final byte[] buffer = Base64.decodeBase64(keyContent);
            final KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
            final PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            return (PrivateKey) keyFactory.generatePrivate(keySpec);
        }, keyContent);
    }

    private static PublicKey x509Public(final String keyContent, final String algorithm) {
        return Fn.getJvm(() -> {
            final byte[] buffer = Base64.decodeBase64(keyContent);
            final KeyFactory keyFactory = KeyFactory.getInstance(Constants.ALGORITHM_RSA);
            final X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            return (PublicKey) keyFactory.generatePublic(keySpec);
        }, keyContent);
    }

    private static String runCipher(final String source, final String algorithm, final int mode, final Key key) {
        return Fn.getJvm(() -> {
            final Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(mode, key);
            return new String(cipher.doFinal(Base64.decodeBase64(source)));
        }, source, key);
    }

    private static String runHED(final String source, final Function<HED, String> executor) {
        final HED hed = Pocket.lookup(HED.class);
        if (Objects.isNull(hed)) {
            LOGGER.warn("[ HED ] You have missed `HED` component in your environment: META-INF/services/{0}",
                HED.class.getName());
            return source;
        } else {
            return executor.apply(hed);
        }
    }
}
