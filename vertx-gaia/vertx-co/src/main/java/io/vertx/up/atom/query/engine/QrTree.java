package io.vertx.up.atom.query.engine;

import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.Strings;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.Set;

public class QrTree implements QrDo {

    private static final Annal LOGGER = Annal.get(QrTree.class);
    private final transient Set<QrTree> trees = new HashSet<>();
    private final transient Set<String> linearKeys = new HashSet<>();
    private final transient JsonObject raw = new JsonObject();
    private transient Qr.Connector op;
    private transient QrLinear linear;   // The same level linear;

    private QrTree(final JsonObject data) {
        this.raw.mergeIn(data);
        // calc op
        this.initConnector(data);
        // extract linear
        this.initQrTree(data);
        // keys
        this.initQrLinear(data);
    }

    public static QrTree create(final JsonObject data) {
        return new QrTree(data);
    }

    private void initQrLinear(final JsonObject data) {
        final JsonObject linear = new JsonObject();
        this.linearKeys.forEach(key -> linear.put(key, data.getValue(key)));
        this.linear = QrLinear.create(linear);
    }

    private void initQrTree(final JsonObject data) {
        final JsonObject linear = data.copy();
        linear.remove(Strings.EMPTY);
        final Set<String> treeKeys = new HashSet<>();
        for (final String field : linear.fieldNames()) {
            if (!Ut.isJObject(linear.getValue(field))) {
                this.linearKeys.add(field);
            } else {
                final JsonObject item = linear.getJsonObject(field);
                this.trees.add(QrTree.create(item));
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

    /**
     * Check current QTree to see whether it's valid.
     *
     * @return {@link java.lang.Boolean};
     */
    @Override
    public boolean valid() {
        return null != this.linear || !this.trees.isEmpty();
    }

    /**
     * Serialized current instance to Json
     *
     * @return {@link JsonObject}
     */
    @Override
    public JsonObject toJson() {
        return this.raw;
    }

    /**
     * @param fieldExpr {@link java.lang.String}
     * @param value     {@link java.lang.Object}
     *
     * @return {@link QrLinear}
     */
    @Override
    public QrTree add(final String fieldExpr, final Object value) {
        return this;
    }
}
