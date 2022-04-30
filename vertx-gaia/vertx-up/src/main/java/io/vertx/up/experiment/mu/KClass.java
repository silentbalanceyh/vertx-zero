package io.vertx.up.experiment.mu;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Strings;
import io.vertx.up.exception.web._404ModelNotFoundException;
import io.vertx.up.exception.web._409IdentifierConflictException;
import io.vertx.up.experiment.mixture.HOne;
import io.vertx.up.experiment.specification.KJoin;
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

/**
 * M/U means modal unit, its format is following, please refer the related definition.yml file and definition.json
 * file for more details of KClass structure.
 *
 * KClass will be shared data structure ( JsonObject Part ) with KModule in up framework
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class KClass implements Serializable {
    private static final Cc<String, KHybrid> CC_HYBRID = Cc.open();
    private static final Annal LOGGER = Annal.get(KClass.class);
    private final String identifier;
    private final String namespace;
    /*
     * Put `linkage` configuration into linkage Set<String>
     */
    private final Set<String> linkage = new HashSet<>();
    private final JsonObject attribute = new JsonObject();

    private final KHybrid hybrid;
    private final KModule module;

    private KClass(final String namespace, final JsonObject configuration) {
        this.namespace = namespace;

        this.identifier = configuration.getString(KName.IDENTIFIER);

        final JsonObject moduleJ = Ut.valueJObject(configuration, KName.MODULE);
        this.module = Ut.deserialize(moduleJ, KModule.class);

        final JsonArray linkage = Ut.valueJArray(configuration, KName.LINKAGE);
        /*
         * Pre-Condition
         * this.identifier
         * this.module
         */
        this.initLinkage(Ut.toSet(linkage));

        final String unique = namespace + Strings.DASH + this.identifier;
        // Analyzer for attribute ( Type analyzing, Extract type from Dao )
        final JsonObject hybridInput = Ut.valueJObject(configuration, "hybrid");
        final JsonObject hybridJ = hybridInput.copy();
        {
            // Attribute preparing
            final JsonObject attributeRef = Ut.valueJObject(hybridJ, KName.ATTRIBUTE);
            // Set attribute and merged into current
            this.attribute.mergeIn(attributeRef, true);

            final JsonObject replacedAttr = this.initAttribute(attributeRef);
            hybridJ.put(KName.ATTRIBUTE, replacedAttr);
        }
        this.hybrid = CC_HYBRID.pick(() -> KHybrid.create(hybridJ), unique);
    }

    public static KClass create(final String namespace, final JsonObject classJ) {
        return new KClass(namespace, classJ);
    }

    public static KClass create(final String namespace, final String identifier) {
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
        return new KClass(namespace, classJ);
    }

    public String identifier() {
        return this.identifier;
    }

    public String namespace() {
        return this.namespace;
    }

    private JsonObject initAttribute(final JsonObject attributeRef) {
        // 1. Get combine types
        final ConcurrentMap<String, Class<?>> typeMap = this.initType();
        // 2. Attribute combine
        final JsonObject replacedAttr = new JsonObject();
        final JsonObject combineAttr = this.initAttribute();
        combineAttr.mergeIn(attributeRef, true);
        // 3. Process combined attr
        combineAttr.fieldNames().forEach(field -> {
            final Object value = combineAttr.getValue(field);
            final Class<?> type = typeMap.getOrDefault(field, String.class);
            if (value instanceof String) {
                // name = alias
                final JsonObject replaced = new JsonObject();
                replaced.put(KName.ALIAS, value);
                replaced.put(KName.TYPE, type.getName());
                replacedAttr.put(field, replaced);
            } else {
                // name = {}
                final JsonObject original = (JsonObject) value;
                final JsonObject replaced = original.copy();
                replaced.put(KName.ALIAS, original.getValue(KName.ALIAS));
                final String originalType = original.getString(KName.TYPE);
                if (!type.getName().equals(originalType)) {
                    LOGGER.warn("[ KClass ] Type conflict, old = {0}, new = {1}",
                        originalType, type.getName());
                }
                replaced.put(KName.TYPE, type.getName());
                replacedAttr.put(field, replaced);
            }
        });
        return replacedAttr;
    }

    private JsonObject initAttribute() {
        final JsonObject combine = new JsonObject();
        // Get Joint by nested
        this.linkage.forEach(identifier -> {
            final KClass kClass = KClass.create(this.namespace, identifier)
                .initLinkage(this.linkage);
            final JsonObject attribute = kClass.attribute;
            {
                final KJoin join = this.module.getConnect();
            }
            if (Ut.notNil(attribute)) {
                combine.mergeIn(attribute.copy(), true);
            }
        });
        return combine;
    }

    private ConcurrentMap<String, Class<?>> initType() {
        final ConcurrentMap<String, Class<?>> typeMap = new ConcurrentHashMap<>();
        // Get Joint by nested
        final HOne<ConcurrentMap<String, Class<?>>> one = HOne.type();

        this.linkage.forEach(identifier -> {
            /*
             * Create sub-class and set the input `linkage` into sub-class to combine
             * For example:
             * 1. A, linkage -> w.ticket, w.todo
             * 2. B, linkage -> w.ticket
             * 3. C, linkage
             *
             * Connect and set sub-class
             */
            final KClass kClass = KClass.create(this.namespace, identifier)
                .initLinkage(this.linkage);
            typeMap.putAll(one.combine(this.module, kClass.module));
            /*
             * Reduce and continue
             */
            typeMap.putAll(kClass.initType());
        });
        return typeMap;
    }

    /*
     * Bind linkage when you want to pass linkage from 1st to 2nd
     * Such as
     * 1) A,
     * 2) A Join B,
     * 3) B Join C
     * Here are definition for each:
     * 1) A, linkage = B,C
     * 2) B, linkage = C
     * 3) C, ?
     * Here the C must appear once only
     */
    private KClass initLinkage(final Set<String> linkage) {
        final Set<String> linkageSet = new HashSet<>(linkage);
        // 1. Add all configured linkage here
        linkageSet.addAll(this.linkage);
        // 2. Remove current identifier;
        linkageSet.remove(this.identifier);

        this.linkage.clear();
        if (Objects.nonNull(this.module)) {
            linkageSet.forEach(identifier -> {
                final KPoint point = this.module.getConnect(identifier);
                if (Objects.nonNull(point)) {
                    this.linkage.add(identifier);
                }
            });
        }
        return this;
    }

    public KHybrid hybrid() {
        return this.hybrid;
    }
}
