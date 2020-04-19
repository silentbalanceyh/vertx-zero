package io.vertx.up.exception.web;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

import javax.ws.rs.core.MediaType;

/**
 * # 「Error」Zero Exception
 *
 * * Category: WebException
 * * Code: -40069
 * * Status: 415
 *
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class _415MediaDeclareException extends WebException {
    public _415MediaDeclareException(final Class<?> clazz,
                                     final Class<?> argType, final MediaType type) {
        super(clazz, argType.getName(), type.toString());
    }

    @Override
    public int getCode() {
        return -40069;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.UNSUPPORTED_MEDIA_TYPE;
    }
}
