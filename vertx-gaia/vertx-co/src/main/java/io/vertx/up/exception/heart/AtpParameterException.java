package io.vertx.up.exception.heart;

import io.vertx.up.exception.ZeroRunException;

public class AtpParameterException extends ZeroRunException {

    public AtpParameterException() {
        super(Info.ATP_MSG);
    }
}
