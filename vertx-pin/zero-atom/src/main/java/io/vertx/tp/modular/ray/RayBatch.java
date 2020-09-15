package io.vertx.tp.modular.ray;

import io.vertx.up.commune.Record;
import io.vertx.up.commune.element.AmbJson;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class RayBatch extends AbstractRay<Record[]> {
    @Override
    public Record[] doAttach(final Record[] input) {
        /*
         * 返回 DataQuote
         */
        final ConcurrentMap<String, AmbJson> data = new ConcurrentHashMap<>();
        this.refer.values().forEach(source -> data.putAll(source.fetchBatch(input)));
        /*
         * 结果处理
         */
        return RayCombine.combine(input, data, this.referRules);
    }
}
