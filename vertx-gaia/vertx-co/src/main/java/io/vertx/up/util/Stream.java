package io.vertx.up.util;

import io.vertx.core.buffer.Buffer;
import io.vertx.up.eon.FileSuffix;
import io.vertx.up.eon.Protocols;
import io.vertx.up.eon.Strings;
import io.vertx.up.eon.Values;
import io.vertx.up.exception.heart.EmptyStreamException;
import io.vertx.up.fn.Actuator;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Log;
import io.vertx.up.runtime.EnvVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    static String root() {
        final URL rootUrl = Stream.class.getResource("/");
        if (Objects.isNull(rootUrl)) {
            return Strings.EMPTY;
        } else {
            final File rootFile = new File(rootUrl.getFile());
            return rootFile.getAbsolutePath() + "/";
        }
    }

    static byte[] readBytes(final String filename) {
        final InputStream in = read(filename);
        return Fn.getJvm(() -> {
            final ByteArrayOutputStream out = new ByteArrayOutputStream(Values.CACHE_SIZE);

            final byte[] temp = new byte[Values.CACHE_SIZE];
            int size;
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
        ioDebug(() -> Log.info(LOGGER, Info.__FILE_ROOT, root(), filename));
        /*
         * 0. new File(filename)
         *    new FileInputStream(File)
         */
        final File file = new File(filename);
        if (file.exists()) {
            ioDebug(() -> Log.info(LOGGER, Info.INF_CUR, file.exists()));
            return readSupplier(() -> in(file), filename);
        } else {
            /*
             *  filename re-calculate with root()
             */
            final String refile = StringUtil.path(root(), filename);
            final File fileResolved = new File(refile);
            if (fileResolved.exists()) {
                return readSupplier(() -> in(fileResolved), refile);
            }
        }

        InputStream in;
        if (Objects.isNull(clazz)) {


            /*
             * 1. Thread.currentThread().getContextClassLoader()
             *    loader.getResourceAsStream(filename)
             */
            in = readSupplier(() -> in(filename), filename);
        } else {


            /*
             * 2. clazz.getResourceAsStream(filename)
             */
            in = readSupplier(() -> in(filename, clazz), filename);
            if (Objects.isNull(in)) {


                /*
                 * Switch to 1.
                 */
                in = readSupplier(() -> in(filename), filename);
            }
        }

        // Stream.class get
        if (Objects.isNull(in)) {


            /*
             * 3. Stream.class.getResourceAsStream(filename)
             */
            ioDebug(() -> Log.info(LOGGER, Info.__CLASS_LOADER_STREAM, filename));
            in = readSupplier(() -> Stream.class.getResourceAsStream(filename), filename);
        }
        // System.Class Loader
        if (Objects.isNull(in)) {


            /*
             * 4. ClassLoader.getSystemResourceAsStream(filename)
             */
            ioDebug(() -> Log.info(LOGGER, Info.__CLASS_LOADER_SYSTEM, filename));
            in = readSupplier(() -> ClassLoader.getSystemResourceAsStream(filename), filename);
        }
        /*
         * Jar reading
         * Firstly, check whether it contains jar flag
         */
        if (Objects.isNull(in) && filename.contains(FileSuffix.JAR_DIVIDER)) {


            /*
             * 5. readJar(filename)
             */
            ioDebug(() -> Log.info(LOGGER, Info.__JAR_RESOURCE, filename));
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
            ioDebug(() -> Log.info(LOGGER, Info.INF_PATH, filename, in));
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
        ioDebug(() -> Log.info(LOGGER, Info.__FILE_INPUT_STREAM, Objects.isNull(file) ? null : file.getAbsolutePath()));
        return Fn.getJvm(() -> (file.exists() && file.isFile()) ? new FileInputStream(file) : null, file);
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
    static InputStream in(final String filename, final Class<?> clazz) {
        ioDebug(() -> Log.info(LOGGER, Info.__RESOURCE_AS_STREAM, filename));
        return Fn.getJvm(() -> clazz.getResourceAsStream(filename), clazz, filename);
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
        ioDebug(() -> Log.info(LOGGER, Info.__CLASS_LOADER, filename));
        return Fn.getJvm(() -> loader.getResourceAsStream(filename), filename);
    }

    private static void ioDebug(final Actuator executor) {
        final boolean ioDebug = Env.envBool(EnvVariables.Z_IO_DEBUG);
        if (ioDebug) {
            executor.execute();
        }
    }
}
