package io.horizon.atom.common;

import io.horizon.eon.em.EmMeta;
import io.horizon.util.HUt;
import io.vertx.core.json.JsonObject;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Function;

/*
 * Normalized for field `metadata`
 * 1) __type__
 * 2) __content__
 */
public class Metadata implements Serializable {

    private static final String KEY_TYPE = "__type__";
    private static final String KEY_CONTENT = "__content__";

    private final JsonObject content = new JsonObject();

    public Metadata(final JsonObject input) {
        /*
         * Whether input contains `__type__`
         */
        if (input.containsKey(KEY_TYPE)) {
            /*
             * Source parsed here.
             */
            final EmMeta.Source source =
                HUt.toEnum(input.getString(KEY_TYPE), EmMeta.Source.class);
            final JsonObject content = input.getJsonObject(KEY_CONTENT);
            /*
             * Parser applying
             */
            final Function<JsonObject, JsonObject> parser =
                this.getParser(source);
            if (Objects.nonNull(parser)) {
                final JsonObject normalized = parser.apply(content);
                if (Objects.nonNull(normalized)) {
                    this.content.mergeIn(normalized.copy(), true);
                }
            }
        } else {
            /*
             * The pure metadata parsing, stored input to content
             * directly here.
             */
            this.content.mergeIn(input.copy(), true);
        }
    }

    private Function<JsonObject, JsonObject> getParser(final EmMeta.Source source) {
        if (EmMeta.Source.FILE == source) {
            return this::fromFile;
        } else {
            return null;
        }
    }

    private JsonObject fromFile(final JsonObject content) {
        final String path = content.getString("path");
        if (HUt.isNil(path)) {
            return new JsonObject();
        } else {
            try {
                return HUt.ioJObject(path);
            } catch (final Throwable ex) {
                return new JsonObject();
            }
        }
    }

    public JsonObject toJson() {
        return this.content;
    }
}
