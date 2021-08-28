package io.vertx.up.util;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.up.eon.FileSuffix;
import io.vertx.up.eon.Protocols;
import io.vertx.up.eon.Values;
import io.vertx.up.exception.heart.EmptyStreamException;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Log;

import java.io.*;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Stream read class.
 */
final class Stream {
    /**
     * Direct read by vert.x logger to avoid dead lock.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Stream.class);

    private Stream() {
    }

    /**
     * Codec usage
     *
     * @param message The java object that will be converted from.
     * @param <T>     Target java object that will be converted to.
     *
     * @return Target java object ( Generic Type )
     */
    static <T> byte[] to(final T message) {
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        return Fn.getJvm(new byte[0], () -> {
            final ObjectOutputStream out = new ObjectOutputStream(bytes);
            out.writeObject(message);
            out.close();
            return bytes.toByteArray();
        }, bytes);
    }

    /**
     * Codec usage
     *
     * @param pos    The position of reading
     * @param buffer The buffer to hold the data from reading.
     * @param <T>    The converted java object type, Generic Type
     *
     * @return Return to converted java object.
     */
    @SuppressWarnings("unchecked")
    static <T> T from(final int pos, final Buffer buffer) {
        LOGGER.debug("[ Position ] {}", pos);
        final ByteArrayInputStream stream = new ByteArrayInputStream(buffer.getBytes());
        return Fn.getJvm(null, () -> {
            final ObjectInputStream in = new ObjectInputStream(stream);
            return (T) in.readObject();
        }, stream);
    }

    /**
     * @param filename The filename to describe source path
     *
     * @return Return the InputStream object mount to source path.
     */
    static InputStream read(final String filename) {
        InputStream in = read(filename, null);
        if (Objects.isNull(in)) {
            in = read(filename, Stream.class);
        }
        return in;
    }

    static byte[] readBytes(final String filename) {
        final InputStream in = read(filename);
        return Fn.getJvm(() -> {
            final ByteArrayOutputStream out = new ByteArrayOutputStream(Values.CACHE_SIZE);

            final byte[] temp = new byte[Values.CACHE_SIZE];
            int size = 0;
            while ((size = in.read(temp)) != -1) {
                out.write(temp, 0, size);
            }
            in.close();
            return out.toByteArray();
        });
    }

    /**
     * Ensure read from path
     * 1. Read from current folder
     * 2. clazz == null: Read from class loader
     * 3. clazz != null: Read from clazz's class loader
     *
     * @param filename The filename to describe source path
     * @param clazz    The class loader related class
     *
     * @return Return the InputStream object mount to source path.
     */
    private static InputStream read(final String filename,
                                    final Class<?> clazz) {
        final File file = new File(filename);
        if (file.exists()) {
            Log.debug(LOGGER, Info.INF_CUR, file.exists());
        }
        InputStream in = readSupplier(() -> Fn.getSemi(file.exists(), null,
            () -> in(file),
            () -> (null == clazz) ? in(filename) : in(filename, clazz)), filename);
        // Stream.class get
        if (null == in) {
            in = readSupplier(() -> Stream.class.getResourceAsStream(filename), filename);
        }
        // System.Class Loader
        if (null == in) {
            in = readSupplier(() -> ClassLoader.getSystemResourceAsStream(filename), filename);
        }
        /*
         * Jar reading
         * Firstly, check whether it contains jar flag
         */
        if (null == in && filename.contains(FileSuffix.JAR_DIVIDER)) {
            in = readJar(filename);
        }
        if (null == in) {
            throw new EmptyStreamException(filename);
        }
        return in;
    }

    private static InputStream readJar(final String filename) {
        return readSupplier(() -> {
            try {
                final URL url = new URL(filename);
                final String protocol = url.getProtocol();
                if (Protocols.JAR.equals(protocol)) {
                    final JarURLConnection jarCon = (JarURLConnection) url.openConnection();
                    return jarCon.getInputStream();
                } else {
                    return null; // Jar Error
                }
            } catch (final IOException e) {
                Log.jvm(LOGGER, e);
                return null;
            }
        }, filename);
    }

    private static InputStream readSupplier(final Supplier<InputStream> supplier,
                                            final String filename) {
        final InputStream in = supplier.get();
        if (null != in) {
            Log.debug(LOGGER, Info.INF_PATH, filename, in);
        }
        return in;
    }

    /**
     * Stream read from up.god.file object
     * new FileInputStream(up.god.file)
     *
     * @param file The up.god.file object to describe source path
     *
     * @return Return the InputStream object mount to source path.
     */
    static InputStream in(final File file) {
        return Fn.getJvm(() -> (file.exists() && file.isFile())
            ? new FileInputStream(file) : null, file);
    }

    /**
     * Stream read from clazz
     * clazz.getResourceAsStream(filename)
     *
     * @param filename The filename to describe source path
     * @param clazz    The class loader related class
     *
     * @return Return the InputStream object mount to source path.
     */
    static InputStream in(final String filename,
                          final Class<?> clazz) {
        return Fn.getJvm(
            () -> clazz.getResourceAsStream(filename), clazz, filename);
    }

    /**
     * Stream read from Thread Class Loader
     * Thread.currentThread().getContextClassLoader()
     *
     * @param filename The filename to describe source path
     *
     * @return Return the InputStream object mount to source path.
     */
    static InputStream in(final String filename) {
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        return Fn.getJvm(
            () -> loader.getResourceAsStream(filename), filename);
    }
}
