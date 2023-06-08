package io.mature.extension.stellaris.vendor;

import io.horizon.eon.VString;
import io.horizon.specification.typed.TCopy;
import io.mature.extension.stellaris.OkA;
import io.mature.extension.stellaris.OkX;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.atom.modeling.builtin.DataAtom;
import io.vertx.up.atom.exchange.BTree;
import io.vertx.up.atom.exchange.DFabric;
import io.vertx.up.commune.config.Integration;
import io.vertx.up.unity.Ux;

/**
 * ## 通道组装接口
 *
 * > 该接口是同步接口，所以主要应用于<strong>测试环境</strong>，正式环境中的配置信息直接走`I_API/I_JOB/I_SERVICE`配置。
 *
 * ### 1. 基本介绍
 *
 * 定义了<strong>通道组装专用</strong>接口，主要用于：
 *
 * - 单元测试中读取UCMDB推送专用的一次性配置。
 * - 组装UCMDB推送和CMDB访问的基础配置。
 * - 提供一次性配置的<strong>字典翻译器</strong>。
 *
 * ### 2. 核心结构
 *
 * #### 2.1. 对象说明
 *
 * |类名|说明|
 * |---:|:---|
 * |`io.vertx.mod.atom.jet.JtApp`|应用配置对象。|
 * |`io.vertx.up.commune.config.Database`|数据库配置对象。|
 * |`io.vertx.up.commune.config.Integration`|集成配置对象。|
 * |`io.vertx.up.commune.exchange.DictFabric`|字典翻译器。|
 * |`io.vertx.up.commune.exchange.DualMapping`|字段映射器。|
 *
 * #### 2.2. 一次配置
 *
 * 默认实现类：{@link PartyB}，一次配置的配置文件如下：
 *
 * - `runtime/external/once/database.json`
 * - `runtime/external/once/application.json`
 * - `runtime/external/once/integration.json`
 *
 * > 实现类可在后期使用<strong>策略</strong>或<strong>桥梁</strong>模式实现配置反射处理，下边的初始化环境是针对{@link PartyB io.extension.stellaris.sphere.UquipOnce}。
 *
 * ### 3. 初始化环境
 *
 * 调用代码：
 *
 * ```java
 * // <pre><code class="java">
 *      // 默认初始化
 *      Uquip.on();
 *
 *      // 带 Vertx 实例初始化（测试环境专用），此处 VERTX 是Vertx实例
 *      Uquip.on(VERTX);
 * // </code></pre>
 * ```
 *
 *
 * #### 3.1. 「源码」默认Vertx
 *
 * ```java
 * // <pre><code class="java">
 *      static void on() {
 *          // 默认的Vertx实例，Zero内部专用Vertx实例，io.vertx.core.Vertx类型
 *          on(Ux.nativeVertx());
 *      }
 * // </code></pre>
 * ```
 *
 * #### 3.2. 「源码」输入Vertx
 *
 * ```java
 * // <pre><code class="java">
 *     static void on(final Vertx vertx) {
 *         // Jooq
 *         Uncommon.onDB(vertx);
 *         // Elastic Search
 *         Uncommon.onES(vertx);
 *         // Excel
 *         Uncommon.onEX(vertx);
 *         // Neo4j
 *         Uncommon.onGE(vertx);
 *         // Shared Map
 *         Uncommon.onSD(vertx);
 *         // Codecs
 *         vertx.eventBus().registerDefaultCodec(Envelop.class, Ut.singleton(EnvelopCodec.class));
 *     }
 * // </code></pre>
 * ```
 *
 * #### 3.3. 初始化环境
 *
 * - Jooq数据库访问JDBC环境配置初始化。
 * - Elastic Search全文检索引擎Es环境配置初始化。
 * - Excel导入/导出环境配置初始化。
 * - Neo4j图引擎环境配置初始化。
 * - SharedMap专用缓存池环境配置初始化。
 * - Vert.x 中的 Codecs 注册——创建测试通道环境专用 Codecs。
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface OkB extends OkX, TCopy<OkB> {

    static OkB connect(final OkA okA, final Integration integration) {
        return new PartyB(okA, integration);
    }

    /**
     * 集成配置对象读取方法。
     *
     * @return {@link Integration}
     */
    Integration configIntegration();

    // ---------------- 服务配置 ---------------------

    /**
     * 对应通道定义`I_SERVICE`中的`serviceConfig`属性，服务配置，构造`options`专用。
     *
     * @return {@link JsonObject}
     */
    JsonObject configService();

    /**
     * 构造映射配置对象，专用执行字段映射处理。
     *
     * @return {@link BTree}
     */
    BTree mapping();

    // ---------------- 字典和服务配置 ---------------------

    /**
     * 「Async」异步构造默认的字典翻译器
     *
     * 该字典翻译器使用默认配置（identifier = ""）
     *
     * @return `{@link Future}<{@link DFabric}>`
     */
    default Future<DFabric> fabric() {
        return this.fabric(VString.EMPTY);
    }

    /**
     * 「Async」根据模型定义异步构造某一个模型的字典翻译器
     *
     * @param atom {@link DataAtom} 传入的模型定义对象`io.vertx.mod.atom.modeling.building.DataAtom`
     *
     * @return `{@link Future}<{@link DFabric}>`
     */
    default Future<DFabric> fabric(final DataAtom atom) {
        return this.fabric(atom.identifier()).compose(fabric -> {
            fabric.mapping().bind(atom.type());
            return Ux.future(fabric);
        });
    }

    /**
     * 「Async」根据统一标识符异步构造某一个模型的字典翻译器
     *
     * @param identifier {@link String} 传入的模型统一标识符
     *
     * @return `{@link Future}<{@link DFabric}>`
     */
    Future<DFabric> fabric(String identifier);
}
