package io.vertx.up.util;

import io.horizon.eon.VValue;
import io.horizon.eon.em.CompressLevel;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.config.HugeFile;
import io.vertx.up.fn.Fn;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStream;

/*
 *
 */
final class Out {

    private Out() {
    }

    @SuppressWarnings("all")
    static void write(final String path, final String data) {
        Fn.runAt(() -> Fn.jvmAt(() -> {
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
        return Fn.runOr(() -> Fn.failOr(() -> {
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
        final byte[] dataBytes = data.getBytes(io.horizon.eon.VValue.DFT.CHARSET);
        final byte[] output = Compressor.compress(dataBytes, CompressLevel.BEST_COMPRESSION);
        Fn.jvmAt(() -> {
            final FileOutputStream fos = new FileOutputStream(path);
            fos.write(output);
            fos.close();
        });
    }

    /*
     * OutputStream
     */
    static void writeBig(final String filename, final OutputStream output) {
        final HugeFile file = new HugeFile(filename);
        Fn.jvmAt(() -> {
            while (file.read() != VValue.RANGE) {
                final byte[] bytes = file.getCurrentBytes();
                output.write(bytes);
                output.flush();
            }
            output.close();
        });
    }
}
