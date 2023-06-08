package io.mature.extension.uca.code;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.util.Queue;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Seq<T> {
    /* 绑定配置信息 */
    default Seq<T> bind(final Class<?> clazz, final String code) {
        return this;
    }

    /* 绑定配置信息 */
    Seq<T> bind(JsonObject options);

    /* 多序号生成 */
    Future<Queue<String>> generate(T input, Integer counter);
}
