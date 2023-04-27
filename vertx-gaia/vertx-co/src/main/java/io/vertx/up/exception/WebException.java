package io.vertx.up.exception;

import io.horizon.eon.VString;
import io.horizon.exception.AbstractException;
import io.vertx.core.http.HttpStatusCode;
import io.vertx.core.json.JsonObject;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Errors;
import io.vertx.up.util.Ut;

import java.text.MessageFormat;

/**
 *
 */
public abstract class WebException extends AbstractException {

    protected static final String INFO = "info";
    protected static final String CODE = "code";
    private static final String MESSAGE = "message";
    private final String message;
    private final Class<?> target;
    protected HttpStatusCode status;
    private transient Object[] params;
    private String readible;

    public WebException(final String message) {
        super(message);
        this.message = message;
        status = HttpStatusCode.BAD_REQUEST;
        target = null;      // Target;
    }

    public WebException(final Class<?> clazz, final Object... args) {
        super(VString.EMPTY);
        message = Errors.normalizeWeb(clazz, getCode(), args);
        params = args;
        status = HttpStatusCode.BAD_REQUEST;
        target = clazz;     // Target;
    }

    public abstract int getCode();

    @Override
    public String getMessage() {
        return message;
    }

    public Class<?> getTarget() {
        return target;
    }

    public HttpStatusCode getStatus() {
        // Default exception for 400
        return status;
    }

    public void setStatus(final HttpStatusCode status) {
        this.status = status;
    }

    public String getReadible() {
        return readible;
    }

    public void setReadible(final String readible) {
        Fn.safeNull(() -> {
            if (null == params) {
                this.readible = readible;
            } else {
                this.readible = MessageFormat.format(readible, params);
            }
        }, readible);
    }

    public JsonObject toJson() {
        final JsonObject data = new JsonObject();
        data.put(CODE, getCode());
        data.put(MESSAGE, getMessage());
        if (Ut.isNotNil(readible)) {
            data.put(INFO, readible);
        }
        return data;
    }
}
