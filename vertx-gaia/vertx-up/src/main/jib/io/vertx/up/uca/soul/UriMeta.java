package io.vertx.up.uca.soul;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.vertx.core.http.HttpMethod;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * This object is for uri store, it could store following information
 *
 * 1. Api ( uri + method / uri + method + key ), both static and dynamic
 * 2. Address ( deployment address )
 * 3. Worker ( Critical component that bridged vert.x and zero )
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class UriMeta implements Serializable {
    /*
     * uri, method, key
     * uri - RESTful uri path
     * method - RESTful http method definition
     * key - When enable `I_API` table, it stored api primary key that refer to database
     */
    private transient String uri;
    private transient HttpMethod method;
    private transient String key;

    /*
     * Brief Description for current api here
     * It's for UI display that could store uri comment
     * 1) For System/Static URIs, the comment is equal to `uri`
     * 2) For Dynamic URIs, the comment came from `I_API` field ( name + comment )
     */
    private transient String comment;
    private transient String name;
    /*
     * Whether the API is dynamic or static
     * Dynamic could be configured
     */
    private transient boolean dynamic;
    /*
     * Address ( Worker Addr )
     */
    private transient String address;
    /*
     * Worker Component that reflect to `I_SERVICE` table or
     * class definition in static mode.
     */
    @JsonIgnore
    private transient Class<?> workerClass;
    @JsonIgnore
    private transient Method workerMethod;

    public boolean isDynamic() {
        return this.dynamic;
    }

    public void setDynamic(final boolean dynamic) {
        this.dynamic = dynamic;
    }

    public String getUri() {
        return this.uri;
    }

    public void setUri(final String uri) {
        this.uri = uri;
    }

    @JsonIgnore
    public String getCacheKey() {
        if (Objects.isNull(this.method) || Objects.isNull(this.uri)) {
            return null;
        } else {
            return this.method.name() + ":" + this.uri;
        }
    }

    public HttpMethod getMethod() {
        return this.method;
    }

    public void setMethod(final HttpMethod method) {
        this.method = method;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public Class<?> getWorkerClass() {
        return this.workerClass;
    }

    public void setWorkerClass(final Class<?> workerClass) {
        this.workerClass = workerClass;
    }

    public Method getWorkerMethod() {
        return this.workerMethod;
    }

    public void setWorkerMethod(final Method workerMethod) {
        this.workerMethod = workerMethod;
        this.workerClass = workerMethod.getDeclaringClass();
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(final String comment) {
        this.comment = comment;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final UriMeta uriMeta = (UriMeta) o;
        return this.uri.equals(uriMeta.uri) &&
            this.method == uriMeta.method;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.uri, this.method);
    }
}
