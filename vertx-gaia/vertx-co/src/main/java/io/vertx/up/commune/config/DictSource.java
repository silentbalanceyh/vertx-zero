package io.vertx.up.commune.config;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.em.SourceType;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/*
 * Assist / Category / Tabular
 * 1) Category ->
 * -- Read categories by type and to `key = JsonArray`
 * 2) Tabular ->
 * -- Read tabular by type and to `key = JsonArray`
 * 3) Assist ->
 * A little complex
 */
public class DictSource implements Serializable {
    private static final Annal LOGGER = Annal.get(DictSource.class);
    /*
     * SourceType of current source definition
     */
    private final transient SourceType source;
    private final transient Set<String> types = new HashSet<>();
    /*
     * JsonObject
     */
    private final transient JsonObject componentConfig = new JsonObject();
    /*
     * Another source of ASSIST here
     */
    private transient String key;

    private transient Class<?> component;

    DictSource(final JsonObject definition) {
        /*
         * Source normalize for `source type`
         */
        final String source = definition.getString("source");
        this.source = Ut.toEnum(() -> source, SourceType.class, SourceType.NONE);
        if (SourceType.CATEGORY == this.source || SourceType.TABULAR == this.source) {
            /*
             * Different definition for
             * 1) CATEGORY / TABULAR
             */
            final JsonArray typeJson = definition.getJsonArray("types");
            if (Objects.nonNull(typeJson)) {
                typeJson.stream().filter(Objects::nonNull)
                        .map(item -> (String) item)
                        .forEach(this.types::add);
            }
        } else if (SourceType.ASSIST == this.source) {
            /*
             * Different definition for
             * ASSIST
             */
            this.key = definition.getString("key");
            final String className = definition.getString("component");
            if (Ut.notNil(className)) {
                this.component = Ut.clazz(className);
                if (Objects.isNull(this.component)) {
                    LOGGER.warn("The component `{0}` could not be initialized", className);
                }
            }
            final JsonObject componentConfig = definition.getJsonObject("componentConfig");
            if (Ut.notNil(componentConfig)) {
                this.componentConfig.mergeIn(componentConfig);
            }
        }
    }

    public SourceType getSourceType() {
        return this.source;
    }

    public Set<String> getTypes() {
        return this.types;
    }

    public String getKey() {
        return this.key;
    }

    public <T> T getPlugin() {
        if (Objects.isNull(this.component)) {
            return null;
        } else {
            return Ut.singleton(this.component);
        }
    }

    public JsonObject getPluginConfig() {
        return this.componentConfig;
    }
}
