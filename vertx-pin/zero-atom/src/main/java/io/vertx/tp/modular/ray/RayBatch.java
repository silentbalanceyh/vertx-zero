package io.vertx.tp.modular.ray;

import io.vertx.up.commune.Record;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class RayBatch extends AbstractRay<Record[]> {
    @Override
    public Record[] doAttach(final Record[] input) {

        return input;
    }
}
