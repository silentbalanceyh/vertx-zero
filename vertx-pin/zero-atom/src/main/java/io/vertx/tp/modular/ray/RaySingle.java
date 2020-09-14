package io.vertx.tp.modular.ray;

import io.vertx.up.commune.Record;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class RaySingle extends AbstractRay<Record> {
    @Override
    public Record doAttach(final Record input) {
        /*
         * 返回 DataQuote
         */
        final ConcurrentMap<String, RayRecord> data = new ConcurrentHashMap<>();
        this.references.values().forEach(source -> data.putAll(source.fetchData(input)));
        /*
         * 结果处理
         */
        return input;
    }
}
