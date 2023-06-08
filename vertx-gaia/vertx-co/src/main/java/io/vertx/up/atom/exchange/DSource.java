package io.vertx.up.atom.exchange;

import io.horizon.eon.em.EmDict;
import io.horizon.specification.typed.TCopy;
import io.horizon.uca.log.Annal;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
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
public class DSource implements Serializable, TCopy<DSource> {
    private static final Annal LOGGER = Annal.get(DSource.class);
    private final Set<String> types = new HashSet<>();
    /*
     * JsonObject
     */
    private final JsonObject componentConfig = new JsonObject();
    /*
     * SourceType of current source definition
     */
    private EmDict.Type source;
    /*
     * Another source of ASSIST here
     */
    private String key;

    private Class<?> component;

    public DSource(final JsonObject definition) {
        /*
         * Source normalize for `source type`
         */
        final String source = definition.getString("source");
        this.source = Ut.toEnum(() -> source, EmDict.Type.class, EmDict.Type.NONE);
        if (EmDict.Type.CATEGORY == this.source || EmDict.Type.TABULAR == this.source) {
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
        } else if (EmDict.Type.ASSIST == this.source) {
            /*
             * Different definition for
             * ASSIST
             */
            this.key = definition.getString("key");
            final String className = definition.getString("component");
            if (Ut.isNotNil(className)) {
                this.component = Ut.clazz(className);
                if (Objects.isNull(this.component)) {
                    LOGGER.warn("The component `{0}` could not be initialized", className);
                }
            }
            final JsonObject componentConfig = definition.getJsonObject("componentConfig");
            if (Ut.isNotNil(componentConfig)) {
                this.componentConfig.mergeIn(componentConfig);
            }
        }
    }

    private DSource() {
    }

    public EmDict.Type getSourceType() {
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

    public Class<?> getComponent() {
        return this.component;
    }

    public JsonObject getPluginConfig() {
        return Ut.valueJObject(this.componentConfig);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <CHILD extends DSource> CHILD copy() {
        final DSource source = new DSource();
        source.component = this.component;
        source.componentConfig.clear();
        source.componentConfig.mergeIn(this.componentConfig.copy());
        source.key = this.key;
        source.source = this.source;
        source.types.addAll(this.types);
        return (CHILD) source;
    }
}
