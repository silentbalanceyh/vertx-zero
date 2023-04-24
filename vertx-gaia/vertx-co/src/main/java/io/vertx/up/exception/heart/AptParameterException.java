package io.vertx.up.exception.heart;

import io.zero.exception.ZeroRunException;

public class AptParameterException extends ZeroRunException {

    public AptParameterException() {
        super(Info.ATP_MSG);
    }
}
