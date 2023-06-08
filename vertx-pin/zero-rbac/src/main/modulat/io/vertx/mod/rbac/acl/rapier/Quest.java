package io.vertx.mod.rbac.acl.rapier;

import cn.vertxup.rbac.domain.tables.pojos.SPacket;
import io.horizon.uca.cache.Cc;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.rbac.atom.ScOwner;
import io.vertx.up.commune.Envelop;

import java.util.List;

/**
 * 资源访问者统一新接口，统一资源访问者逻辑
 * 1) 消费端
 * 2) 管理端
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Quest {

    Cc<String, Quest> CC_QUEST = Cc.openThread();

    static Quest syntax() {
        return CC_QUEST.pick(QuestAcl::new);
    }

    /*
     * 管理端
     * JsonObject = {
     *     此处Json格式是 SPath 转换过来的专用格式（构造资源访问者的输入专用）
     * }
     * List<SPacket> packets
     * ScOwner
     * 输出
     * {
     *     "resource": {
     *         "h", "q", "v",
     *         "visit": []
     *     }
     * }
     */
    Future<JsonObject> fetchAsync(JsonObject input, List<SPacket> packets, ScOwner owner);

    /*
     * 管理端保存（资源访问者保存
     */
    Future<JsonObject> syncAsync(JsonObject resourceJ);

    /*
     * 消费端
     * JsonObject = {
     * }
     * 输出
     * DataAcl
     * -- LIST
     * -- FORM
     */
    Future<Envelop> beforeAsync(Envelop request, JsonObject matrixJ);

    Future<Envelop> afterAsync(Envelop request, JsonObject matrixJ);
}
