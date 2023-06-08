package io.vertx.up.commune.secure;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 *
 */
public class Against implements Serializable {
    /*
     * 401: Authenticate method
     * @Authenticate
     */
    private Method authenticate;
    /*
     * 403: Authorize method
     * @Authorized
     */
    private Method authorization;
    /*
     * 403: Resource
     * @AuthorizedResource
     */
    private Method resource;
    /*
     * 403: User
     * @AuthorizedUser
     */
    private Method user;

    public Method getAuthenticate() {
        return this.authenticate;
    }

    public void setAuthenticate(final Method authenticate) {
        this.authenticate = authenticate;
    }

    public Method getAuthorization() {
        return this.authorization;
    }

    public void setAuthorization(final Method authorization) {
        this.authorization = authorization;
    }

    public Method getResource() {
        return this.resource;
    }

    public void setResource(final Method resource) {
        this.resource = resource;
    }

    public Method getUser() {
        return this.user;
    }

    public void setUser(final Method user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Against{" +
            "authenticate=" + this.authenticate +
            ", authorization=" + this.authorization +
            ", resource=" + this.resource +
            ", user=" + this.user +
            '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        final Against against = (Against) o;
        return this.authenticate.equals(against.authenticate) &&
            Objects.equals(this.authorization, against.authorization) &&
            Objects.equals(this.resource, against.resource) &&
            Objects.equals(this.user, against.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.authenticate, this.authorization, this.resource, this.user);
    }
}
