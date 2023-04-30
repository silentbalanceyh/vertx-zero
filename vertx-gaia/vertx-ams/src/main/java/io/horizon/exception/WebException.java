package io.horizon.exception;

import io.horizon.eon.VString;
import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.eon.error.ErrorMessage;
import io.horizon.fn.HFn;
import io.horizon.util.HaS;
import io.vertx.core.json.JsonObject;

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
        this.status = HttpStatusCode.BAD_REQUEST;
        this.target = null;      // Target;
    }

    public WebException(final Class<?> clazz, final Object... args) {
        super(VString.EMPTY);
        this.message = HaS.fromError(ErrorMessage.EXCEPTION_WEB, clazz, this.getCode(), args);
        //        Errors.normalizeWeb(clazz, this.getCode(), args);
        this.params = args;
        this.status = HttpStatusCode.BAD_REQUEST;
        this.target = clazz;     // Target;
    }

    @Override
    public abstract int getCode();

    @Override
    public String getMessage() {
        return this.message;
    }

    public Class<?> getTarget() {
        return this.target;
    }

    public HttpStatusCode getStatus() {
        // Default exception for 400
        return this.status;
    }

    public void setStatus(final HttpStatusCode status) {
        this.status = status;
    }

    public String getReadible() {
        return this.readible;
    }

    public void setReadible(final String readible) {
        HFn.runAt(() -> {
            if (null == this.params) {
                this.readible = readible;
            } else {
                this.readible = MessageFormat.format(readible, this.params);
            }
        }, readible);
    }

    public JsonObject toJson() {
        final JsonObject data = new JsonObject();
        data.put(CODE, this.getCode());
        data.put(MESSAGE, this.getMessage());
        if (HaS.isNotNil(this.readible)) {
            data.put(INFO, this.readible);
        }
        return data;
    }
}
