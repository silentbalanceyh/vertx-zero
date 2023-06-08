package io.macrocosm.specification.boot;

import io.horizon.annotations.reference.One2One;
import io.horizon.specification.typed.TDescriptor;
import io.macrocosm.specification.app.HBackend;
import io.macrocosm.specification.program.HArk;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * 「部署计划」Deployment
 * <hr/>
 * 正式应用上线时，用户会直接从管理端创建一次部署计划，部署流程如下：
 * <pre><code>
 *     1. 管理端直接创建部署计划
 *     2. 系统解析工程部署描述符所有文件
 *
 *     3. 提取资源文件，进行资源文件的上传
 *     4. 初始化应用配置容器
 *     5. 配置初始化为应用运行做准备
 *        - 激活后端模块
 *        - 激活前端模块
 *        - 激活集成连接器
 *        - 拷贝资源文件
 *        - 配置验证
 *     6. 部署最终验证
 *     7. 启动部署完成之后的容器
 * </code></pre>
 *
 * @author lang : 2023-05-21
 */
public interface HDeployment {
    /**
     * 部署描述符，为部署做基础准备
     *
     * @return {@link TDescriptor}
     */
    @One2One
    TDescriptor descriptor();

    /**
     * 部署过程中的执行器，可直接执行当前部署，使用Java语言实现反射调用，编连成
     * 核心部署流程（发布流管理），部署计划只提供了部署的基本元数据定义和相关配置
     * 信息，现阶段不执行编排，编排是执行编排器组件做的事。
     *
     * @return {@link List<Class>}
     */
    Set<Class<?>> executors();

    /**
     * 和执行器绑定的附加配置，每种执行器都有自己的配置
     *
     * @return {@link ConcurrentMap}
     */
    ConcurrentMap<Class<?>, JsonObject> configuration();

    /**
     * 当前部署计划的目标容器对象，该对象会帮助应用实现完整的部署流程
     *
     * @return {@link HArk}
     */
    @One2One
    HArk target();

    /**
     * 当前部署计划来源，通常是 {@link HBackend} 管理端创建
     *
     * @return {@link HBackend}
     */
    HBackend source();
}
