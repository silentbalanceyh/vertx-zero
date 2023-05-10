package io.vertx.quiz;

import io.horizon.uca.log.Annal;
import io.horizon.uca.qr.Criteria;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.config.Database;
import io.vertx.up.commune.config.Integration;
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
 * The API is as following（All Api parameters are `filename`）:
 *
 * 1. ioString(filename) - String Content
 * 2. ioBuffer(filename) - Buffer Content
 * 3. ioJObject(filename) - Json Object Content
 * 4. ioJArray(filename) - Json Array Content
 * 5. ioDatabase(filename) - Database from file ( Json Format )
 * 6. ioIntegration(filename) - Integration from file ( Json Format )
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class EpicBase {

    protected String ioString(final String filename) {
        final Class<?> clazz = this.getClass();
        final String file = "test/" + clazz.getPackage().getName() + "/" + filename;
        this.logger().info("[ Tc ] Test input file reading from: {0}", file);
        return file;
    }

    protected Buffer ioBuffer(final String filename) {
        return Ut.ioBuffer(this.ioString(filename));
    }

    protected JsonObject ioJObject(final String filename) {
        return Ut.ioJObject(this.ioString(filename));
    }

    protected Database ioDatabase(final String filename) {
        final JsonObject fileJson = this.ioJObject(filename);
        final Database database = new Database();
        database.fromJson(fileJson);
        return database;
    }

    protected Integration ioIntegration(final String filename) {
        final JsonObject fileJson = this.ioJObject(filename);
        final Integration integration = new Integration();
        integration.fromJson(fileJson);
        return integration;
    }

    protected Criteria ioCriteria(final String filename) {
        return Criteria.create(this.ioJObject(filename));
    }

    protected JsonArray ioJArray(final String filename) {
        return Ut.ioJArray(this.ioString(filename));
    }

    protected Annal logger() {
        return Annal.get(this.getClass());
    }

}
