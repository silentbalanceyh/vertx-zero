package io.modello.dynamic.modular.apply;

import io.vertx.core.json.JsonObject;
import io.vertx.mod.atom.cv.AoCache;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.KWeb;

import java.util.UUID;

/**
 * 包内工具，设置读取的Json数据的默认值
 */
public interface AoDefault {
    static AoDefault schema() {
        return AoCache.CC_DEFAULT.pick(SchemaDefault::new, SchemaDefault.class.getName());
        // Fn.po?l(Pool.DEFAULT_POOL, SchemaDefault.class.getName(), SchemaDefault::new);
    }

    static AoDefault entity() {
        return AoCache.CC_DEFAULT.pick(EntityDefault::new, EntityDefault.class.getName());
        // return Fn.po?l(Pool.DEFAULT_POOL, EntityDefault.class.getName(), EntityDefault::new);
    }

    static AoDefault key() {
        return AoCache.CC_DEFAULT.pick(KeyDefault::new, KeyDefault.class.getName());
        // return Fn.po?l(Pool.DEFAULT_POOL, KeyDefault.class.getName(), KeyDefault::new);
    }

    static AoDefault field() {
        return AoCache.CC_DEFAULT.pick(FieldDefault::new, FieldDefault.class.getName());
        // return Fn.po?l(Pool.DEFAULT_POOL, FieldDefault.class.getName(), FieldDefault::new);
    }

    static AoDefault model() {
        return AoCache.CC_DEFAULT.pick(ModelDefault::new, ModelDefault.class.getName());
        // return Fn.po?l(Pool.DEFAULT_POOL, ModelDefault.class.getName(), ModelDefault::new);
    }

    static AoDefault attribute() {
        return AoCache.CC_DEFAULT.pick(AttributeDefault::new, AttributeDefault.class.getName());
        // return Fn.po?l(Pool.DEFAULT_POOL, AttributeDefault.class.getName(), AttributeDefault::new);
    }

    static AoDefault join() {
        return AoCache.CC_DEFAULT.pick(JoinDefault::new, JoinDefault.class.getName());
        // return Fn.po?l(Pool.DEFAULT_POOL, JoinDefault.class.getName(), JoinDefault::new);
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
        apply(entity, KName.KEY, UUID.randomUUID().toString());
        apply(entity, KName.ACTIVE, Boolean.TRUE);
        apply(entity, KName.LANGUAGE, KWeb.ARGS.V_LANGUAGE);  // 默认使用cn
        apply(entity, KName.METADATA, new JsonObject().encode());
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
