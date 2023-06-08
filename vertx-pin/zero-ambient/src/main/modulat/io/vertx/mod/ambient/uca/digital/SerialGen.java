package io.vertx.mod.ambient.uca.digital;

import cn.vertxup.ambient.domain.tables.daos.XNumberDao;
import cn.vertxup.ambient.domain.tables.pojos.XNumber;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.ambient.refine.At;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class SerialGen implements Serial {

    @Override
    public synchronized Future<JsonArray> generate(final JsonObject condition, final Integer count) {
        /* XNumber Processing */
        final UxJooq jq = Ux.Jooq.on(XNumberDao.class);
        synchronized (jq) {
            return jq.<XNumber>fetchOneAsync(condition).compose(number -> {
                if (Objects.isNull(number)) {
                    /* Not found for XNumber */
                    return Ux.futureA();
                } else {
                    /*
                     * Generate numbers
                     * 1) Generate new numbers first
                     * 2) Update numbers instead
                     */
                    return At.generateAsync(number, count).compose(generated -> {
                        final XNumber processed = At.serialAdjust(number, count);
                        return jq.updateAsync(processed)
                            .compose(nil -> Ux.future(new JsonArray(generated)));
                    }).otherwise(Ux.otherwise(JsonArray::new));
                }
            });
        }
    }

    @Override
    public synchronized Future<Boolean> reset(final JsonObject condition, final Long defaultValue) {
        final UxJooq jq = Ux.Jooq.on(XNumberDao.class);
        synchronized (jq) {
            return jq.<XNumber>fetchOneAsync(condition).compose(number -> {
                if (Objects.isNull(number)) {
                    return Ux.futureT();
                } else {
                    number.setCurrent(defaultValue); // The Current Value Start From 1
                    return jq.updateAsync(number)
                        .compose(nil -> Ux.futureT());
                }
            });
        }
    }
}
