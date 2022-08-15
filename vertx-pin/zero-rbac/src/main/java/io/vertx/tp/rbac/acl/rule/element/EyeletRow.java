package io.vertx.tp.rbac.acl.rule.element;

import cn.vertxup.rbac.domain.tables.pojos.SPacket;
import cn.vertxup.rbac.domain.tables.pojos.SView;
import io.vertx.core.Future;
import io.vertx.core.shareddata.ClusterSerializable;

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

        return null;
    }
}
