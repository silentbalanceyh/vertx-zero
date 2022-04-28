package io.vertx.up.experiment.specification;

import io.vertx.up.eon.Strings;
import io.vertx.up.experiment.mixture.HApp;

import java.io.Serializable;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class KApp implements Serializable {
    private final String appName;
    private String ns;
    private String language;
    private String sigma;

    public KApp(final String appName) {
        this.appName = appName;
        this.ns = HApp.ns(appName);
    }

    public KApp ns(final String namespace) {
        this.ns = namespace;
        return this;
    }

    public KApp language(final String language) {
        this.language = language;
        return this;
    }

    public KApp sigma(final String sigma) {
        this.sigma = sigma;
        return this;
    }

    public String appName() {
        return this.appName;
    }

    public String ns() {
        return this.ns;
    }

    public String language() {
        return this.language;
    }

    public String sigma() {
        return this.sigma;
    }

    public String keyUnique(final String identifier) {
        return this.ns + Strings.DASH + identifier;
    }
}
