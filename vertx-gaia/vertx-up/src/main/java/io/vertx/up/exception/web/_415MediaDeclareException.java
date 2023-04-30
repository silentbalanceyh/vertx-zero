package io.vertx.up.exception.web;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;
import jakarta.ws.rs.core.MediaType;

/**
 * # 「Error」Zero Exception
 *
 * * Category: WebException
 * * Code: -60053
 * * Status: 415
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class _415MediaDeclareException extends WebException {
    public _415MediaDeclareException(final Class<?> clazz,
                                     final Class<?> argType, final MediaType type) {
        super(clazz, argType.getName(), type.toString());
    }

    @Override
    public int getCode() {
        return -60053;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.UNSUPPORTED_MEDIA_TYPE;
    }
}
