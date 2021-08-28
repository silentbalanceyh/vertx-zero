package io.vertx.up.atom.secure;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 *
 */
public class Phylum implements Serializable {
    /**
     * 401: Authenticate method
     */
    private Method authenticate;
    /**
     * 403: Authorize method
     */
    private Method authorize;

    public Method getAuthenticate() {
        return this.authenticate;
    }

    public void setAuthenticate(final Method authenticate) {
        this.authenticate = authenticate;
    }

    public Method getAuthorize() {
        return this.authorize;
    }

    public void setAuthorize(final Method authorize) {
        this.authorize = authorize;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Phylum)) {
            return false;
        }
        final Phylum phylum = (Phylum) o;
        return Objects.equals(this.authenticate, phylum.authenticate) &&
            Objects.equals(this.authorize, phylum.authorize);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.authenticate, this.authorize);
    }

    @Override
    public String toString() {
        return "Phylum{" +
            "authenticate=" + this.authenticate +
            ", authorize=" + this.authorize +
            '}';
    }
}
