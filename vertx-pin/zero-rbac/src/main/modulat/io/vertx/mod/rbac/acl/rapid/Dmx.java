package io.vertx.mod.rbac.acl.rapid;

import cn.vertxup.rbac.domain.tables.pojos.SVisitant;
import io.horizon.uca.cache.Cc;
import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

/**
 * Quit Normalizer for Matrix ( View )
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Dmx {

    Cc<String, Dmx> CC_QMX = Cc.openThread();

    static Dmx outlet(final Class<?> clazz) {
        return CC_QMX.pick(() -> Ut.instance(clazz), clazz.getName());
    }

    void output(SVisitant visitant, JsonObject matrixJ);
}
