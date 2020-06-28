package io.vertx.up.runtime.soul;

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
 * @author <a href="http://www.origin-x.cn">lang</a>
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
     * Address ( Worker Addr )
     */
    private transient String address;
    /*
     * Worker Component that reflect to `I_SERVICE` table or
     * class definition in static mode.
     */
    private transient Class<?> workerClass;
    private transient Method workerMethod;

    public String getUri() {
        return this.uri;
    }

    public void setUri(final String uri) {
        this.uri = uri;
    }

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
}
