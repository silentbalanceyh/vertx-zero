package io.vertx.up.commune;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class ActFile implements Serializable {

    private final transient JsonArray rawData;
    private final transient List<File> files = new ArrayList<>();

    ActFile(final JsonArray rawData) {
        /*
         * Stored raw data
         */
        if (Objects.isNull(rawData)) {
            this.rawData = new JsonArray();
        } else {
            this.rawData = rawData.copy();
            this.partData(rawData);
        }
    }

    private void partData(final JsonArray fileData) {
        fileData.stream()
            .filter(item -> item instanceof JsonObject)
            .map(item -> (JsonObject) item)
            .forEach(item -> {
                final File file = new File(item.getString("path"));
                if (file.exists()) {
                    this.files.add(file);
                }
            });
    }

    File[] getFiles() {
        return this.files.toArray(new File[]{});
    }

    JsonArray getRaw() {
        return this.rawData;
    }
}
