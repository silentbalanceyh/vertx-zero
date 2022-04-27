package io.vertx.up.experiment.mixture;

import io.vertx.up.commune.Json;
import io.vertx.up.eon.KValue;
import io.vertx.up.eon.Strings;

import java.io.Serializable;
import java.text.MessageFormat;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface HApp extends Serializable, Json {
    /*
     * Default namespace value based on appName
     * Modular belong to `Origin X Engine`
     * It's critical extension in zero framework, it could do dynamic modular on DDL in database
     * Also you could provide your only implementation to replace some configuration.
     */
    static String ns(final String appName) {
        return MessageFormat.format(KValue.NS.DEFAULT, appName);
    }

    static String ns(final String appName, final String identifier) {
        return ns(appName) + Strings.DASH + identifier;
    }

    /* Uniform Model identifier */
    String identifier();

    /*
     * Uniform namespace here
     */
    String namespace();

    /*
     * Uniform File Path for initialize
     */
    String file();
}
