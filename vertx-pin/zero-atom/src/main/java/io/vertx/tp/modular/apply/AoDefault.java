package io.vertx.tp.modular.apply;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.cv.KeField;
import io.vertx.up.fn.Fn;

import java.util.UUID;

/**
 * 包内工具，设置读取的Json数据的默认值
 */
public interface AoDefault {
    static AoDefault schema() {
        return Fn.pool(Pool.DEFAULT_POOL, SchemaDefault.class.getName(),
                SchemaDefault::new);
    }

    static AoDefault entity() {
        return Fn.pool(Pool.DEFAULT_POOL, EntityDefault.class.getName(),
                EntityDefault::new);
    }

    static AoDefault key() {
        return Fn.pool(Pool.DEFAULT_POOL, KeyDefault.class.getName(),
                KeyDefault::new);
    }

    static AoDefault field() {
        return Fn.pool(Pool.DEFAULT_POOL, FieldDefault.class.getName(),
                FieldDefault::new);
    }

    static AoDefault model() {
        return Fn.pool(Pool.DEFAULT_POOL, ModelDefault.class.getName(),
                ModelDefault::new);
    }

    static AoDefault attribute() {
        return Fn.pool(Pool.DEFAULT_POOL, AttributeDefault.class.getName(),
                AttributeDefault::new);
    }

    static AoDefault join() {
        return Fn.pool(Pool.DEFAULT_POOL, JoinDefault.class.getName(),
                JoinDefault::new);
    }

    static <T> void apply(final JsonObject target,
                          final String key,
                          final T value) {
        if (null != target && !target.containsKey(key)) {
            target.put(key, value);
        }
    }

    static void apply(final JsonObject entity) {
        // 这四个字段基本一致
        apply(entity, KeField.KEY, UUID.randomUUID().toString());
        apply(entity, KeField.ACTIVE, Boolean.TRUE);
        apply(entity, KeField.LANGUAGE, "cn");  // 默认使用cn
        apply(entity, KeField.METADATA, new JsonObject().encode());
    }

    /**
     * 为Json Object设置默认值
     */
    void applyJson(JsonObject source);

    /**
     * 特殊函数用于挂载
     */
    default <T> AoDefault mount(final T mounted) {
        // 默认无任何实现
        return this;
    }
}
