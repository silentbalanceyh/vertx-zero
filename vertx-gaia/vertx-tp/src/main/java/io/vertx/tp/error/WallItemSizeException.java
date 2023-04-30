package io.vertx.tp.error;

import io.horizon.eon.em.secure.AuthWall;
import io.horizon.exception.BootingException;

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
