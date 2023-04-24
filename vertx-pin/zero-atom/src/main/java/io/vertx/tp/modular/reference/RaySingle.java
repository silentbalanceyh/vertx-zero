package io.vertx.tp.modular.reference;

import io.horizon.specification.modeler.Record;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.up.unity.Ux;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * ## Single Reference Processor
 *
 * It's the sub-class of {@link io.vertx.tp.modular.reference.AbstractRay} with {@link Record} element.
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class RaySingle extends AbstractRay<Record> {
    /**
     * Critical code logical
     *
     * @param input Input element of {@link Record} for single
     *
     * @return Processed Return the modified data record.
     */
    @Override
    public Record exec(final Record input) {
        final ConcurrentMap<String, JsonArray> data = new ConcurrentHashMap<>();
        this.input.values().forEach(source -> data.putAll(source.single(input)));
        return RayResult.combine(input, data, this.output);
    }

    @Override
    public Future<Record> execAsync(final Record input) {
        final List<Future<ConcurrentMap<String, JsonArray>>> futures = new ArrayList<>();
        this.input.values().forEach(source -> futures.add(source.singleAsync(input)));
        return this.thenCombine(futures)
            .compose(data -> Ux.future(RayResult.combine(input, data, this.output)));
    }
}
