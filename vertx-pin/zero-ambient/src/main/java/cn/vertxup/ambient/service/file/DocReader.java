package cn.vertxup.ambient.service.file;

import cn.vertxup.ambient.service.DatumStub;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ambient.atom.AtConfig;
import io.vertx.tp.ambient.init.AtPin;
import io.vertx.tp.ambient.refine.At;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.optic.business.ExIo;
import io.vertx.tp.optic.business.ExUser;
import io.vertx.tp.optic.feature.Arbor;
import io.vertx.tp.optic.feature.Attachment;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class DocReader implements DocRStub {
    private static final ConcurrentMap<String, Arbor> POOL_ARBOR = new ConcurrentHashMap<>();
    private static final Annal LOGGER = Annal.get(DocReader.class);
    @Inject
    private transient DatumStub stub;
    @Inject
    private transient Attachment attachment;

    // ------------------------- Document Management Tree -------------------------
    /*
     * Here the parameters are mapped to
     * -- appId: X_APP -> KEY
     * -- type:  X_CATEGORY -> TYPE
     */
    @Override
    public Future<JsonArray> treeDir(final String appId, final String type) {
        return this.stub.treeApp(appId, type).compose(categories -> {
            final List<Future<JsonArray>> futures = new ArrayList<>();
            Ut.itJArray(categories).map(this::seekAsync).forEach(futures::add);
            return Ux.thenCombineArray(futures);
        });
    }

    /*
     * Each category node should contain two dim operations:
     * -- treeComponent/treeConfig
     * -- runComponent/runConfig
     *
     * Terminal when comment in following situations
     * -- 1. treeComponent is null or it's not implement from Arbor interface.
     */
    private Future<JsonArray> seekAsync(final JsonObject input) {
        final String runComponent = input.getString(KName.Component.TREE_COMPONENT);
        final Class<?> arborCls = Ut.clazz(runComponent, null);


        // Terminal 1:
        if (Objects.isNull(arborCls) || !Ut.isImplement(arborCls, Arbor.class)) {
            return Ux.futureA();
        }


        final JsonObject configuration = input.getJsonObject(KName.Component.TREE_CONFIG);
        JsonObject storeRef = configuration.getJsonObject(KName.STORE);
        if (Ut.isNil(storeRef)) {
            storeRef = new JsonObject();
        }
        /*
         * The configuration data came from `treeConfig`, combine the configuration information attached into
         * {
         *      "store": {
         *          "storePath": "",
         *          "runComponent": "Default `Fs` interface component that will be stored into I_DIRECTORY",
         *          "initialize": {
         *              "field1": "value1",
         *              "field2": "value2",
         *              "...": "..."
         *          }
         *      }
         * }
         */
        final AtConfig config = AtPin.getConfig();
        storeRef.put(KName.STORE_PATH, config.getStorePath());
        configuration.put(KName.STORE, storeRef);

        final Arbor arbor = Fn.poolThread(POOL_ARBOR, () -> Ut.instance(arborCls), arborCls.getName());
        At.infoFile(LOGGER, "Arbor = {0}, Configuration = {1}", arborCls.getName(), configuration.encode());
        return arbor.generate(input, configuration);
    }

    // ------------------------- Document Method ( Other ) -------------------------

    @Override
    public Future<JsonArray> fetchDoc(final String sigma, final String directoryId) {
        Objects.requireNonNull(sigma);
        return Ke.channel(ExIo.class, JsonArray::new, io -> io.dirRun(sigma, directoryId)).compose(directory -> {
            final JsonObject condition = Ux.whereAnd();
            condition.put(KName.DIRECTORY_ID, directoryId);
            // active = true
            condition.put(KName.ACTIVE, Boolean.TRUE);
            condition.put(KName.SIGMA, sigma);
            return this.attachment.fetchAsync(condition).compose(files -> {
                directory.addAll(files);
                return Ux.future(directory);
            });
        });
    }

    @Override
    public Future<JsonArray> fetchTrash(final String sigma) {
        Objects.requireNonNull(sigma);
        return Ke.channel(ExIo.class, JsonArray::new, io -> io.dirTrash(sigma)).compose(directory -> {
            final JsonObject condition = Ux.whereAnd();
            // active = false
            condition.put(KName.ACTIVE, Boolean.FALSE);
            condition.put(KName.SIGMA, sigma);
            return this.attachment.fetchAsync(condition).compose(files -> {
                directory.addAll(files);
                return Ux.future(directory);
            });
        });
    }

    @Override
    public Future<JsonArray> searchDoc(final String sigma, final String keyword) {
        Objects.requireNonNull(sigma);
        /* Attachment Only */
        final JsonObject condition = Ux.whereAnd();
        condition.put(KName.SIGMA, sigma);
        condition.put(KName.ACTIVE, Boolean.TRUE);
        /*
         * createdBy is the owner of attachment record because here the attachment
         * file could not be updated, there are two operation only:
         * 1 - Upload
         * 2 - Replaced
         *  */
        return Ke.channel(ExUser.class, JsonArray::new, user -> user.auditor(keyword)).compose(keys -> {
            if (Ut.notNil(keys)) {
                // User Matched
                final JsonObject criteria = Ux.whereOr();
                criteria.put(KName.NAME + ",c", keyword);
                criteria.put(KName.CREATED_BY + ",i", keys);
                condition.put("$Qr$", criteria);
            } else {
                condition.put(KName.NAME + ",c", keyword);
            }
            return this.attachment.fetchAsync(condition);
        });
    }

    // ------------------------- Document Method ( Download ) -------------------------

    @Override
    public Future<Buffer> downloadDoc(final String key) {
        return null;
    }

    @Override
    public Future<Buffer> downloadDoc(final Set<String> keys) {
        return null;
    }
}
