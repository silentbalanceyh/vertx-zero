package cn.vertxup.ambient.service;

import io.horizon.uca.cache.Cc;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.ambient.uca.digital.*;
import io.vertx.up.eon.KName;

import static io.vertx.mod.ambient.refine.At.LOG;

public class DatumService implements DatumStub {

    private static final Cc<String, Aide> CC_AIDE = Cc.open();
    private static final Cc<String, Tree> CC_TREE = Cc.open();
    private static final Cc<String, SerialGen> CC_SERIAL = Cc.open();

    // ------------------------ Dict Operation Api
    @Override
    public Future<JsonArray> dictApp(final String appId, final String type) {
        final Aide aide = CC_AIDE.pick(AideApp::new, appId); // Fn.po?l(POOL_AIDE, appId, AideApp::new);
        return aide.fetch(appId, new JsonArray().add(type));
    }

    @Override
    public Future<JsonArray> dictApp(final String appId, final JsonArray types) {
        final Aide aide = CC_AIDE.pick(AideApp::new, appId); // Fn.po?l(POOL_AIDE, appId, AideApp::new);
        return aide.fetch(appId, types);
    }

    @Override
    public Future<JsonObject> dictApp(final String appId, final String type, final String code) {
        final Aide aide = CC_AIDE.pick(AideApp::new, appId); // Fn.po?l(POOL_AIDE, appId, AideApp::new);
        return aide.fetch(appId, type, code);
    }

    @Override
    public Future<JsonArray> dictSigma(final String sigma, final JsonArray types) {
        final Aide aide = CC_AIDE.pick(AideSigma::new, sigma); // Fn.po?l(POOL_AIDE, sigma, AideSigma::new);
        return aide.fetch(sigma, types);
    }

    @Override
    public Future<JsonArray> dictSigma(final String sigma, final String type) {
        final Aide aide = CC_AIDE.pick(AideSigma::new, sigma); // Fn.po?l(POOL_AIDE, sigma, AideSigma::new);
        return aide.fetch(sigma, new JsonArray().add(type));
    }

    @Override
    public Future<JsonObject> dictSigma(final String sigma, final String type, final String code) {
        final Aide aide = CC_AIDE.pick(AideSigma::new, sigma); // Fn.po?l(POOL_AIDE, sigma, AideSigma::new);
        return aide.fetch(sigma, type, code);
    }

    // ------------------------ Tree Operation Api
    @Override
    public Future<JsonArray> treeApp(final String appId, final String type, final Boolean leaf) {
        final Tree tree = CC_TREE.pick(TreeApp::new, appId); // Fn.po?l(POOL_TREE, appId, TreeApp::new);
        return tree.fetch(appId, type, leaf);
    }

    @Override
    public Future<JsonArray> treeApp(final String appId, final JsonArray types) {
        final Tree tree = CC_TREE.pick(TreeApp::new, appId); // Fn.po?l(POOL_TREE, appId, TreeApp::new);
        return tree.fetch(appId, types);
    }


    @Override
    public Future<JsonObject> treeApp(final String appId, final String type, final String code) {
        final Tree tree = CC_TREE.pick(TreeApp::new, appId); // Fn.po?l(POOL_TREE, appId, TreeApp::new);
        return tree.fetch(appId, type, code);
    }

    @Override
    public Future<JsonArray> treeSigma(final String sigma, final String type, final Boolean leaf) {
        final Tree tree = CC_TREE.pick(TreeSigma::new, sigma); // Fn.po?l(POOL_TREE, sigma, TreeSigma::new);
        return tree.fetch(sigma, type, leaf);
    }

    @Override
    public Future<JsonArray> treeSigma(final String sigma, final JsonArray types) {
        final Tree tree = CC_TREE.pick(TreeSigma::new, sigma); // Fn.po?l(POOL_TREE, sigma, TreeSigma::new);
        return tree.fetch(sigma, types);
    }

    @Override
    public Future<JsonObject> treeSigma(final String sigma, final String type, final String code) {
        final Tree tree = CC_TREE.pick(TreeSigma::new, sigma); // Fn.po?l(POOL_TREE, sigma, TreeSigma::new);
        return tree.fetch(sigma, type, code);
    }

    // ------------------------ Number Generation
    @Override
    public Future<JsonArray> numberApp(final String appId, final String code, final Integer count) {
        LOG.Flow.info(this.getClass(), "Serial Gen: appId = {0}, code = {1}, count = {2}", appId, code, count);
        // APP_ID = ? AND CODE = ?
        final JsonObject condition = new JsonObject();
        condition.put(KName.APP_ID, appId).put(KName.CODE, code);

        final Serial serial = CC_SERIAL.pick(SerialGen::new, appId); // Fn.po?l(POOL_SERIAL, appId, SerialGen::new);
        return serial.generate(condition, count);
    }


    @Override
    public Future<JsonArray> numberAppI(final String appId, final String identifier, final Integer count) {
        LOG.Flow.info(this.getClass(), "Serial Gen: appId = {0}, identifier = {1}, count = {2}", appId, identifier, count);
        // APP_ID = ? AND IDENTIFIER = ?
        final JsonObject condition = new JsonObject();
        condition.put(KName.APP_ID, appId).put(KName.IDENTIFIER, identifier);

        final Serial serial = CC_SERIAL.pick(SerialGen::new, appId); // Fn.po?l(POOL_SERIAL, appId, SerialGen::new);
        return serial.generate(condition, count);
    }

    @Override
    public Future<JsonArray> numberSigma(final String sigma, final String code, final Integer count) {
        LOG.Flow.info(this.getClass(), "Serial Gen: sigma = {0}, code = {1}, count = {2}", sigma, code, count);
        // SIGMA = ? AND CODE = ?
        final JsonObject condition = new JsonObject();
        condition.put(KName.SIGMA, sigma).put(KName.CODE, code);

        final Serial serial = CC_SERIAL.pick(SerialGen::new, sigma); // Fn.po?l(POOL_SERIAL, sigma, SerialGen::new);
        return serial.generate(condition, count);
    }

    @Override
    public Future<JsonArray> numberSigmaI(final String sigma, final String identifier, final Integer count) {
        LOG.Flow.info(this.getClass(), "Serial Gen: sigma = {0}, identifier = {1}, count = {2}", sigma, identifier, count);
        // SIGMA = ? AND IDENTIFIER = ?
        final JsonObject condition = new JsonObject();
        condition.put(KName.SIGMA, sigma).put(KName.IDENTIFIER, identifier);

        final Serial serial = CC_SERIAL.pick(SerialGen::new, sigma); // Fn.po?l(POOL_SERIAL, sigma, SerialGen::new);
        return serial.generate(condition, count);
    }

    @Override
    public Future<Boolean> numberAppR(final String appId, final String code, final Long defaultValue) {
        LOG.Flow.info(this.getClass(), "Serial Reset: appId = {0}, code = {1}, default = {2}", appId, code, String.valueOf(defaultValue));

        final JsonObject condition = new JsonObject();
        condition.put(KName.APP_ID, appId).put(KName.CODE, code);

        final Serial serial = CC_SERIAL.pick(SerialGen::new, appId); // Fn.po?l(POOL_SERIAL, appId, SerialGen::new);
        return serial.reset(condition, defaultValue);
    }

    @Override
    public Future<Boolean> numberSigmaR(final String sigma, final String code, final Long defaultValue) {
        LOG.Flow.info(this.getClass(), "Serial Reset: sigma = {0}, code = {1}, default = {2}", sigma, code, String.valueOf(defaultValue));

        final JsonObject condition = new JsonObject();
        condition.put(KName.SIGMA, sigma).put(KName.CODE, code);

        final Serial serial = CC_SERIAL.pick(SerialGen::new, sigma); // Fn.po?l(POOL_SERIAL, sigma, SerialGen::new);
        return serial.reset(condition, defaultValue);
    }
}
