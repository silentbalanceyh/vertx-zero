package io.vertx.tp.rbac.acl.rapier;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.secure.Acl;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class SyntaxData {
    /*
     * 计算 `syntax` 用于生成条件，然后系统会根据条件读取 visitant 对象
     */
    static Future<Acl> visitAcl(final JsonObject bodyData, final JsonObject matrix) {
        return null;
    }
}
