package io.vertx.up.exception.web;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;
import io.vertx.up.util.Ut;
import jakarta.ws.rs.core.MediaType;

import java.util.Set;

public class _415MediaNotSupportException extends WebException {

    public _415MediaNotSupportException(final Class<?> clazz,
                                        final MediaType media,
                                        final Set<MediaType> expected) {
        super(clazz, media.toString(), Ut.fromJoin(expected.toArray(new MediaType[]{})));
    }

    @Override
    public int getCode() {
        return -60006;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.UNSUPPORTED_MEDIA_TYPE;
    }
}
