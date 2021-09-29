package io.vertx.up.atom.secure;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 *
 */
public class Against implements Serializable {
    /*
     * User defined header parsing code
     * workflow, in this situation the auth flow should
     * be defined instead of standard.
     */
    private Method header;
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
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final Against against = (Against) o;
        return Objects.equals(this.header, against.header) && this.authenticate.equals(against.authenticate) && Objects.equals(this.authorize, against.authorize);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.header, this.authenticate, this.authorize);
    }

    public Method getHeader() {
        return this.header;
    }

    public void setHeader(final Method header) {
        this.header = header;
    }

    @Override
    public String toString() {
        return "Against{" +
            "header=" + this.header +
            ", authenticate=" + this.authenticate +
            ", authorize=" + this.authorize +
            '}';
    }
}
