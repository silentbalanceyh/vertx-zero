package io.vertx.tp.rbac.acl.relation;

import cn.vertxup.rbac.domain.tables.pojos.SUser;
import io.vertx.tp.optic.environment.Connex;
import io.vertx.up.uca.cache.Cc;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@SuppressWarnings("all")
public class Junc {

    private static final Cc<String, Connex> CC_CONNEX = Cc.openThread();

    /*
     * ExUser 内置调用，处理 modelId / modelKey 专用
     */
    public static Connex<String> reference() {
        return (Connex<String>) CC_CONNEX.pick(ConnexModel::new, ConnexModel.class.getName());
    }

    /*
     * 原 UserExtension，根据配置文件处理专用操作用户和其他账号的链接
     */
    public static Connex<SUser> extension() {
        return (Connex<SUser>) CC_CONNEX.pick(ConnexUser::new, ConnexUser.class.getName());
    }
}
