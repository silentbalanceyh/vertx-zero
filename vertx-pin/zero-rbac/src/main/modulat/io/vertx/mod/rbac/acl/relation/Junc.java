package io.vertx.mod.rbac.acl.relation;

import cn.vertxup.rbac.domain.tables.pojos.SUser;
import io.horizon.uca.cache.Cc;
import io.vertx.core.json.JsonArray;
import io.vertx.mod.ke.secure.Tie;
import io.vertx.mod.ke.secure.Twine;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@SuppressWarnings("all")
public class Junc {

    private static final Cc<String, Twine> CC_TWINE = Cc.openThread();

    private static final Cc<String, Tie> CC_TIE = Cc.openThread();

    /*
     * ExUser 内置调用，处理 modelId / modelKey 专用
     * -- 旧版本使用 modelId / modelKey 更新 S_USER 专用
     **/
    public static Twine<String> refModel() {
        return (Twine<String>) CC_TWINE.pick(TwineModel::new, TwineModel.class.getName());
    }

    /*
     * 原 UserExtension，根据配置文件处理专用操作用户和其他账号的链接
     * -- 新版本使用 S_USER 和配置中对应类型的账号相关联专用
     * */
    public static Twine<SUser> refExtension() {
        return (Twine<SUser>) CC_TWINE.pick(TwineExtension::new, TwineExtension.class.getName());
    }

    public static Twine<String> refRights() {
        return (Twine<String>) CC_TWINE.pick(TwineRights::new, TwineRights.class.getName());
    }

    public static Tie<String, JsonArray> role() {
        return (Tie<String, JsonArray>) CC_TIE.pick(TieRole::new, TieRole.class.getName());
    }

    public static Tie<String, JsonArray> group() {
        return (Tie<String, JsonArray>) CC_TIE.pick(TieGroup::new, TieGroup.class.getName());
    }
}
