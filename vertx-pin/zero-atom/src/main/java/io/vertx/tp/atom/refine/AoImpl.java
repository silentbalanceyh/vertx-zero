package io.vertx.tp.atom.refine;

import io.aeon.experiment.specification.power.KApp;
import io.horizon.specification.modeler.HAtom;
import io.horizon.specification.modeler.HDao;
import io.horizon.specification.modeler.HRecord;
import io.vertx.aeon.specification.app.HES;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.Model;
import io.vertx.tp.atom.modeling.Schema;
import io.vertx.tp.atom.modeling.builtin.DataAtom;
import io.vertx.tp.atom.modeling.data.DataRecord;
import io.vertx.tp.modular.jdbc.Pin;
import io.vertx.tp.optic.mixture.HLoadAtom;
import io.vertx.tp.optic.robin.Switcher;
import io.vertx.tp.plugin.database.DS;
import io.vertx.tp.plugin.database.DataPool;
import io.vertx.up.commune.config.Database;
import io.vertx.up.commune.config.Identity;
import io.vertx.up.eon.KName;
import io.vertx.up.experiment.mixture.HLoad;
import io.vertx.up.uca.cache.Cc;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.function.Supplier;

class AoImpl {
    // 模型读取器
    public static final Cc<String, HLoad> CC_LOAD = Cc.openThread();
    private static final Cc<Integer, Switcher> CC_SWITCHER = Cc.open();
    private static final Cc<String, HDao> CC_T_DAO = Cc.openThread();

    /*
     * Private Method for Schema / Model
     */
    private static Schema toSchema(final String appName) {
        final Class<?> implSchema = AoStore.clazzSchema();
        return Ut.instance(implSchema, AoStore.namespace(appName));
    }

    /*
     * - Schema
     *   toSchema(String, JsonObject)
     *   toSchema(String, String)
     *
     * - Model
     *   toModel(String, JsonObject)
     *   toModel(String, String)
     *
     * - Switcher
     *   toSwitcher(Identity, JsonObject)
     */
    static Schema toSchema(final String appName, final JsonObject schemaJson) {
        final Schema schemaObj = toSchema(appName);
        schemaObj.fromJson(schemaJson);
        return schemaObj;
    }

    static Schema toSchema(final String appName, final String file) {
        final Schema schemaObj = toSchema(appName);
        schemaObj.fromFile(file);
        return schemaObj;
    }

    static Model toModel(final String appName) {
        final KApp app = HES.connect(appName).bind(AoStore.namespace(appName));
        // H3H.CC_APP.pick(() -> new KApp(appName).bind(AoStore.namespace(appName)), appName);
        final Class<?> implModel = AoStore.clazzModel();
        return Ut.instance(implModel, app);
    }

    static Switcher toSwitcher(final Identity identity, final JsonObject options) {
        return CC_SWITCHER.pick(() -> {
            final Class<?> implSwitcher = AoStore.clazzSwitcher();
            return Ut.instance(implSwitcher, identity, options);
        }, identity.hashCode());
        /*
        Fn.po?l(AoCache.POOL_SWITCHER, identity.hashCode(), () -> {
            final Class<?> implSwitcher = AoStore.clazzSwitcher();
            return Ut.instance(implSwitcher, identity, options);
        });*/
    }

    // ------------------- Dao / Atom -----------------

    /*
     * - DataAtom
     *   toAtom(JsonObject)
     * - AoDao
     */
    static DataAtom toAtom(final JsonObject options) {
        final String identifier = options.getString(KName.IDENTIFIER);
        final String name = options.getString(KName.NAME);
        return toAtom(name, identifier);
    }

    static DataAtom toAtom(final String appName, final String identifier) {
        final HLoad loader = CC_LOAD.pick(HLoadAtom::new);
        final HAtom atom = loader.atom(appName, identifier);
        if (atom instanceof DataAtom) {
            return (DataAtom) atom;
        } else {
            return null;
        }
    }

    static HDao toDao(final HAtom atom) {
        return Ux.channelS(DS.class, ds -> {
            /* 连接池绑定数据库 */
            final DataPool pool = ds.switchDs(atom.sigma());
            if (Objects.nonNull(pool)) {
                /* 返回AoDao */
                final Database database = pool.getDatabase();
                return toDao(() -> atom, database);
            } else {
                return null;
            }
        });
    }

    static HDao toDao(final Supplier<HAtom> supplier, final Database database) {
        if (Objects.isNull(database)) {
            return null;
        } else {
            final HAtom atom = supplier.get();
            if (Objects.isNull(atom)) {
                return null;
            } else {
                final Pin pin = Pin.getInstance();
                return CC_T_DAO.pick(() -> pin.getDao(database).mount(atom), atom.identifier());
                // return Fn.po?lThread(AoCache.POOL_T_DAO, () -> pin.getDao(database).mount(atom), atom.identifier());
            }
        }
    }

    // ----------------- To Current ------------------
    static DataAtom toAtom(final String identifier) {
        final KApp env = HES.connect();
        if (Objects.nonNull(env)) {
            return toAtom(env.name(), identifier);
        } else {
            return null;
        }
    }

    static HRecord toRecord(final String identifier, final JsonObject data) {
        final DataAtom atom = toAtom(identifier);
        if (Objects.isNull(atom)) {
            return null;
        }
        final HRecord record = new DataRecord();
        Ut.contract(record, DataAtom.class, atom);
        return Ux.updateR(record, data);
    }

    static HDao toDao(final String identifier) {
        final KApp env = HES.connect();
        final DataAtom atom;
        if (Objects.nonNull(env)) {
            atom = toAtom(env.name(), identifier);
        } else {
            atom = null;
        }
        if (Objects.nonNull(atom)) {
            return toDao(() -> atom, env.database());
        } else {
            return null;
        }
    }
}
