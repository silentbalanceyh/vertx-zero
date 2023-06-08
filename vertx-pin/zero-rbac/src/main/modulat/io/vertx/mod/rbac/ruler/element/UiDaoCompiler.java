package io.vertx.mod.rbac.ruler.element;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.rbac.error._404AdmitDaoNullException;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class UiDaoCompiler implements HAdmitCompiler {
    @Override
    public Future<JsonArray> ingest(final JsonObject qr, final JsonObject config) {
        final String daoStr = Ut.valueString(config, KName.DAO);
        final Class<?> daoCls = Ut.clazz(daoStr, null);

        // Error-80226, uiConfig中没有配置dao节点
        Fn.out(Objects.isNull(daoCls), _404AdmitDaoNullException.class, this.getClass(), daoStr);
        return Ux.Jooq.on(daoCls).fetchJAsync(qr);
    }
}
