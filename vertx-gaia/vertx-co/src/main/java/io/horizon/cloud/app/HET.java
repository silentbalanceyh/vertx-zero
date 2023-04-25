package io.horizon.cloud.app;

import io.aeon.atom.secure.Hoi;
import io.horizon.cloud.action.HCommand;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.exception.web._501NotSupportException;

/**
 * 「以太」上下文专用接口
 * 新接口，用于提取上下文环境，取词：Ether，缩写为：ET，又引申为 Environment Tenant
 * 追加多环境模式后用于提取核心上下文
 * -- 特别注意的一点是，该接口为同步接口，意为在容器环境初始化完成之后的应用提取操作
 * -- 如果容器没有初始化完成，则该接口会返回为空，有了该接口，系统就有了上下文，和标
 * -- 准框架中的 XHeader 连接
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface HET extends HCommand<JsonObject, Hoi> {

    /*
     * 最终形成的结构如：
     *                  AeonHighway       ( 位于 aeon-ambient 中 )
     * 实现：            AmbientHighway    ( 位于 zero-ambient 中 )
     *                       |
     *                       |
     * 定义：XHeader          |
     *        |         ( initialized )
     * 底层：HES ----------- HET ---------- Hoi（ 1 KApp, N KApp )
     *                       |
     *     connect   -->     o
     *                       |
     *         sigma / appId / appKey / name / code
     *
     *
     * 新版本中，有了 Hoi 就相当于拥有了上下文环境，且 Hoi 中使用静态环境，可直接在方法内部
     * 执行全局化调用，且请求过来的时候初始化 Hoi，它的初始化依靠 XHeader 中的核心数据
     */
    @Override
    Hoi configure(JsonObject input);

    /*
     * 连接当前系统构造核心配置数据集
     * appId = appJ ( 配置 )
     * 初始化时会使用的核心方法，连接当前应用也会执行该初始化
     */
    default Future<Boolean> initialize() {
        return Future.failedFuture(new _501NotSupportException(this.getClass()));
    }
}
