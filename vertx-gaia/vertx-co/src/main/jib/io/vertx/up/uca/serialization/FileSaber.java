package io.vertx.up.uca.serialization;

import io.vertx.up.exception.web._400FilePathMissingException;
import io.vertx.up.fn.Fn;

import java.io.File;

public class FileSaber extends BaseSaber {

    @Override
    public Object from(final Class<?> paramType,
                       final String filename) {
        return Fn.runOr(() -> {
            final File file = new File(filename);
            // Throw 400 Error
            Fn.outWeb(!file.exists() || !file.canRead(), this.getLogger(),
                _400FilePathMissingException.class, this.getClass(), filename);
            return file;
        }, paramType, filename);
    }
}
