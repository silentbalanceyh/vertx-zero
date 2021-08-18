package io.vertx.tp.modular.reference;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.up.commune.Record;
import io.vertx.up.unity.Ux;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * ## Multi Reference Processor
 *
 * It's the sub-class of {@link io.vertx.tp.modular.reference.AbstractRay} with {@link io.vertx.up.commune.Record}[] element.
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class RayBatch extends AbstractRay<Record[]> {
    /**
     * Critical code logical
     *
     * @param input Input element of {@link io.vertx.up.commune.Record} for multi
     *
     * @return Processed Return the modified data records.
     */
    @Override
    public Record[] exec(final Record[] input) {
        final ConcurrentMap<String, JsonArray> data = new ConcurrentHashMap<>();
        this.input.values().forEach(source -> data.putAll(source.batch(input)));
        return RayResult.combine(input, data, this.output);
    }

    @Override
    public Future<Record[]> execAsync(final Record[] input) {
        final List<Future<ConcurrentMap<String, JsonArray>>> futures = new ArrayList<>();
        this.input.values().forEach(source -> futures.add(source.batchAsync(input)));
        return this.thenCombine(futures)
                .compose(data -> Ux.future(RayResult.combine(input, data, this.output)));
    }
}
