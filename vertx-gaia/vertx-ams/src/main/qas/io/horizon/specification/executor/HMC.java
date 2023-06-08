package io.horizon.specification.executor;

import io.horizon.specification.typed.TExecutor;
import io.macrocosm.specification.app.HModule;

/**
 * 「模块执行器」Module Component
 * <hr/>
 *
 * 执行器分为不同的类型，且不同类型携带的业务属性不相同，核心分类如下：
 * <pre><code>
 * -- 系统级执行器，不隶属于任何一个模块，不支持热部署
 *    {@link HBackup}
 *    {@link HRestore}
 *    这种模式下不启动 {@link HMC} 接口
 * -- 模块执行器，父接口为 {@link HMC}，携带了关于模块的业务信息
 *    - 支持热部署
 *    - 支持Bundle模式
 *    - 支持应用属性
 * </code></pre>
 *
 * @author lang : 2023-05-21
 */
public interface HMC extends TExecutor {
    /**
     * 当前组件所属模块相关信息
     *
     * @return {@link HModule}
     */
    HModule module();
}
