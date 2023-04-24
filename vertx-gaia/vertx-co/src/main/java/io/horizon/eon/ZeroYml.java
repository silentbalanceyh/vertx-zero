package io.horizon.eon;

/**
 * 重新规划配置部分专用，针对文件部分形成梯度配置键值，完成类似 YAML 结构的配置文件
 * 集，并可以在 interface 之间形成继承模式，变量定义变得更加完善
 * 缩写含义：High Level Yaml Configuration
 * 配合 yml 文件中的信息，值采用小写，节点则采用大写
 *
 * @author lang : 2023/4/24
 */
public interface ZeroYml {
    String freedom = "freedom";
    /*
     * vertx-server.yml
     * vertx-inject.yml
     * vertx-error.yml
     * vertx-resolver.yml
     */
    String _server = "server";
    String _inject = "inject";
    String _error = "error";
    String _resolver = "resolver";

    /*
     * vertx.yml
     *   zero:
     *     lime:
     *       jooq:
     *         orbit:
     *         provider:
     *     vertx:                   // VertxOption
     *
     *     freedom: false           // 打开自由格式，不使用 data 规范
     */
    interface lime {
        String V = "lime";

        interface jooq {
            String orbit = "orbit";
            String provider = "provider";
        }
    }

    interface inject {

        String mongo = "mongo";

        String mysql = "mysql";

        String redis = "redis";

        String session = "session";

        String shared = "shared";


        String jooq = "jooq";

        String rpc = "rpc";

        String secure = "secure";

        String logger = "logger";
    }
}
