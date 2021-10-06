package io.vertx.tp.error;

import io.vertx.up.eon.em.AuthWall;
import io.vertx.up.exception.UpException;

public class WallItemSizeException extends UpException {

    public WallItemSizeException(final Class<?> clazz,
                                 final AuthWall wall,
                                 final Integer size) {
        super(clazz, wall.key(), size);
    }

    @Override
    public int getCode() {
        return -40076;
    }
}
