package io.horizon.util;

import io.horizon.eon.VPath;
import io.horizon.exception.internal.EmptyIoException;
import io.horizon.fn.HFn;
import io.vertx.core.buffer.Buffer;

import java.io.*;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Stream read class.
 */
final class IoStream {
    /**
     * 「DEAD-LOCK」LoggerFactory.getLogger
     * Do not use `Annal` logger because of deadlock.
     */
    //    private static final Logger LOGGER = LoggerFactory.getLogger(IoStream.class);
    private static final LogUtil LOG = LogUtil.from(IoStream.class);

    private IoStream() {
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
        return HFn.failOr(new byte[0], () -> {
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
        final ByteArrayInputStream stream = new ByteArrayInputStream(buffer.getBytes());
        return HFn.failOr(null, () -> {
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
        return read(filename, null);
    }


    static byte[] readBytes(final String filename) {
        final InputStream in = read(filename);
        return HFn.failOr(() -> IoN.bytes(in), in);
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
    static InputStream read(final String filename,
                            final Class<?> clazz) {
        final String root = IoPath.root();
        LOG.io(INFO.IoStream.__FILE_ROOT, root, filename);
        /*
         * 0. new File(filename)
         *    new FileInputStream(File)
         */
        // 切换NIO
        final File file = new File(filename);
        if (file.exists()) {
            LOG.io(INFO.IoStream.INF_CUR, file.exists());
            return readSupplier(() -> readDirect(file), filename);
        } else {
            /*
             *  filename re-calculate with root()
             */
            final String refile = IoPath.resolve(IoPath.root(), filename);
            final File fileResolved = new File(refile);
            if (fileResolved.exists()) {
                return readSupplier(() -> readDirect(fileResolved), refile);
            }
        }

        InputStream in;
        if (Objects.isNull(clazz)) {


            /*
             * 1. Thread.currentThread().getContextClassLoader()
             *    loader.getResourceAsStream(filename)
             */
            in = readSupplier(() -> readDirect(filename), filename);
        } else {


            /*
             * 2. clazz.getResourceAsStream(filename)
             */
            in = readSupplier(() -> readDirect(filename, clazz), filename);
            if (Objects.isNull(in)) {


                /*
                 * Switch to 1.
                 */
                in = readSupplier(() -> readDirect(filename), filename);
            }
        }

        // Stream.class get
        if (Objects.isNull(in)) {


            /*
             * 3. Stream.class.getResourceAsStream(filename)
             */
            LOG.io(INFO.IoStream.__CLASS_LOADER_STREAM, filename);
            in = readSupplier(() -> IoStream.class.getResourceAsStream(filename), filename);
        }
        // System.Class Loader
        if (Objects.isNull(in)) {


            /*
             * 4. ClassLoader.getSystemResourceAsStream(filename)
             */
            LOG.io(INFO.IoStream.__CLASS_LOADER_SYSTEM, filename);
            in = readSupplier(() -> ClassLoader.getSystemResourceAsStream(filename), filename);
        }
        /*
         * Jar reading
         * Firstly, check whether it contains jar flag
         */
        if (Objects.isNull(in) && filename.contains(VPath.SUFFIX.JAR_DIVIDER)) {


            /*
             * 5. readJar(filename)
             */
            LOG.io(INFO.IoStream.__JAR_RESOURCE, filename);
            in = readJar(filename);
        }
        if (null == in) {
            throw new EmptyIoException(IoStream.class, filename);
        }
        return in;
    }

    // 直接读取文件
    static InputStream readDirect(final File file) {
        if (Objects.isNull(file)) {
            // 文件本身为空，跳过
            return null;
        }
        if (!file.exists() || !file.isFile()) {
            // 文件不存在，跳过
            return null;
        }
        // NIO切换
        return IoN.stream(file);
    }

    static InputStream readDirect(final String filename) {
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        LOG.io(INFO.IoStream.__CLASS_LOADER, filename);
        return HFn.failOr(() -> loader.getResourceAsStream(filename), filename);
    }

    private static InputStream readDirect(final String filename, final Class<?> clazz) {
        LOG.io(INFO.IoStream.__RESOURCE_AS_STREAM, clazz, filename);
        return HFn.failOr(() -> clazz.getResourceAsStream(filename), clazz, filename);
    }

    // ----------------- 私有方法

    private static InputStream readJar(final String filename) {
        return readSupplier(() -> {
            try {
                final URL url = new URL(filename);
                final String protocol = url.getProtocol();
                if (VPath.PROTOCOL.JAR.equals(protocol)) {
                    final JarURLConnection jarCon = (JarURLConnection) url.openConnection();
                    return jarCon.getInputStream();
                } else {
                    return null; // Jar Error
                }
            } catch (final IOException e) {
                // TODO: Log.fatal(Stream.class, e);
                e.printStackTrace();
                return null;
            }
        }, filename);
    }

    private static InputStream readSupplier(final Supplier<InputStream> supplier,
                                            final String filename) {
        final InputStream in = supplier.get();
        if (null != in) {
            LOG.io(INFO.IoStream.INF_PATH, filename, in);
        }
        return in;
    }
}
