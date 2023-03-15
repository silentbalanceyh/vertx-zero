package io.vertx.up.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.Strings;
import io.vertx.up.eon.Values;
import io.vertx.up.eon.em.YamlType;
import io.vertx.up.exception.heart.EmptyStreamException;
import io.vertx.up.exception.heart.JsonFormatException;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * The library for IO resource reading.
 */
final class IO {
    /**
     * Yaml
     **/
    private static final ObjectMapper YAML = new YAMLMapper();

    /**
     * 「DEAD-LOCK」LoggerFactory.getLogger
     * Do not use `Annal` logger because of deadlock.
     */
    private static final Logger LOGGER
        = LoggerFactory.getLogger(IO.class);

    private IO() {
    }

    /**
     * Read to JsonArray
     *
     * @param filename The filename to describe source path
     *
     * @return Return to JsonArray object
     */
    static JsonArray getJArray(final String filename) {
        return Fn.outRun(() -> new JsonArray(getString(filename)),
            JsonFormatException.class, filename);
    }

    /**
     * Read to JsonObject
     *
     * @param filename The filename to describe source path
     *
     * @return Return to JsonObject
     */
    static JsonObject getJObject(final String filename) {
        final String content = getString(filename);
        // TODO: For debug
        return Fn.outRun(() -> new JsonObject(content),
            JsonFormatException.class, filename);
    }

    /**
     * Read to String
     *
     * @param in input stream
     *
     * @return converted stream
     */
    static String getString(final InputStream in) {
        return getString(in, null);
    }

    static String getString(final InputStream in, final String joined) {
        final StringBuilder buffer = new StringBuilder(Values.BUFFER_SIZE);
        return Fn.orJvm(() -> {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(in, Values.ENCODING));
            // Character stream
            String line;
            while (null != (line = reader.readLine())) {
                buffer.append(line);
                if (Ut.notNil(joined)) {
                    buffer.append(joined);
                }
            }
            in.close();
            reader.close();
            return buffer.toString();
        }, in);
    }

    static String getString(final String filename, final String joined) {
        return Fn.orJvm(() -> getString(Stream.read(filename), joined), filename);
    }

    static String getString(final String filename) {
        return Fn.orJvm(() -> getString(Stream.read(filename)), filename);
    }

    /**
     * Read yaml to JsonObject
     *
     * @param filename input filename
     *
     * @return Deserialized type of T
     */
    @SuppressWarnings("unchecked")
    static <T> T getYaml(final String filename) {
        if (Ut.isNil(filename)) {
            /*
             * If filename is null or empty
             * return to null reference for future usage
             */
            return null;
        } else {
            final YamlType type = getYamlType(filename);
            final String literal = getYamlNode(filename).toString();
            if (Ut.isNil(literal)) {
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
    }

    private static JsonNode getYamlNode(final String filename) {
        final InputStream in = Stream.read(filename);
        final JsonNode node = Fn.safeJvm(() -> {
            if (null == in) {
                throw new EmptyStreamException(filename);
            }
            return YAML.readTree(in);
        }, null);
        if (null == node) {
            throw new EmptyStreamException(filename);
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
        final String content = IO.getString(filename);
        return Fn.orNull(YamlType.OBJECT, () -> {
            if (content.trim().startsWith(Strings.DASH)) {
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
    static Properties getProp(final String filename) {
        return Fn.orJvm(() -> {
            final Properties prop = new Properties();
            final InputStream in = Stream.read(filename);
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
    static URL getURL(final String filename) {
        return Fn.orJvm(() -> {
            final URL url = Thread.currentThread().getContextClassLoader()
                .getResource(filename);
            return Fn.orSemi(null == url, null,
                () -> IO.class.getResource(filename),
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
    static Buffer getBuffer(final String filename) {
        final InputStream in = Stream.read(filename);
        return Fn.orJvm(() -> {
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
    static File getFile(final String filename) {
        return Fn.orJvm(() -> {
            final File file = new File(filename);
            return Fn.orSemi(file.exists(), null,
                () -> file,
                () -> {
                    final URL url = getURL(filename);
                    if (null == url) {
                        throw new EmptyStreamException(filename);
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
            final URL url = getURL(filename);
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
    static String getPath(final String filename) {
        return Fn.orJvm(() -> {
            final File file = getFile(filename);
            return Fn.orJvm(() -> {
                Log.info(LOGGER, Info.INF_APATH, file.getAbsolutePath());
                return file.getAbsolutePath();
            }, file);
        }, filename);
    }

    static String getCompress(final String file) {
        final byte[] bytes = Stream.readBytes(file);
        final byte[] compressed = Compressor.decompress(bytes);
        return new String(compressed, Values.DEFAULT_CHARSET);
    }

    static Buffer zip(final Set<String> fileSet) {
        // Create Tpl zip file path
        return Fn.orJvm(() -> {
            final ByteArrayOutputStream fos = new ByteArrayOutputStream();
            final ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(fos));

            // Buffer Size
            final byte[] buffers = new byte[Values.CACHE_SIZE];
            fileSet.forEach(filename -> Fn.safeJvm(() -> {
                // create .zip and put file here
                final File file = new File(filename);
                final ZipEntry zipEntry = new ZipEntry(file.getName());
                zos.putNextEntry(zipEntry);

                // Read File content and put them in the zip
                final FileInputStream fis = new FileInputStream(file);
                final BufferedInputStream bis = new BufferedInputStream(fis, Values.CACHE_SIZE);
                int read;
                while ((read = bis.read(buffers, 0, Values.CACHE_SIZE)) != -1) {
                    zos.write(buffers, 0, read);
                }
                bis.close();
            }));
            zos.close();
            return Buffer.buffer(fos.toByteArray());
        });
    }
}
