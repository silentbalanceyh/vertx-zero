package io.vertx.up.atom.secure;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Objects;

public class Ostium implements Serializable {
    /**
     * Header parsing
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

    public Method getHeader() {
        return this.header;
    }

    public void setHeader(final Method header) {
        this.header = header;
    }

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
        if (!(o instanceof Ostium)) {
            return false;
        }
        final Ostium ostium = (Ostium) o;
        return Objects.equals(this.header, ostium.header) &&
                Objects.equals(this.authenticate, ostium.authenticate) &&
                Objects.equals(this.authorize, ostium.authorize);
    }

    @Override
    public int hashCode() {

        return Objects.hash(this.header, this.authenticate, this.authorize);
    }

    @Override
    public String toString() {
        return "Ostium{" +
                "header=" + this.header +
                ", authenticate=" + this.authenticate +
                ", authorize=" + this.authorize +
                '}';
    }
}
