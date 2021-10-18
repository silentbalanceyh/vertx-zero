package io.vertx.tp.optic.business;

import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ambient.uca.dict.Dpm;
import io.vertx.up.commune.exchange.DiConsumer;
import io.vertx.up.commune.exchange.DiSource;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.em.GlossaryType;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class ExAttributeComponent {
    /**
     * Here are json format of `definition`
     *
     * ```json
     * // <pre><code class="json">
     *     {
     *         "sourceConsumer": {
     *             "source": "Consume Source",
     *             "in": "In Field",
     *             "out": "Out Field"
     *         },
     *         "sourceDict": {
     *              "comments": "Source Dict Definition Here"
     *         },
     *         "source": "Related to `source` field of M_ATTRIBUTE",
     *         "sourceField": "Related to `sourceField` field of M_ATTRIBUTE",
     *         "sourceData": {
     *             "comments": "The final dictionary data store"
     *         }
     *     }
     * // </code></pre>
     * ```
     *
     * @param definition {@link JsonObject} The input definition
     *
     * @return {@link ConcurrentMap} The dict source map ( key = JsonArray )
     */
    public ConcurrentMap<String, JsonArray> source(final JsonObject definition) {
        final JsonObject sourceDictJson = definition.getJsonObject(KName.SOURCE_DICT);
        final ConcurrentMap<String, JsonArray> sourceData = new ConcurrentHashMap<>();
        if (Ut.notNil(sourceDictJson)) {
            /*
             * Parameters
             */
            final DiSource sourceDict = new DiSource(sourceDictJson);
            /*
             * Dpm
             */
            final GlossaryType type = sourceDict.getSourceType();
            final Dpm dpm = Dpm.get(type);
            if (Objects.nonNull(dpm)) {
                /*
                 * Build parameters for processing
                 */
                final MultiMap paramMap = MultiMap.caseInsensitiveMultiMap();
                final JsonObject sourceParams = definition.getJsonObject(KName.SOURCE_PARAMS);
                if (Ut.notNil(sourceParams)) {
                    sourceParams.stream()
                        .filter(Objects::nonNull)
                        .filter(entry -> Objects.nonNull(entry.getValue()))
                        .forEach(entry -> paramMap.add(entry.getKey(), entry.getValue().toString()));
                }
                sourceData.putAll(dpm.fetch(sourceDict, paramMap));
            }
        }
        return sourceData;
    }

    protected Object translateTo(final Object value, final JsonObject definition) {
        if (Objects.isNull(value)) {
            return null;
        }
        final JsonObject data = this.translateData(definition, false);
        final Object processed = data.getValue(value.toString(), value);
        return this.normalizeValue(processed, definition);
    }

    protected Object translateFrom(final Object value, final JsonObject definition) {
        if (Objects.isNull(value)) {
            return null;
        }
        final JsonObject data = this.translateData(definition, true);
        final Object processed = data.getValue(value.toString(), value);
        return this.normalizeValue(processed, definition);
    }

    private JsonObject translateData(final JsonObject definition, final boolean isFrom) {
        /* Consumer */
        final JsonObject consumer = definition.getJsonObject(KName.SOURCE_CONSUMER);
        final DiConsumer epsilon = new DiConsumer();
        epsilon.fromJson(consumer);

        /* Data */
        final JsonObject dictData = Ut.sureJObject(definition.getJsonObject(KName.SOURCE_DATA));
        final String key = epsilon.getSource();
        final JsonArray data = Ut.notNil(key) ? dictData.getJsonArray(key) : new JsonArray();
        final JsonObject result = new JsonObject();
        if (epsilon.isValid()) {
            Ut.itJArray(data).forEach(item -> {
                final String inValue = item.getString(epsilon.getIn());
                final String outValue = item.getString(epsilon.getOut());
                if (Ut.notNil(inValue) && Ut.notNil(outValue)) {
                    if (isFrom) {
                        /*
                         * in -> out
                         */
                        result.put(inValue, outValue);
                    } else {
                        /*
                         * out -> in
                         */
                        result.put(outValue, inValue);
                    }
                }
            });
        }
        return result;
    }

    private Object normalizeValue(final Object value, final JsonObject definition) {
        if (Objects.isNull(value)) {
            return null;
        } else {
            if (definition.containsKey(KName.SOURCE_NORM)) {
                final JsonObject normData = Ut.sureJObject(definition.getJsonObject(KName.SOURCE_NORM));
                return normData.getValue(value.toString(), value);
            } else return value;
        }
    }
}
