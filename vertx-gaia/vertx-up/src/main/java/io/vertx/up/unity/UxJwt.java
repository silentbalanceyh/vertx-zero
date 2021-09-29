package io.vertx.up.unity;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.FileSystemException;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.JWTOptions;
import io.vertx.ext.auth.KeyStoreOptions;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.ext.auth.impl.jose.JWK;
import io.vertx.ext.auth.impl.jose.JWT;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.auth.jwt.impl.JWTAuthProviderImpl;
import io.vertx.up.eon.Plugins;
import io.vertx.up.fn.Fn;
import io.vertx.up.uca.yaml.Node;
import io.vertx.up.uca.yaml.ZeroUniform;
import io.vertx.up.util.Ut;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.function.Function;

/**
 * Jwt token extract
 */
@SuppressWarnings("all")
class UxJwt {

    private static JWT JWT_INSTANCE;

    static JsonObject extract(final String jwt) {
        // Extract "config" from jwt configuration.
        final JsonObject config = UxJwt.readOptions();
        // Extract token from input jwt.
        return Fn.getNull(() -> extract(jwt, config), config);
    }

    static String generate(final JsonObject claims, final JWTOptions options) {
        return generate(claims, options, Ut::ioBuffer);
    }

    static String generate(final JsonObject claims, final JWTOptions options,
                           final Function<String, Buffer> funcBuffer) {
        final JsonObject opts = readOptions();
        final JWTAuthOptions meta = Fn.getNull(new JWTAuthOptions(), () -> new JWTAuthOptions(opts), opts);
        return Fn.getNull(() -> {
            final JsonObject _claims = claims.copy();
            if (options.getPermissions() != null && !_claims.containsKey(meta.getPermissionsClaimKey())) {
                _claims.put(meta.getPermissionsClaimKey(), new JsonArray(options.getPermissions()));
            }
            final JWT reference = create(meta, funcBuffer);
            return reference.sign(_claims, options);
        }, meta, claims);
    }

    private static JsonObject readOptions() {
        final Node<JsonObject> node = Ut.instance(ZeroUniform.class);
        final JsonObject options = node.read();
        // Extract data from "secure"
        final JsonObject secure = Fn.getNull(() -> options.getJsonObject(Plugins.Infix.SECURE), options);
        // Extract "jwt" from secure configuration.
        final JsonObject jwtConfig = Fn.getNull(() -> secure.getJsonObject("jwt"), secure);
        // Extract "config" from jwt configuration.
        return Fn.getNull(() -> jwtConfig.getJsonObject("config"), jwtConfig);
    }

    static JsonObject extract(final String jwt, final JsonObject options) {
        if (Ut.isNil(jwt)) {
            // Fix Issue: java.lang.RuntimeException: Not enough or too many segments
            return new JsonObject();
        } else {
            final JWT reference = create(new JWTAuthOptions(options), Ut::ioBuffer);
            return reference.decode(jwt);
        }
    }

    /**
     * Singleton for JWT instance to avoid performance issue.
     *
     * @param config     JWT configuration
     * @param funcBuffer IO function
     *
     * @return Valid JWT reference
     */
    static JWT create(final JWTAuthOptions config, final Function<String, Buffer> funcBuffer) {
        if (null == JWT_INSTANCE) {
            synchronized (UxJwt.class) {
                if (null == JWT_INSTANCE) {
                    JWT_INSTANCE = createDirect(config, funcBuffer);
                }
            }
        }
        return JWT_INSTANCE;
    }

    private static JWT createDirect(final JWTAuthOptions config, final Function<String, Buffer> funcBuffer) {
        final JWT reference = new JWT();
        final JWTOptions options = config.getJWTOptions();
        // set the nonce algorithm
        reference.nonceAlgorithm(options.getNonceAlgorithm());

        final KeyStoreOptions keyStore = config.getKeyStore();
        try {
            // Attempt to load a Key file
            if (keyStore != null) {
                final KeyStore ks;
                if (keyStore.getProvider() == null) {
                    ks = KeyStore.getInstance(keyStore.getType());
                } else {
                    ks = KeyStore.getInstance(keyStore.getType(), keyStore.getProvider());
                }

                // synchronize on the class to avoid the case where multiple file accesses will overlap
                synchronized (JWTAuthProviderImpl.class) {
                    final Buffer keystore = funcBuffer.apply(keyStore.getPath());

                    try (InputStream in = new ByteArrayInputStream(keystore.getBytes())) {
                        ks.load(in, keyStore.getPassword().toCharArray());
                    }
                }
                // load all available keys in the keystore
                for (JWK key : JWK.load(ks, keyStore.getPassword(), keyStore.getPasswordProtection())) {
                    reference.addJWK(key);
                }
            }
            // attempt to load pem keys
            final List<PubSecKeyOptions> keys = config.getPubSecKeys();

            if (keys != null) {
                for (PubSecKeyOptions pubSecKey : config.getPubSecKeys()) {
                    reference.addJWK(new JWK(pubSecKey));
                }
            }

            // attempt to load jwks
            final List<JsonObject> jwks = config.getJwks();

            if (jwks != null) {
                for (JsonObject jwk : jwks) {
                    reference.addJWK(new JWK(jwk));
                }
            }
        } catch (KeyStoreException | IOException | FileSystemException | CertificateException | NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
        /*
        try {
            if (keyStore != null) {
                final KeyStore ks = KeyStore.getInstance(keyStore.getType());
                final Class var5 = JwtAuthProvider.class;
                synchronized (JwtAuthProvider.class) {
                    final Buffer keystore = funcBuffer.apply(keyStore.getPath());
                    final InputStream in = new ByteArrayInputStream(keystore.getBytes());
                    Throwable var8 = null;

                    try {
                        ks.load(in, keyStore.getPassword().toCharArray());
                    } catch (final Throwable var20) {
                        var8 = var20;
                        throw var20;
                    } finally {
                        if (in != null) {
                            if (var8 != null) {
                                try {
                                    in.close();
                                } catch (final Throwable var19) {
                                    var8.addSuppressed(var19);
                                }
                            } else {
                                in.close();
                            }
                        }

                    }
                }
                reference = new JWT(ks, keyStore.getPassword().toCharArray());
            } else {
                reference = new JWT();
                final List<PubSecKeyOptions> keys = config.getPubSecKeys();
                if (keys != null) {
                    final Iterator var25 = config.getPubSecKeys().iterator();

                    while (var25.hasNext()) {
                        final PubSecKeyOptions pubSecKey = (PubSecKeyOptions) var25.next();
                        if (pubSecKey.isSymmetric()) {
                            reference.addJWK(new JWK(pubSecKey.getAlgorithm(), pubSecKey.getPublicKey()));
                        } else {
                            reference.addJWK(new JWK(pubSecKey.getAlgorithm(), pubSecKey.isCertificate(), pubSecKey.getPublicKey(), pubSecKey.getSecretKey()));
                        }
                    }
                }

                final List<PubSecKeyOptions> secrets = config.getPubSecKeys();
                if (secrets != null) {
                    final Iterator var28 = secrets.iterator();

                    while (var28.hasNext()) {
                        final PubSecKeyOptions secret = (PubSecKeyOptions) var28.next();
                        reference.addSecret(secret.getType(), secret.getSecret());
                    }
                }
            }

        } catch (final IOException | FileSystemException | CertificateException | NoSuchAlgorithmException | KeyStoreException var23) {
            throw new _500JwtRuntimeException(UxJwt.class, var23);
        }*/
        return reference;
    }
}
