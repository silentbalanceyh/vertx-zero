package cn.originx.scaffold.component;

import io.vertx.up.annotations.Contract;
import io.vertx.up.atom.worker.Mission;

/**
 * ## 「Actor」顶层调度器
 *
 * 在连接器{@link AbstractActor}上追加任务配置{@link Mission}。
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractActor extends AbstractConnector {
    /**
     * 「合约」{@link Mission}任务配置成员，从`I_JOB`中提取任务配置并执行计算。
     */
    @Contract
    private transient Mission mission;

    /**
     * 返回当前通道构造的任务配置信息。
     *
     * @return {@link Mission}任务配置。
     */
    protected Mission mission() {
        return this.mission;
    }
}
