package io.vertx.up.exception.boot;

import io.horizon.exception.BootingException;
import io.vertx.up.eon.em.EmSecure;

public class WallItemSizeException extends BootingException {

    public WallItemSizeException(final Class<?> clazz,
                                 final EmSecure.AuthWall wall,
                                 final Integer size) {
        super(clazz, wall.key(), size);
    }

    @Override
    public int getCode() {
        return -40076;
    }
}
