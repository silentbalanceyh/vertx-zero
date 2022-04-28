package io.vertx.up.experiment.specification;

import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.exception.web._404ModelNotFoundException;
import io.vertx.up.exception.web._409IdentifierConflictException;
import io.vertx.up.experiment.mixture.HApp;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.Objects;

/**
 * M/U means modal unit, its format is following, please refer the related definition.yml file and definition.json
 * file for more details of KClass structure.
 *
 * KClass will be shared data structure ( JsonObject Part ) with KModule in up framework
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class KClass implements Serializable {
    private final String identifier;
    private final String appName;
    private final KHybrid hybrid;
    private final KModule module;

    private KClass(final String appName, final JsonObject configuration) {
        this.appName = appName;

        this.identifier = configuration.getString(KName.IDENTIFIER);

        final JsonObject moduleJ = Ut.valueJObject(configuration, KName.MODULE);
        this.module = Ut.deserialize(moduleJ, KModule.class);

        final JsonObject hybrid = Ut.valueJObject(configuration, "hybrid");
        this.hybrid = KHybrid.create(hybrid);
    }

    public static KClass create(final String appName, final String identifier) {
        Objects.requireNonNull(identifier);
        final String ns = HApp.ns(appName);
        final String fileCls = "hybird/" + identifier + ".json";
        if (!Ut.ioExist(fileCls)) {
            throw new _404ModelNotFoundException(KClass.class, ns, identifier);
        }
        // JsonObject read
        final JsonObject classJ = Ut.ioJObject(fileCls);
        final Object valueJ = classJ.getValue(KName.MODULE);
        final JsonObject moduleJ = new JsonObject();
        if (valueJ instanceof String) {
            final String fileMod = (String) valueJ;
            if (!Ut.ioExist(fileMod)) {
                throw new _404ModelNotFoundException(KClass.class, ns, identifier);
            }
            moduleJ.mergeIn(Ut.ioJObject(fileMod));
        } else if (valueJ instanceof JsonObject) {
            moduleJ.mergeIn((JsonObject) valueJ);
        }
        final String idConfig = classJ.getString(KName.IDENTIFIER);
        if (!identifier.equals(idConfig)) {
            throw new _409IdentifierConflictException(KClass.class, identifier, idConfig);
        }
        classJ.put(KName.MODULE, moduleJ);
        return new KClass(appName, classJ);
    }
}
