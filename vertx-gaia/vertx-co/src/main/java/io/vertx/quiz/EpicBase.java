package io.vertx.quiz;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

/**
 * # 「Co」 Testing Framework
 *
 * This class is testing base class to read file resources, here are specification in zero framework for testing
 *
 * 1. The resource path name should be `src/test/resources/test/[package]/[filename]`.
 * 2. The `package` name is the same as current class, the root folder is `test` in testing resources.
 * 3. You can provide `filename` that will be used in testing classes.
 *
 * Here support three file format
 *
 * 1. String
 * 2. JsonObject
 * 3. JsonArray
 *
 * And so on, it provide testing logger for developers to record all the testing logs.
 *
 * This class is for JUnit purely
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class EpicBase {
    /**
     * Get file content into String
     *
     * @param filename the filename that you input
     *
     * @return String content
     */
    protected String ioFile(final String filename) {
        final Class<?> clazz = this.getClass();
        final String file = "test/" + clazz.getPackage().getName() + "/" + filename;
        this.getLogger().info("[ Sim ] Test input file reading from: {0}", file);
        return file;
    }

    /**
     * Get file content into JsonObject
     *
     * @param filename the filename that you input
     *
     * @return Buffer content
     */
    protected Buffer ioBuffer(final String filename) {
        return Ut.ioBuffer(this.ioFile(filename));
    }

    /**
     * Get file content into JsonObject
     *
     * @param filename the filename that you input
     *
     * @return JsonObject content
     */
    protected JsonObject ioJObject(final String filename) {
        return Ut.ioJObject(this.ioFile(filename));
    }

    /**
     * Get file content into JsonArray
     *
     * @param filename the filename that you input
     *
     * @return JsonArray content
     */
    protected JsonArray ioJArray(final String filename) {
        return Ut.ioJArray(this.ioFile(filename));
    }

    /**
     * The logger that could be initialized based on class, it could be used in sub-classes
     *
     * @return logger reference
     */
    protected Annal getLogger() {
        return Annal.get(this.getClass());
    }
}
