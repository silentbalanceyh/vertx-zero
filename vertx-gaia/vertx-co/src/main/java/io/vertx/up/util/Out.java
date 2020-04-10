package io.vertx.up.util;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.Values;
import io.vertx.up.eon.em.CompressLevel;
import io.vertx.up.fn.Fn;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;

/*
 *
 */
final class Out {

    private Out() {
    }

    @SuppressWarnings("all")
    static void write(final String path, final String data) {
        Fn.safeNull(() -> Fn.safeJvm(() -> {
            final File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            final FileWriter writer = new FileWriter(file);
            writer.write(data);
            writer.close();
        }), path, data);
    }

    static void write(final String path, final JsonObject data) {
        final String target = null == data ? "{}" : data.encodePrettily();
        write(path, target);
    }

    static void write(final String path, final JsonArray data) {
        final String target = null == data ? "[]" : data.encodePrettily();
        write(path, target);
    }

    static boolean make(final String path) {
        return Fn.getNull(() -> Fn.getJvm(() -> {
            final File file = new File(path);
            boolean created = false;
            if (!file.exists()) {
                created = file.mkdirs();
            }
            return created;
        }), path);
    }

    static void writeCompress(final String path, final JsonArray data) {
        final String target = null == data ? "[]" : data.encode();
        writeCompress(path, target);
    }

    static void writeCompress(final String path, final JsonObject data) {
        final String target = null == data ? "{}" : data.encode();
        writeCompress(path, target);
    }

    static void writeCompress(final String path, final String data) {
        final byte[] dataBytes = data.getBytes(Values.DEFAULT_CHARSET);
        final byte[] output = Compressor.compress(dataBytes, CompressLevel.BEST_COMPRESSION);
        Fn.safeJvm(() -> {
            final FileOutputStream fos = new FileOutputStream(path);
            fos.write(output);
            fos.close();
        });
    }
}
