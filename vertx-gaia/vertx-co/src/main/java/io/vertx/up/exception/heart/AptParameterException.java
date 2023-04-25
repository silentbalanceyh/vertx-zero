package io.vertx.up.exception.heart;

import io.horizon.exception.ZeroRunException;

public class AptParameterException extends ZeroRunException {

    public AptParameterException() {
        super(Info.ATP_MSG);
    }
}
