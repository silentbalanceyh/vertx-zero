package io.vertx.up.experiment.mu;

import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Strings;
import io.vertx.up.exception.web._404ModelNotFoundException;
import io.vertx.up.exception.web._409IdentifierConflictException;
import io.vertx.up.experiment.mixture.HOne;
import io.vertx.up.experiment.specification.KModule;
import io.vertx.up.experiment.specification.KPoint;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.cache.Cc;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;

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
     * 3) The chLinkage belong to current KClass Object
     */
    private static final Cc<String, KModule> CC_MODULE = Cc.open();

    /*
     * Basic information of current object
     */
    private final String identifier;
    private final String namespace;

    private final Set<String> chLinkage = new HashSet<>();
    private final JsonObject chAttribute = new JsonObject();

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

        {
            /* Linkage for current KClass */
            final Set<String> linkageSet = Ut.toSet(Ut.valueJArray(configuration, KName.LINKAGE));
            this.chLinkage.addAll(forLinkage(linkageSet, module));
            /* attribute for current KClass */
            final JsonObject attribute = Ut.valueJObject(configuration, KName.ATTRIBUTE);
            this.chAttribute.mergeIn(attribute, true);
        }

        // Analyzer for attribute ( Type analyzing, Extract type from Dao )
        final JsonObject hybridInput = Ut.valueJObject(configuration, "hybrid");
        final JsonObject hybridJ;
        if (recursion) {
            // Call internal Method
            hybridJ = KClassInternal.loadHybrid(hybridInput, this);
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

    /*
     * Bind chLinkage when you want to pass chLinkage from 1st to 2nd
     * Such as
     * 1) A,
     * 2) A Join B,
     * 3) B Join C
     * Here are definition for each:
     * 1) A, chLinkage = B,C
     * 2) B, chLinkage = C
     * 3) C, ?
     * Here the C must appear once only
     *
     * It means that this method is only for ( One -> One ) and it does not support
     * nested and recursion linkage because different linkage combine different module
     * and KClass here. It's very important.
     */
    private static Set<String> forLinkage(final Set<String> linkage, final KModule module) {
        Objects.requireNonNull(module);
        // 1. Add all configured chLinkage here
        final Set<String> linkageSet = new HashSet<>(linkage);
        // 2. Remove current identifier;
        linkageSet.remove(module.getIdentifier());

        linkageSet.forEach(identifier -> {
            final KPoint point = module.getConnect(identifier);
            // 3. Check the identifier in linkageSet could be connected.
            if (Objects.nonNull(point)) {
                linkageSet.add(identifier);
            }
        });
        return linkageSet;
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

        static JsonObject loadHybrid(final JsonObject hybridJ, final KClass clazz) {
            // 1. Extract type Map
            final ConcurrentMap<String, Class<?>> typeMap = loadType(clazz);
            // 2. Attribute Processing
            final JsonObject attribute = loadAttribute(clazz);

            return null;
        }

        private static JsonObject loadAttribute(final KClass clazz) {
            final JsonObject attribute = new JsonObject();
            // 1. Fetch reference of `HOne`
            final HOne<BiFunction<JsonObject, JsonObject, JsonObject>> one = HOne.Fn.hybrid();

            // 2. Determine the join method here
            final Set<String> linkageSet = clazz.chLinkage;
            final KModule module = clazz.module;
            final JsonObject attributeJ = clazz.chAttribute.copy();
            if (linkageSet.isEmpty()) {
                /* No linkage defined */
                final BiFunction<JsonObject, JsonObject, JsonObject> executor
                    = one.combine(module, (KModule) null);
                // attribute -> {}
                attribute.mergeIn(executor.apply(new JsonObject(), attributeJ));
            } else {

                linkageSet.forEach(identifier -> {
                    final KClass kClass = KClass.create(clazz.namespace, identifier, true);

                    final BiFunction<JsonObject, JsonObject, JsonObject> executor
                        = one.combine(module, kClass.module);
                    {
                        // Attribute Combine
                        // Child ( attribute ) -> {}
                        attribute.mergeIn(loadAttribute(kClass), true);
                    }
                    // Merged here
                    // attribute -> Child ( attribute ) -> {}
                    final JsonObject attributeCJ = kClass.chAttribute.copy();
                    attribute.mergeIn(executor.apply(attributeCJ, attributeJ));
                });
            }
            return attribute;
        }

        private static ConcurrentMap<String, Class<?>> loadType(final KClass clazz) {
            final ConcurrentMap<String, Class<?>> result = new ConcurrentHashMap<>();
            // 1. Fetch reference of `HOne`
            final HOne<ConcurrentMap<String, Class<?>>> one = HOne.type();

            // 2. Determine the join method here
            final Set<String> linkageSet = clazz.chLinkage;
            final KModule module = clazz.module;
            if (linkageSet.isEmpty()) {
                /*
                 * No linkage defined here,
                 * 1) Load clazz type map only
                 * 2) Second parameter is null and set connect to null
                 */
                result.putAll(one.combine(module, (KModule) null));
            } else {
                /*
                 * linkage defined ( Preprocessed by okLinkage )
                 * 1) module's identifier does not exist in `linkage`
                 * 2) invalid identifier doest not exist in `linkage`
                 *
                 * A -> B, C
                 * B -> D
                 * D -> F
                 *
                 * Here the result will be
                 * ( A,B,C,D,F )
                 *
                 * 「Node」
                 * - If there exist same `field`, field = clazz will be merged.
                 * - The priority is not important ( Same field must be the same Type ).
                 * - The merge sequence is not important here
                 */
                linkageSet.forEach(identifier -> {
                    final KClass kClass = KClass.create(clazz.namespace, identifier, true);
                    result.putAll(one.combine(module, kClass.module));
                    result.putAll(loadType(kClass));
                });
            }
            return result;
        }


        /*
         * Load data from `hybrid/<identifier>.json` here.
         * The structure could be formatted with:
         *
         * 1. KClass = KHybrid + KModule
         * 2. Here the KHybrid could be different with the same module, it means that
         *    KClass could be different based on same module
         *
         * For example
         *
         *      Module1, Module2, Module3, Module4
         *
         * 1) KClass1 = KHybrid1 + Module1
         * 2) KClass1 = KHybrid1 + ( Module1 + Module3 )
         *    Above situation 2 must be following condition:
         *    2.1) recursion = true for KClass1
         *    2.2) The Module1 defined KPoint to Module3
         *    2.3) The KClass1 chLinkage contains identifier of Module3
         * 3) KClass2 = KHybrid2 + ( Module2 + Module3 + Module4 )
         *
         * When the `recursion = true`, the nested module is automatically connected
         *
         * 1) Module1 = Module1 + KPoint ( Module2 ),`chLinkage` in KClass1
         * 2) Module2 = Module2 + KPoint ( Module3 ),`chLinkage` in KClass2
         * The final result of KClass1 is ( Module1 + Module2 + Module3 )
         */
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
