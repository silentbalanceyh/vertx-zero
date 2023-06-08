package io.vertx.up.eon.em;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author lang : 2023-05-31
 */
public final class EmSecure {
    private EmSecure() {
    }

    /**
     * @author <a href="http://www.origin-x.cn">Lang</a>
     */
    public enum ScDim {
        NONE,       // 无维度定义
        FLAT,       // 列表型维度定义
        TREE,       // 树型维度定义
        FOREST,     // 森林模式（多树）
    }

    /**
     * @author <a href="http://www.origin-x.cn">Lang</a>
     */
    public enum ScIn {
        NONE,       // 无数据源
        WEB,        // 静态专用
        DAO,        // 动态：静态接口
        ATOM,       // 动态：动态接口
        DEFINE,     // 自定义，组件使用模式
    }

    /**
     * ACL的作用周期
     * - DELAY：延迟执行，处理影响型请求专用
     * - EAGER：及时执行，处理当前请求专用
     * - ERROR：配置错误导致ACL的作用周期失效
     *
     * @author <a href="http://www.origin-x.cn">Lang</a>
     */
    public enum ActPhase {
        DELAY, // Delay for ACL control
        EAGER, // Eager for ACL control, this control should impact current request
        ERROR, // Error phase
    }

    /**
     * Wall type for security
     */
    public enum AuthWall {
        /*
         * Here are mode that defined by zero framework
         * Config key here and related to `rules`
         * Vert.x native standard, all below values came from vertx official guide
         *
         * Here are some tpl in `provider/handler` instead of nothing
         */
        BASIC("basic"),         // Any provider will be ok
        REDIRECT("redirect"),   // Any provider will be ok
        JWT("jwt"),
        OAUTH2("oauth2"),
        DIGEST("digest"),
        WEB("web"),
        /*
         * When you provide this value, it means that you will use Zero Extension kind
         * type of wall instead of Vert.x Native
         *
         * If the wall type value is not in these value collections, you must provide
         * defined key that could be read configuration data from `vertx-secure.yml` etc.
         */
        EXTENSION("extension");

        private static final ConcurrentMap<String, AuthWall> TYPE_MAP = new ConcurrentHashMap<>();

        static {
            Arrays.stream(AuthWall.values()).forEach(wall -> TYPE_MAP.put(wall.key(), wall));
        }

        private transient final String configKey;

        AuthWall(final String configKey) {
            this.configKey = configKey;
        }

        public static AuthWall from(final String configKey) {
            return TYPE_MAP.getOrDefault(configKey, null);
        }

        public static Set<String> keys() {
            return TYPE_MAP.keySet();
        }

        public String key() {
            return this.configKey;
        }
    }

    /**
     * @author <a href="http://www.origin-x.cn">Lang</a>
     */
    public enum AuthWord {
        AND, // Perm1 + Perm2 + Perm3
        OR,  // Perm1,  Perm2,  Perm3
    }

    public enum CertType {
        JKS,
        PKCS12,
        PEM
    }
}
