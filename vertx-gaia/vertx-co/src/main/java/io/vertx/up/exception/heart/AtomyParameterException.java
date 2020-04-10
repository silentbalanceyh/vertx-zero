package io.vertx.up.exception.heart;

import io.vertx.up.exception.ZeroRunException;

public class AtomyParameterException extends ZeroRunException {

    public AtomyParameterException() {
        super(Info.ATOMY_MSG);
    }
}
