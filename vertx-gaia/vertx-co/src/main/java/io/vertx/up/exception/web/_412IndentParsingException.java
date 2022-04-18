package io.vertx.up.exception.web;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.core.json.JsonObject;
import io.vertx.up.exception.WebException;

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
