package io.vertx.mod.rbac.ruler.element;

import cn.vertxup.rbac.domain.tables.pojos.SPacket;
import cn.vertxup.rbac.domain.tables.pojos.SView;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.ClusterSerializable;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;

/**
 * 处理 h 节点，S_VIEW -> rows
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class EyeletRow implements HEyelet {
    /*
     * 单列配置：
     * {
     *     "field": []
     * }
     */
    @Override
    public Future<ClusterSerializable> ingest(final SPacket packet,
                                              final SView view) {
        if (Objects.isNull(view)) {
            return Ux.future(new JsonObject());
        }
        final JsonObject rowJ = Ut.toJObject(view.getRows());
        return Ux.future(rowJ);
    }
}
