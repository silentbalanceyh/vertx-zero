package cn.originx.uca.concrete;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;

import static io.vertx.tp.atom.refine.Ao.LOG;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AgileAdd extends AbstractAgile {
    @Override
    public Future<JsonObject> processAsync(final JsonObject record) {
        LOG.Uca.info(this.getClass(), "（单）插入数据：identifier = {0}, data = {1}",
            this.identifier(), record.encode());
        return this.dao().insertAsync(this.record(record)).compose(Ux::futureJ);
    }

    @Override
    public Future<JsonArray> processAsync(final JsonArray records) {
        LOG.Uca.info(this.getClass(), "（批）插入数据：identifier = {0}, data = {1}",
            this.identifier(), records.encode());
        return this.dao().insertAsync(this.records(records)).compose(Ux::futureA);
    }
}
