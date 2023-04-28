package io.horizon.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import io.horizon.eon.VString;
import io.horizon.eon.VValue;
import io.horizon.eon.em.typed.YamlType;
import io.horizon.exception.internal.EmptyIoException;
import io.horizon.exception.internal.JsonFormatException;
import io.horizon.fn.HFn;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * The library for IO resource reading.
 */
final class Io {
    /**
     * Yaml
     **/
    private static final ObjectMapper YAML = new YAMLMapper();

    /**
     * 「DEAD-LOCK」LoggerFactory.getLogger
     * Do not use `Annal` logger because of deadlock.
     */
    private static final LogUtil LOG = LogUtil.from(Io.class);

    private Io() {
    }

    static JsonArray ioJArray(final String filename) {
        final JsonArray content;
        try {
            content = new JsonArray(ioString(filename, null));
        } catch (final Throwable ex) {
            throw new JsonFormatException(Io.class, filename);
        }
        return content;
    }

    static JsonObject ioJObject(final String filename) {
        final JsonObject content;
        try {
            content = new JsonObject(ioString(filename, null));
        } catch (final Throwable ex) {
            throw new JsonFormatException(Io.class, filename);
        }
        return content;
    }

    static String ioString(final InputStream in, final String joined) {
        final StringBuilder buffer = new StringBuilder();
        return HFn.failOr(() -> {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            // Character stream
            String line;
            while (null != (line = reader.readLine())) {
                buffer.append(line);
                if (!TIs.isNil(joined)) {
                    buffer.append(joined);
                }
            }
            in.close();
            reader.close();
            return buffer.toString();
        }, in);
    }

    static String ioString(final String filename, final String joined) {
        return HFn.failOr(() -> ioString(IoStream.read(filename), joined), filename);
    }

    /**
     * Read yaml to JsonObject
     *
     * @param filename input filename
     *
     * @return Deserialized type of T
     */
    @SuppressWarnings("unchecked")
    static <T> T ioYaml(final String filename) {
        if (TIs.isNil(filename)) {
            /*
             * If filename is null or empty
             * return to null reference for future usage
             */
            return null;
        }
        final YamlType type = getYamlType(filename);
        if (Objects.isNull(type)) {
            return null;
        }
        final String literal = getYamlNode(filename).toString();
        if (TIs.isNil(literal)) {
            /*
             * If content is null or empty
             * return to null reference for future usage
             */
            return null;
        } else {
            if (YamlType.ARRAY == type) {
                return (T) new JsonArray(literal);
            } else {
                return (T) new JsonObject(literal);
            }
        }
    }

    private static JsonNode getYamlNode(final String filename) {
        final InputStream in = IoStream.read(filename);
        final JsonNode node = HFn.failOr(() -> {
            if (null == in) {
                throw new EmptyIoException(Io.class, filename);
            }
            return YAML.readTree(in);
        });
        if (null == node) {
            throw new EmptyIoException(Io.class, filename);
        }
        return node;
    }

    /**
     * Check yaml type
     *
     * @param filename input file name
     *
     * @return YamlType of the file by format
     */
    private static YamlType getYamlType(final String filename) {
        final String content = ioString(filename, null);
        return HFn.runOr(YamlType.OBJECT, () -> {
            if (content.trim().startsWith(VString.DASH)) {
                return YamlType.ARRAY;
            } else {
                return YamlType.OBJECT;
            }
        }, content);
    }

    /**
     * Read to property object
     *
     * @param filename input filename
     *
     * @return Properties that will be returned
     */
    static Properties ioProp(final String filename) {
        return HFn.failOr(() -> {
            final Properties prop = new Properties();
            final InputStream in = IoStream.read(filename);
            prop.load(in);
            in.close();
            return prop;
        }, filename);
    }

    /**
     * Read to URL
     *
     * @param filename input filename
     *
     * @return URL of this filename include ZIP/JAR url
     */
    static URL ioURL(final String filename) {
        return HFn.failOr(() -> {
            final URL url = Thread.currentThread().getContextClassLoader()
                .getResource(filename);
            return HFn.runOr(null == url, null,
                () -> Io.class.getResource(filename),
                () -> url);
        }, filename);
    }

    /**
     * Read to Buffer
     *
     * @param filename input filename
     *
     * @return Buffer from filename
     */
    @SuppressWarnings("all")
    static Buffer ioBuffer(final String filename) {
        final InputStream in = IoStream.read(filename);
        return HFn.failOr(() -> {
            final byte[] bytes = new byte[in.available()];
            in.read(bytes);
            in.close();
            return Buffer.buffer(bytes);
        }, in);
    }

    /**
     * Read to File
     *
     * @param filename input filename
     *
     * @return File object by filename that input
     */
    static File ioFile(final String filename) {
        return HFn.failOr(() -> {
            final File file = new File(filename);
            return HFn.runOr(file.exists(), null,
                () -> file,
                () -> {
                    final URL url = ioURL(filename);
                    if (null == url) {
                        throw new EmptyIoException(Io.class, filename);
                    }
                    return new File(url.getFile());
                });
        }, filename);
    }

    static boolean isExist(final String filename) {
        try {
            final File file = new File(filename);
            if (file.exists()) {
                return true;
            }
            final URL url = ioURL(filename);
            return Objects.nonNull(url);
        } catch (final Throwable ex) {
            // Fix: java.lang.NullPointerException
            // File does not exist
            return false;
        }
    }

    /**
     * Read to Path
     *
     * @param filename input filename
     *
     * @return file content that converted to String
     */
    static String ioPath(final String filename) {
        return HFn.failOr(() -> {
            final File file = ioFile(filename);
            return HFn.failOr(() -> {
                LOG.io(__Message.HIo.INF_PATH, file.getAbsolutePath());
                return file.getAbsolutePath();
            }, file);
        }, filename);
    }

    static String ioCompress(final String file) {
        final byte[] bytes = IoStream.readBytes(file);
        final byte[] compressed = IoCompressor.decompress(bytes);
        return new String(compressed, VValue.DFT.CHARSET);
    }

    static Buffer zip(final Set<String> fileSet) {
        // Create Tpl zip file path
        return HFn.failOr(() -> {
            final ByteArrayOutputStream fos = new ByteArrayOutputStream();
            final ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(fos));

            // Buffer Size
            final byte[] buffers = new byte[VValue.DFT.SIZE_BYTE_ARRAY];
            fileSet.forEach(filename -> HFn.jvmAt(() -> {
                // create .zip and put file here
                final File file = new File(filename);
                final ZipEntry zipEntry = new ZipEntry(file.getName());
                zos.putNextEntry(zipEntry);

                // Read File content and put them in the zip
                final FileInputStream fis = new FileInputStream(file);
                final BufferedInputStream bis = new BufferedInputStream(fis, VValue.DFT.SIZE_BYTE_ARRAY);
                int read;
                while ((read = bis.read(buffers, 0, VValue.DFT.SIZE_BYTE_ARRAY)) != -1) {
                    zos.write(buffers, 0, read);
                }
                bis.close();
            }));
            zos.close();
            return Buffer.buffer(fos.toByteArray());
        });
    }
}
