package io.vertx.mod.rbac.acl.rapid;

import cn.vertxup.rbac.domain.tables.pojos.SVisitant;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.rbac.cv.em.QVHMode;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class DmxRow implements Dmx {
    @Override
    public void output(final SVisitant visitant, final JsonObject matrixJ) {
        final JsonObject dmRow = Ut.toJObject(visitant.getDmRow());
        if (Ut.isNil(dmRow)) {
            return;
        }
        final QVHMode mode = Ut.toEnum(visitant::getMode, QVHMode.class, QVHMode.REPLACE);
        // dmRow 只支持两种模式（ REPLACE / EXTEND )
        final JsonObject rowRef = matrixJ.getJsonObject(KName.Rbac.ROWS);
        if (QVHMode.REPLACE == mode || Ut.isNil(rowRef)) {
            // REPLACE 或 View 中没有值
            matrixJ.put(KName.Rbac.ROWS, dmRow);
        }
    }
}
