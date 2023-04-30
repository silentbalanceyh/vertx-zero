package io.vertx.up.fn;

import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

/**
 * 原 Ut.ifX 方法，由于通常做的是函数连接，重构之后转移到 Fn.ifX 中
 * 这一系列的函数主要用于 compose 的流线型API中，可帮助我们完成更暴力的函数式异步开发
 * 摒弃掉 Future 模式后做防御式编排。
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@Deprecated
class OldIf {

    static <T> JsonObject ifField(final JsonObject input, final String field, final T value) {
        final JsonObject inputJ = Ut.valueJObject(input);
        if (Ut.isNil(field)) {
            if (value instanceof final JsonObject data) {
                inputJ.mergeIn(data, true);
            }
        } else {
            inputJ.put(field, value);
        }
        return inputJ;
    }
}
