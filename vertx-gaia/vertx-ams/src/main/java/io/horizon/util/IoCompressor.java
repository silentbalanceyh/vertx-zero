package io.horizon.util;

import io.horizon.eon.VValue;
import io.horizon.eon.em.typed.CompressLevel;
import io.horizon.fn.HFn;

import java.io.ByteArrayOutputStream;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

class IoCompressor {

    static byte[] compress(final byte[] data, final CompressLevel level) {
        return HFn.failOr(() -> {
            final Deflater deflater = new Deflater();
            deflater.setLevel(level.getLevel());
            deflater.setInput(data);

            try (final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length)) {
                deflater.finish();
                final byte[] buffer = new byte[VValue.DFT.SIZE_BYTE_ARRAY];
                while (!deflater.finished()) {
                    final int count = deflater.deflate(buffer);
                    outputStream.write(buffer, 0, count);
                }
                return outputStream.toByteArray();
            }
        });
    }

    static byte[] decompress(final byte[] data) {
        return HFn.failOr(() -> {
            final Inflater inflater = new Inflater();
            inflater.setInput(data);

            try (final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(
                data.length)) {
                final byte[] buffer = new byte[VValue.DFT.SIZE_BYTE_ARRAY];
                while (!inflater.finished()) {
                    final int count = inflater.inflate(buffer);
                    outputStream.write(buffer, 0, count);
                }
                return outputStream.toByteArray();
            }
        });
    }
}
