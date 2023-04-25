package io.vertx.tp.modular.reference;

import io.horizon.specification.modeler.HRecord;
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
 * It's the sub-class of {@link io.vertx.tp.modular.reference.AbstractRay} with {@link HRecord} element.
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class RaySingle extends AbstractRay<HRecord> {
    /**
     * Critical code logical
     *
     * @param input Input element of {@link HRecord} for single
     *
     * @return Processed Return the modified data record.
     */
    @Override
    public HRecord exec(final HRecord input) {
        final ConcurrentMap<String, JsonArray> data = new ConcurrentHashMap<>();
        this.input.values().forEach(source -> data.putAll(source.single(input)));
        return RayResult.combine(input, data, this.output);
    }

    @Override
    public Future<HRecord> execAsync(final HRecord input) {
        final List<Future<ConcurrentMap<String, JsonArray>>> futures = new ArrayList<>();
        this.input.values().forEach(source -> futures.add(source.singleAsync(input)));
        return this.thenCombine(futures)
            .compose(data -> Ux.future(RayResult.combine(input, data, this.output)));
    }
}
