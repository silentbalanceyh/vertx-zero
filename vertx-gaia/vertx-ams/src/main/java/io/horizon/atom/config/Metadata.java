package io.horizon.atom.config;

import io.horizon.eon.em.modeler.MetaSource;
import io.horizon.util.HaS;
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
            final MetaSource source =
                HaS.toEnum(input.getString(KEY_TYPE), MetaSource.class);
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

    private Function<JsonObject, JsonObject> getParser(final MetaSource source) {
        if (MetaSource.FILE == source) {
            return this::fromFile;
        } else {
            return null;
        }
    }

    private JsonObject fromFile(final JsonObject content) {
        final String path = content.getString("path");
        if (HaS.isNil(path)) {
            return new JsonObject();
        } else {
            try {
                return HaS.ioJObject(path);
            } catch (final Throwable ex) {
                return new JsonObject();
            }
        }
    }

    public JsonObject toJson() {
        return this.content;
    }
}