package io.vertx.up.exception.heart;

import io.horizon.exception.ZeroRunException;

import java.text.MessageFormat;

/**
 * vertx:
 * lime: error, inject, db, consul
 */
public class LimeFileException extends ZeroRunException {

    public LimeFileException(final String filename) {
        super(MessageFormat.format(Info.LIME_FILE, filename));
    }
}
