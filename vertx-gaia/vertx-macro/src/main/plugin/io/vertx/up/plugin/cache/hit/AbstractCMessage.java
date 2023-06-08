package io.vertx.up.plugin.cache.hit;

import io.horizon.eon.em.typed.ChangeFlag;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractCMessage implements CMessage {
    /*
     * Entity type of current message
     */
    protected transient final Class<?> type;
    /*
     * Primary Set
     */
    protected transient final TreeSet<String> primarySet = new TreeSet<>();
    /*
     * Data Reference if it's ok
     */
    protected transient Object data;

    protected AbstractCMessage(final Class<?> type) {
        this.type = type;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Class<T> dataType() {
        return Objects.nonNull(this.type) ? (Class<T>) this.type : null;
    }

    protected String typeName() {
        return Objects.nonNull(this.type) ? this.type.getName() : null;
    }

    @Override
    public CMessage bind(final TreeSet<String> primarySet) {
        this.primarySet.clear();
        this.primarySet.addAll(primarySet);
        return this;
    }

    @Override
    public <T> CMessage data(final T data) {
        this.data = data;
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T data() {
        return (T) this.data;
    }

    @Override
    public Buffer dataDelivery(final ChangeFlag flag) {
        final JsonObject delivery = new JsonObject();
        /*
         * Check entity to see whether they are collection
         * New structure for different data set
         */
        delivery.put("flag", flag);
        delivery.put("type", this.typeName());
        delivery.put("key", Ut.toJArray(this.primarySet));

        /*
         * Refer
         */
        if (this.isRef()) {
            delivery.put("refer", Boolean.TRUE);
        } else {
            delivery.put("refer", Boolean.FALSE);
        }

        /*
         * Message
         */
        if (Objects.nonNull(this.data)) {
            if (this.isList()) {
                delivery.put("data", (JsonArray) Ut.serializeJson(this.data));
                delivery.put("collection", Boolean.TRUE);
            } else {
                delivery.put("data", (JsonObject) Ut.serializeJson(this.data));
                delivery.put("collection", Boolean.FALSE);
            }
        }
        /*
         * Overite Part
         */
        final JsonObject overite = this.dataOverwrite();
        if (Ut.isNotNil(overite)) {
            delivery.mergeIn(overite, true);
        }
        return delivery.toBuffer();
    }

    public JsonObject dataOverwrite() {
        return new JsonObject();
    }

    protected TreeMap<String, String> dataMap(final String id) {
        final TreeMap<String, String> treeMap = new TreeMap<>();
        /*
         * Primary Key
         */
        final StringBuilder key = new StringBuilder();
        this.primarySet.forEach(key::append);
        treeMap.put(key.toString(), id);
        return treeMap;
    }
}
