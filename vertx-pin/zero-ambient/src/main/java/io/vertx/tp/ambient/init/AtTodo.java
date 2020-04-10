package io.vertx.tp.ambient.init;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.ambient.cv.AtFolder;
import io.vertx.tp.ambient.refine.At;
import io.vertx.up.eon.FileSuffix;
import io.vertx.up.eon.Strings;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/*
 * Configuration for XTodo by file
 * 1) Each file stored one type of todoDef
 * 2) The file data should be stored into Map
 */
class AtTodo {
    /*
     * Logger for IxDao
     */
    private static final Annal LOGGER = Annal.get(AtTodo.class);
    private static final ConcurrentMap<String, JsonObject> TODO_DEF =
            new ConcurrentHashMap<>();

    static void init() {
        if (TODO_DEF.isEmpty()) {
            final List<String> files = Ut.ioFiles(AtFolder.TODO_FOLDER, FileSuffix.JSON);
            At.infoInit(LOGGER, "At Todo Files: {0}", files.size());
            files.forEach(file -> {
                final String path = AtFolder.TODO_FOLDER + file;
                final JsonObject todoDef = Ut.ioJObject(path);

                final String key = file.replace(Strings.DOT + FileSuffix.JSON, Strings.EMPTY);

                TODO_DEF.put(key, todoDef);
            });
        }
    }

    static JsonObject getTodo(final String type) {
        final JsonObject todoDef = TODO_DEF.get(type);
        return Objects.isNull(todoDef) ? new JsonObject() : todoDef.copy();
    }
}
