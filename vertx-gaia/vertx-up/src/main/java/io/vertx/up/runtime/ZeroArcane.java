package io.vertx.up.runtime;

import io.vertx.aeon.eon.HPath;
import io.vertx.aeon.eon.em.TypeOs;
import io.vertx.aeon.refine.HLog;
import io.vertx.aeon.specification.app.HES;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.shared.MapInfix;
import io.vertx.up.uca.yaml.Node;
import io.vertx.up.uca.yaml.ZeroUniform;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentMap;

/**
 * Arcane:神秘的，秘密的
 * Zero新版启动器，将启动周期直接从ZeroHeart中拿掉，统一规划启动，ZeroHeart只负责判断
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class ZeroArcane {
    private static final String MSG_DEVELOPMENT = "OS = {0},  `{1}` file has been found! DEVELOPMENT connected.";
    private static final String MSG_ENV = "Zero Environment Variables: {0}\n";
    private static final String MSG_EXT_COMPONENT = "Extension components initialized {0}";
    private static final String MSG_EXT_CONFIGURATION = "Extension configuration missing {0}";


    private static final String INIT = "init";
    private static final Node<JsonObject> VISITOR = Ut.singleton(ZeroUniform.class);

    /*
     * 「Vertx启动前」启动流程一
     * 不带Vertx的专用启动流程，在Vertx实例启动之前启动，如新版Aeon系统
     * 1. 低代码链接：Github/Gitee空间初始化
     * 2. Aeon系统启动以及相关配置
     * Era: 纪元、年代
     */
    public static void startEra() {
        final TypeOs osType = Ut.envOs();
        /*
         * 判断是否开启了开发环境
         * 路径下是否包含：.env.development 环境变量文件，如存在该文件，则挂载设置环境变量
         * 1）环境变量设置会突破Java中的私有域，属于非法操作，JDK 11之后需带启动参数
         *
         */
        if (Ut.ioExist(HPath.ENV_DEVELOPMENT)) {
            // 1. 设置环境变量
            HLog.warnEnv(ZeroArcane.class, MSG_DEVELOPMENT, osType.name(), HPath.ENV_DEVELOPMENT);
            final Properties properties = Ut.ioProperties(HPath.ENV_DEVELOPMENT);
            final ConcurrentMap<String, String> written = Ut.envOut(properties);
            // 2. 打印环境变量
            final Set<String> treeSet = new TreeSet<>(written.keySet());
            final StringBuilder out = new StringBuilder();
            treeSet.forEach(name -> {
                out.append("\n\t").append(name);
                final String value = written.get(name);
                out.append(" = ").append(value);
            });
            HLog.infoEnv(ZeroArcane.class, MSG_ENV, out.toString());
        }
    }

    /*
     * 「Vertx启动后」启动流程二
     * 带Vertx的专用启动流程，在Vertx实例启动之后启动
     * 1. SharedMap提前初始化（Infix架构下所有组件的特殊组件预启动流程）
     * 2. 其他Native插件初始化
     * Elite：精华
     */
    public static void startElite(final Vertx vertx) {
        // SharedMap
        if (ZeroHeart.isShared()) {
            /*
             * Map infix initialized first to fix
             * Boot issue here to enable map infix ( SharedMap will be used widely )
             * It means that the MapInfix should started twice for safe usage in future
             *
             * In our production environment, only MapInfix plugin booting will cost some time
             * to be ready, it may take long time to be ready after container started
             * In this kind of situation, Zero container start up MapInfix internally first
             * to leave more time to be prepared.
             */
            MapInfix.init(vertx);
        }
    }

    /*
     * 「Vertx启动后」启动流程三
     * 1. Extension 启动流程，开启Zero Extension扩展模块启动
     * 2. 只有配置了 init 节点的核心模块会被直接启动
     * Edge：边界/边缘
     */
    public static void startEdge() {
        startEdge(null)
                .onComplete(res -> HLog.infoEnv(ZeroArcane.class, "Extension Initialized {0}", res.result()));
    }

    public static Future<Boolean> startEdge(final Vertx vertx) {
        // inject configuration
        final JsonObject config = VISITOR.read();
        /*
         * Check whether there exist `init` node for class
         * Each `init` clazz must be configured as
         * init:
         * - clazz: XXXX
         *   config:
         *      key1:value1
         *      key2:value2
         */
        if (config.containsKey(INIT)) {
            /* Component initializing with native */
            final JsonArray components = config.getJsonArray(INIT, new JsonArray());
            HLog.infoEnv(ZeroArcane.class, MSG_EXT_COMPONENT, components.encode());
            return Ux.nativeInit(components, vertx);
        } else {
            HLog.infoEnv(ZeroArcane.class, MSG_EXT_CONFIGURATION, config);
            return Future.succeededFuture(Boolean.TRUE);
        }
    }

    /*
     * 「Vertx启动后」启动流程四
     * *：必须在Extension启动完成后执行，且作为非标准化的扩展启动部分
     * 1. 多应用管理平台
     * 2. 多租户管理平台
     * 3. 多语言管理平台
     * Enroll：登记
     */
    public static Future<Boolean> startEnroll(final Vertx vertx) {
        return HES.configure()
                .compose(initialized -> Future.succeededFuture(Boolean.TRUE));
    }
}
