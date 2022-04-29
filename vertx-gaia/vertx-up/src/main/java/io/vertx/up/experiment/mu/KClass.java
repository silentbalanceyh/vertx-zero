package io.vertx.up.experiment.mu;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Strings;
import io.vertx.up.exception.web._404ModelNotFoundException;
import io.vertx.up.exception.web._409IdentifierConflictException;
import io.vertx.up.experiment.specification.KJoin;
import io.vertx.up.experiment.specification.KModule;
import io.vertx.up.experiment.specification.KPoint;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.cache.Cc;
import io.vertx.up.uca.jooq.JqAnalyzer;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
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

    private final Set<String> linkage = new HashSet<>();
    private final KHybrid hybrid;
    private final KModule module;

    private KClass(final String namespace, final JsonObject configuration) {
        this.namespace = namespace;

        this.identifier = configuration.getString(KName.IDENTIFIER);

        final JsonObject moduleJ = Ut.valueJObject(configuration, KName.MODULE);
        this.module = Ut.deserialize(moduleJ, KModule.class);

        final JsonArray linkage = Ut.valueJArray(configuration, KName.LINKAGE);
        this.linkage.addAll(Ut.toSet(linkage));
        // Remove Current
        this.linkage.remove(this.identifier);

        final String unique = namespace + Strings.DASH + this.identifier;
        // Analyzer for attribute ( Type analyzing, Extract type from Dao )
        final JsonObject hybridJ = this.initializeHybrid(configuration);
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

    private JsonObject initializeHybrid(final JsonObject configuration) {
        JsonObject hybridJ = Ut.valueJObject(configuration, "hybrid");
        hybridJ = hybridJ.copy();

        final ConcurrentMap<String, Class<?>> typeMap = this.initializeType();

        final JsonObject attributeRef = Ut.valueJObject(hybridJ, KName.ATTRIBUTE);
        final JsonObject replacedAttr = new JsonObject();
        attributeRef.fieldNames().forEach(field -> {
            final Object value = attributeRef.getValue(field);
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
        hybridJ.put(KName.ATTRIBUTE, replacedAttr);
        return hybridJ;
    }

    private ConcurrentMap<String, Class<?>> initializeType() {
        final Class<?> daoCls = this.module.getDaoCls();
        final UxJooq jq = Ux.Jooq.on(daoCls);
        final JqAnalyzer analyzer = jq.analyzer();
        final ConcurrentMap<String, Class<?>> typeMap = analyzer.types();
        final String identifierM = this.module.getIdentifier();
        if (!this.identifier.equals(identifierM)) {
            // Get Joint
            this.linkage.forEach(identifier -> {

            });
        }
        return typeMap;
    }

    private ConcurrentMap<String, Class<?>> initializeType(final String identifier) {
        // Remove all existing part
        final KJoin join = this.module.getConnect();
        if (Objects.isNull(join)) {
            return new ConcurrentHashMap<>();
        }
        final KPoint point = join.point(identifier);
        if (Objects.isNull(point)) {
            return new ConcurrentHashMap<>();
        }
        final KClass joined = KClass.create(this.namespace, identifier).linkage(this.linkage);
        return joined.initializeType();
    }

    private void initializeLinkage(final KModule module, final Set<String> linkage) {
        // First remove current identifier
        linkage.remove(this.identifier);
    }

    public KClass linkage(final Set<String> linkage) {
        this.linkage.addAll(linkage);
        // Remove Current
        this.linkage.remove(this.identifier);
        return this;
    }

    public KHybrid hybrid() {
        return this.hybrid;
    }
}
