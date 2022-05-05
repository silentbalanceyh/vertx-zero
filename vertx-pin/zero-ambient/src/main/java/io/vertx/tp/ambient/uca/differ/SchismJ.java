package io.vertx.tp.ambient.uca.differ;

import cn.vertxup.ambient.domain.tables.pojos.XActivity;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class SchismJ extends AbstractSchism {
    @Override
    public Future<JsonObject> diffAsync(
        final JsonObject previous, final JsonObject current, final Supplier<Future<XActivity>> activityFn) {
        Objects.requireNonNull(activityFn);
        /*
         * The activityFn will create new XActivity record for current comparing
         * 1) The basic condition is Ok
         * 2) The required field will be filled in current method
         */
        return activityFn.get().compose(activity -> {
            /*
             *
             */
            return Ux.futureJ();
        });
    }
}
