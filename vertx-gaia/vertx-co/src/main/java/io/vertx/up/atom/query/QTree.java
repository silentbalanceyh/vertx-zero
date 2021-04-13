package io.vertx.up.atom.query;

import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.Strings;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.Set;

public class QTree {

    private static final Annal LOGGER = Annal.get(QTree.class);
    private final transient Set<QTree> trees = new HashSet<>();
    private final transient Set<String> linearKeys = new HashSet<>();
    private final transient JsonObject raw = new JsonObject();
    private transient Qr.Connector op;
    private transient QLinear linear;   // The same level linear;

    private QTree(final JsonObject data) {
        this.raw.mergeIn(data);
        // calc op
        this.initConnector(data);
        // extract linear
        this.initLinearKey(data);
        // keys
        this.initLinear(data);
    }

    public static QTree create(final JsonObject data) {
        return new QTree(data);
    }

    private void initLinear(final JsonObject data) {
        final JsonObject linear = new JsonObject();
        this.linearKeys.forEach(key -> linear.put(key, data.getValue(key)));
        this.linear = QLinear.create(linear);
    }

    private void initLinearKey(final JsonObject data) {
        final JsonObject linear = data.copy();
        linear.remove(Strings.EMPTY);
        final Set<String> treeKeys = new HashSet<>();
        for (final String field : linear.fieldNames()) {
            if (!Ut.isJObject(linear.getValue(field))) {
                this.linearKeys.add(field);
            } else {
                final JsonObject item = linear.getJsonObject(field);
                this.trees.add(QTree.create(item));
                treeKeys.add(field);
            }
        }
        LOGGER.debug(Info.Q_ALL, this.linearKeys, treeKeys);
    }

    private void initConnector(final JsonObject data) {
        if (!data.containsKey(Strings.EMPTY)) {
            this.op = Qr.Connector.OR;
        } else {
            final boolean isAnd = Boolean.parseBoolean(data.getValue(Strings.EMPTY).toString());
            this.op = isAnd ? Qr.Connector.AND : Qr.Connector.OR;
        }
        LOGGER.debug(Info.Q_STR, this.op);
    }

    public boolean isValid() {
        return null != this.linear || !this.trees.isEmpty();
    }

    public JsonObject toJson() {
        return this.raw;
    }
}
