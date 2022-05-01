package io.vertx.up.experiment.mu;

import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Strings;
import io.vertx.up.exception.web._404ModelNotFoundException;
import io.vertx.up.exception.web._409IdentifierConflictException;
import io.vertx.up.experiment.specification.KModule;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.cache.Cc;
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
    /*
     * Cache for hybrid / module
     * 1) The KClass could be created each time ( new object )
     * 2) Following two objects could be created by unique key
     * -- KModule Key:  unique = namespace + identifier
     * -- KHybrid Key:  unique = namespace + identifier
     * 3) The linkage belong to current KClass Object
     */
    private static final Cc<String, KModule> CC_MODULE = Cc.open();


    /*
     * Basic information of current object
     */
    private final String identifier;
    private final String namespace;

    private final KHybrid hybrid;
    private final KModule module;

    // ============================== Create Start =======================
    private KClass(final String namespace, final JsonObject configuration,
                   final boolean recursion) {
        /* Build unique key based on `namespace + identifier` */
        this.namespace = namespace;
        this.identifier = configuration.getString(KName.IDENTIFIER);
        final String unique = namespace + Strings.DASH + this.identifier;

        /* Module Building based on Cache */
        final KModule module = CC_MODULE.pick(() -> {
            final JsonObject moduleJ = Ut.valueJObject(configuration, KName.MODULE);
            return Ut.deserialize(moduleJ, KModule.class);
        }, unique);
        this.module = module;

        // Analyzer for attribute ( Type analyzing, Extract type from Dao )
        final JsonObject hybridInput = Ut.valueJObject(configuration, "hybrid");
        final JsonObject hybridJ;
        if (recursion) {
            // Call internal Method
            hybridJ = KClassInternal.loadHybrid(hybridInput, module);
        } else {
            // Call direct Method, recursion = false only
            hybridJ = hybridInput.copy();
        }
        this.hybrid = KHybrid.create(hybridJ);
    }
    // ============================== Create End =========================


    public static KClass create(final String namespace, final String identifier,
                                final boolean recursion) {
        final JsonObject classJ = KClassInternal.loadData(namespace, identifier);
        return create(namespace, classJ, recursion);
    }

    public static KClass create(final String namespace, final JsonObject classJ,
                                final boolean recursion) {
        return new KClass(namespace, classJ, recursion);
    }

    public KHybrid hybrid() {
        return this.hybrid;
    }

    public String identifier() {
        return this.identifier;
    }

    public String namespace() {
        return this.namespace;
    }

    // =============================== Private Internal Class

    /**
     * @author <a href="http://www.origin-x.cn">Lang</a>
     */
    private static class KClassInternal {
        private static final Annal LOGGER = Annal.get(KClassInternal.class);

        static JsonObject loadHybrid(final JsonObject hybridJ, final KModule module) {

            return null;
        }

        static JsonObject loadData(final String namespace, final String identifier) {
            Objects.requireNonNull(identifier);
            final String fileCls = "hybrid/" + identifier + ".json";
            if (!Ut.ioExist(fileCls)) {
                LOGGER.warn("[ KClass ] Class Not Found = {0}", fileCls);
                throw new _404ModelNotFoundException(KClass.class, namespace, identifier);
            }
            // JsonObject read
            final JsonObject classJ = Ut.ioJObject(fileCls);
            final Object valueJ = classJ.getValue(KName.MODULE);
            final JsonObject moduleJ = new JsonObject();
            if (valueJ instanceof String) {
                final String fileMod = (String) valueJ;
                if (!Ut.ioExist(fileMod)) {
                    LOGGER.warn("[ KClass ] Module Not Found = {0}", fileMod);
                    throw new _404ModelNotFoundException(KClass.class, namespace, identifier);
                }
                moduleJ.mergeIn(Ut.ioJObject(fileMod));
            } else if (valueJ instanceof JsonObject) {
                moduleJ.mergeIn((JsonObject) valueJ);
            }
            final String idConfig = classJ.getString(KName.IDENTIFIER);
            if (!identifier.equals(idConfig)) {
                LOGGER.warn("[ KClass ] Identifier Conflict: {0} , {1}", identifier, idConfig);
                throw new _409IdentifierConflictException(KClass.class, identifier, idConfig);
            }
            classJ.put(KName.MODULE, moduleJ);
            LOGGER.info("[ KClass ] Json Input = {0}", classJ.encode());
            return classJ;
        }
    }
}
