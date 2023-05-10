package io.vertx.tp.error;

import io.horizon.exception.BootingException;
import io.vertx.up.eon.em.secure.AuthWall;

public class WallItemSizeException extends BootingException {

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
