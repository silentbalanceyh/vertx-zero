package io.vertx.mod.ambient.uca.validator;

import cn.vertxup.ambient.domain.tables.daos.XCategoryDao;
import cn.vertxup.ambient.domain.tables.pojos.XCategory;
import io.horizon.hoc.checker.HocTrue;
import io.horizon.specification.uca.HTrue;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.ClusterSerializable;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;

/**
 * 树型删除检查
 * <pre><code>
 *     1. 有两种格式的数据录入：{@link JsonObject} 和 {@link JsonArray}
 *     2. 如果两种格式数据都存在于数据库中（有记录），那么报错，返回对应错误
 *        如果数据都不存在，则跳过（删除时主要检查存在）
 * </code></pre>
 *
 * @author lang : 2023-05-27
 */
public class TreeChecker extends HocTrue<XCategory> {

    public static HTrue<XCategory> of() {
        return of(TreeChecker.class);
    }

    @Override
    public Future<Boolean> executeJAsync(final ClusterSerializable json, final JsonObject config) {
        final JsonObject condition = Ux.whereAmb(json, KName.PARENT_ID);
        return Ux.Jooq.on(XCategoryDao.class).existAsync(condition);
    }
}
