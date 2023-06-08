package io.vertx.up.exception.web;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class _412IndentParsingException extends WebException {

    public _412IndentParsingException(final Class<?> clazz, final String targetIndent,
                                      final JsonObject data) {
        super(clazz, targetIndent, data);
    }

    @Override
    public int getCode() {
        return -80543;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.PRECONDITION_FAILED;
    }
}
