package io.vertx.tp.atom.refine;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.cv.AoCache;
import io.vertx.tp.atom.modeling.Model;
import io.vertx.tp.atom.modeling.Schema;
import io.vertx.tp.atom.modeling.data.DataAtom;
import io.vertx.tp.modular.dao.AoDao;
import io.vertx.tp.modular.jdbc.Pin;
import io.vertx.tp.optic.robin.Switcher;
import io.vertx.up.commune.config.Database;
import io.vertx.up.commune.config.Identity;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.function.Supplier;

class AoImpl {

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

    static Model toModel(final String appName, final JsonObject modelJson) {
        final Model modelObj = toModel(appName);
        modelObj.fromJson(modelJson);
        return modelObj;
    }

    static Model toModel(final String appName, final String file) {
        final Model modelObj = toModel(appName);
        modelObj.fromFile(file);
        return modelObj;
    }

    static Switcher toSwitcher(final Identity identity, final JsonObject options) {
        return Fn.pool(AoCache.POOL_SWITCHER, identity.hashCode(), () -> {
            final Class<?> implSwitcher = AoStore.clazzSwitcher();
            return Ut.instance(implSwitcher, identity, options);
        });
    }

    private static Schema toSchema(final String appName) {
        final Class<?> implSchema = AoStore.clazzSchema();
        return Ut.instance(implSchema, AoStore.toNamespace(appName));
    }

    private static Model toModel(final String appName) {
        final Class<?> implModel = AoStore.clazzModel();
        return Ut.instance(implModel, AoStore.toNamespace(appName));
    }

    static DataAtom toAtom(final JsonObject options) {
        final String identifier = options.getString(KName.IDENTIFIER);
        final String name = options.getString(KName.NAME);
        return DataAtom.get(name, identifier);
    }


    static AoDao toDao(final Database database, final DataAtom atom) {
        return toDao(database, () -> atom);
    }

    static AoDao toDao(final Database database, final Supplier<DataAtom> supplier) {
        if (Objects.isNull(database)) {
            return null;
        } else {
            final DataAtom atom = supplier.get();
            if (Objects.isNull(atom)) {
                return null;
            } else {
                final Pin pin = Pin.getInstance();
                return Fn.poolThread(AoCache.POOL_T_DAO, () -> pin.getDao(database).mount(atom), atom.identifier());
            }
        }
    }
}
