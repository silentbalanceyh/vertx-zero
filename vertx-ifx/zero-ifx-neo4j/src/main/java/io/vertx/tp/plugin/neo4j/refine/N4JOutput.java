package io.vertx.tp.plugin.neo4j.refine;

import io.vertx.core.json.JsonObject;
import io.horizon.uca.log.Annal;
import io.vertx.up.util.Ut;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.exceptions.NoSuchRecordException;
import org.neo4j.driver.internal.InternalNode;
import org.neo4j.driver.internal.InternalRelationship;

import java.util.Map;

class N4JOutput {
    private static final Annal LOGGER = Annal.get(N4JOutput.class);

    static JsonObject toJson(final Result result) {
        try {
            if (result.hasNext()) {
                return toJson(result.single());
            } else return null;
        } catch (final NoSuchRecordException ex) {
            LOGGER.warn("[ NEO4J ] Edge Exception: {0}", ex.getMessage());
            return null;
        }
    }

    static JsonObject toJson(final Result result, final String alias) {
        try {
            if (result.hasNext()) {
                final Record record = result.single();
                final JsonObject data = toJson(record);
                final JsonObject normalized = new JsonObject();
                Ut.itJObject(data, (value, field) -> {
                    if (field.contains(alias)) {
                        normalized.put(field.replace(alias + ".", ""), value);
                    } else {
                        normalized.put(field, value);
                    }
                });
                return normalized;
            } else return null;
        } catch (final NoSuchRecordException ex) {
            LOGGER.warn("[ NEO4J ] Node Exception: {0}", ex.getMessage());
            return null;
        }
    }

    private static JsonObject toJson(final Record record) {
        return toJson(record.asMap());
    }

    static JsonObject toJson(final Map<String, Object> input) {
        final JsonObject data = new JsonObject();
        input.forEach((key, value) -> {
            if (value instanceof InternalNode) {
                /*
                 * InternalNode
                 */
                final InternalNode node = ((InternalNode) value);
                node.asMap().forEach(data::put);
            } else if (value instanceof InternalRelationship) {
                /*
                 * InternalRelationship
                 */
                final InternalRelationship edge = (InternalRelationship) value;
                edge.asMap().forEach(data::put);
            } else {

                data.put(key, value);
            }
        });
        return data;
    }
}
