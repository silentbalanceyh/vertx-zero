package io.vertx.tp.ke.atom;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.cv.em.KeSource;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Function;

/*
 * Normalized for field `metadata`
 * 1) __type__
 * 2) __content__
 */
public class KeMetadata implements Serializable {
    private static final Annal LOGGER = Annal.get(KeMetadata.class);

    private static final String KEY_TYPE = "__type__";
    private static final String KEY_CONTENT = "__content__";

    private final transient JsonObject content = new JsonObject();

    public KeMetadata(final JsonObject input) {
        /*
         * Whether input contains `__type__`
         */
        if (input.containsKey(KEY_TYPE)) {
            /*
             * Source parsed here.
             */
            final KeSource source =
                    Ut.toEnum(KeSource.class, input.getString(KEY_TYPE));
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

    private Function<JsonObject, JsonObject> getParser(final KeSource source) {
        if (KeSource.FILE == source) {
            return this::fromFile;
        } else {
            return null;
        }
    }

    private JsonObject fromFile(final JsonObject content) {
        final String path = content.getString("path");
        if (Ut.isNil(path)) {
            return new JsonObject();
        } else {
            try {
                return Ut.ioJObject(path);
            } catch (final Throwable ex) {
                LOGGER.jvm(ex);
                return new JsonObject();
            }
        }
    }

    public JsonObject toJson() {
        return this.content;
    }
}
