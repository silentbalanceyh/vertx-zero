package io.vertx.up.experiment.specification.request;

import io.vertx.up.eon.Strings;
import io.vertx.up.experiment.mixture.HApp;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class KApp implements Serializable {
    private final String name;      // vie.app.xxxx
    private String ns;              // ( namespace )
    private String language;        // X-Lang
    private String sigma;           // X-Sigma

    public KApp(final String name) {
        this.name = name;
        this.ns = HApp.ns(name);
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

    public String name() {
        return this.name;
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final KApp kApp = (KApp) o;
        return Objects.equals(this.name, kApp.name) &&
            Objects.equals(this.ns, kApp.ns) &&
            Objects.equals(this.language, kApp.language) &&
            Objects.equals(this.sigma, kApp.sigma);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.ns, this.language, this.sigma);
    }

    @Override
    public String toString() {
        return "KApp{" +
            "name='" + this.name + '\'' +
            ", ns='" + this.ns + '\'' +
            ", language='" + this.language + '\'' +
            ", sigma='" + this.sigma + '\'' +
            '}';
    }
}
