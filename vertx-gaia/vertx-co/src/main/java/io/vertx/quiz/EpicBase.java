package io.vertx.quiz;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

public class EpicBase {
    /*
     * Get file from the same package here.
     */
    protected String ioFile(final String filename) {
        final Class<?> clazz = this.getClass();
        final String file = "test/" + clazz.getPackage().getName() + "/" + filename;
        this.getLogger().info("[ Sim ] Test input file reading from: {0}", file);
        return file;
    }

    protected JsonObject ioJObject(final String filename) {
        return Ut.ioJObject(this.ioFile(filename));
    }

    protected JsonArray ioJArray(final String filename) {
        return Ut.ioJArray(this.ioFile(filename));
    }

    protected Annal getLogger() {
        return Annal.get(this.getClass());
    }
}
