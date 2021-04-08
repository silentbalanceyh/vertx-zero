package io.vertx.tp.modular.ray;

import io.vertx.up.commune.Record;
import io.vertx.up.commune.element.JAmb;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * ## Multi Reference Processor
 *
 * It's the sub-class of {@link io.vertx.tp.modular.ray.AbstractRay} with {@link io.vertx.up.commune.Record}[] element.
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
    public Record[] doAttach(final Record[] input) {
        final ConcurrentMap<String, JAmb> data = new ConcurrentHashMap<>();
        this.refer.values().forEach(source -> data.putAll(source.fetchBatch(input)));
        return RayResult.combine(input, data, this.referRules);
    }
}
