package cn.originx.uca.concrete;

import io.horizon.specification.modeler.HRecord;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static io.vertx.tp.atom.refine.Ao.LOG;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AgileFind extends AbstractAgile {
    @Override
    public Future<JsonObject> processAsync(final JsonObject record) {
        final HRecord recordRef = this.record(record);
        final String key = recordRef.key();
        LOG.Uca.info(this.getClass(), "（单）读取数据：identifier = {0}, key = {1}, data = {2}",
            this.identifier(), key, record.encode());
        return this.dao().fetchByIdAsync(key).compose(Ux::futureJ);
    }

    @Override
    public Future<JsonArray> processAsync(final JsonArray records) {
        final HRecord[] recordsRef = this.records(records);
        final List<String> keyList = Arrays.stream(recordsRef)
            .filter(Objects::nonNull)
            .map(record -> (String) record.key())
            .filter(Ut::isNotNil)
            .collect(Collectors.toList());
        LOG.Uca.info(this.getClass(), "（批）读取数据：identifier = {0}, key = {1}, data = {2}",
            this.identifier(), Ut.fromJoin(keyList), records.encode());
        return this.dao().fetchByIdAsync(keyList.toArray(new String[]{})).compose(Ux::futureA);
    }
}
