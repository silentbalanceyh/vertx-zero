package io.vertx.up.util;

import io.vertx.up.atom.config.HugeFile;
import io.vertx.up.eon.Values;
import io.vertx.up.fn.Fn;

import java.io.OutputStream;

class IOBig {
    /*
     * OutputStream
     */
    static void writeTo(final String filename, final OutputStream output) {
        final HugeFile file = new HugeFile(filename);
        Fn.safeJvm(() -> {
            while (file.read() != Values.RANGE) {
                final byte[] bytes = file.getCurrentBytes();
                output.write(bytes);
                output.flush();
            }
            output.close();
        });
    }
}
