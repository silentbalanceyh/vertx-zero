package io.vertx.tp.plugin.cache.hit;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.TreeSet;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
@SuppressWarnings("all")
public abstract class AbstractCMessage implements CMessage {
    protected transient final Class<?> type;
    protected transient final TreeSet<String> primarySet = new TreeSet<>();
    protected transient Object data;

    protected AbstractCMessage(final Class<?> type) {
        this.type = type;
    }

    protected <T> Class<T> type() {
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
    public <T> CMessage data(T data) {
        this.data = data;
        return this;
    }

    @Override
    public <T> T data() {
        return (T) this.data;
    }

    @Override
    public Buffer dataDelivery(ChangeFlag flag) {
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
            delivery.put("primary", Boolean.TRUE);
        } else {
            delivery.put("primary", Boolean.FALSE);
        }
        /*
         * Message
         */
        if (Objects.nonNull(this.data)) {
            if (this.isList()) {
                delivery.put("data", (JsonArray) Ut.serializeJson(this.data));
            } else {
                delivery.put("data", (JsonObject) Ut.serializeJson(this.data));
                delivery.put("collection", Boolean.TRUE);
            }
        }
        delivery.mergeIn(dataPart());
        return delivery.toBuffer();
    }

    public JsonObject dataPart() {
        return new JsonObject();
    }
}
