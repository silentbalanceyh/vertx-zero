package io.vertx.up.eon.configure;

import io.horizon.eon.VPath;
import io.horizon.eon.VString;
import io.horizon.eon.em.EmDS;
import io.horizon.util.HUt;
import io.vertx.up.annotations.Infusion;
import io.vertx.up.eon.em.EmSecure;
import io.vertx.up.eon.em.container.SessionType;

/**
 * 重新规划配置部分专用，针对文件部分形成梯度配置键值，完成类似 YAML 结构的配置文件
 * 集，并可以在 interface 之间形成继承模式，变量定义变得更加完善
 * 缩写含义：High Level Yaml Configuration，基础规范重新修订：
 * <pre><code>
 *     1. 最终的值采用全大写，表示常量（叶节点）
 *     2. 文件名则采用 _ 前缀，然后走二级目录
 *     3. 节点中若带有子节点，则节点采用小写
 * </code></pre>
 *
 * @author lang : 2023/4/24
 */
public interface YmlCore {
    // -------------------------- 此处为文件名部分的基础配置 ----------------------------
    /*
     * vertx-server.yml
     * vertx-inject.yml
     * vertx-error.yml
     * vertx-readable.yml
     * vertx-resolver.yml
    String INJECT = _inject;
     */
    String VERTX = "vertx";
    /*
     * vertx.yml
     *   zero:
     *     lime:
     *     vertx:                   // VertxOption
     *
     *     freedom: false           // 打开自由格式，不使用 data 规范
     */
    String LIME = "lime";
    String FREEDOM = "freedom";

    static String of(final String filename) {
        if (HUt.isNil(filename)) {
            // vertx.yml
            return VERTX + VString.DOT + VPath.SUFFIX.YML;
        } else {
            // vertx-{key}.yml
            return VERTX + VString.DASH + filename + VString.DOT + VPath.SUFFIX.YML;
        }
    }

    // vertx-resolver.yml
    interface resolver extends YmlResolver {
    }

    // vertx-readable.yml
    interface readable extends YmlReadable {
    }

    // vertx-server.yml
    interface server extends YmlServer {
    }

    // vertx-error.yml
    interface error extends YmlError {
    }

    // vertx-inject.yml

    /**
     * {@link io.vertx.up.eon.KPlugin}
     * <pre><code>
     *     - 内部扩展
     *     - 外部扩展 {@link Infusion}
     * </code></pre>
     */
    interface inject extends YmlInject {
    }

    // ------------------------- Lime Part ----------------------

    /**
     * vertx-jooq.yml
     * <pre><code>
     * jooq:
     *     orbit:
     *         hostname:
     *         instance:
     *         category: {@link EmDS.Category}
     *         jdbcUrl:
     *         username:
     *         password:
     *         driverClassName:
     *         options:
     *     provider:
     *         hostname:
     *         instance:
     *         category: {@link EmDS.Category}
     *         jdbcUrl:
     *         username:
     *         password:
     *         driverClassName:
     *         options:
     * </code></pre>
     */
    interface jooq extends YmlJooq {
    }

    /**
     * vertx-session.yml
     * <pre><code>
     * session:
     *     config:
     *         category:    {@link SessionType}
     *         store:
     *         options:
     * </code></pre>
     */
    interface session extends YmlSession {
    }

    /**
     * vertx-redis.yml
     * <pre><code>
     * redis:
     *     host:
     *     port:
     *     username:
     *     password:
     *     endpoint:
     * </code></pre>
     */
    interface redis extends YmlRedis {
    }

    /**
     * vertx-trash.yml
     * <pre><code>
     * trash:
     *     keepDay:
     * </code></pre>
     */
    interface trash extends YmlTrash {
    }

    /**
     * vertx-elasticsearch.yml
     * <pre><code>
     * elasticsearch:
     *     hostname:
     *     port:
     *     scheme:
     *     index:
     *     username:
     *     password:
     * </code></pre>
     */
    interface elasticsearch extends YmlEs {
    }

    /**
     * vertx-neo4j.yml
     * <pre><code>
     * neo4j:
     *     port:
     *     username:
     *     password:
     *     hostname:
     *     protocol:
     *     async:
     * </code></pre>
     */
    interface neo4j extends YmlNeo4j {
    }

    /**
     * vertx-secure.yml
     * <pre><code>
     * secure:
     *    jwt: {@link EmSecure.AuthWall#JWT}
     *        options:
     *            realm:
     *            jwtOptions:
     *            keyStore:
     *        provider:
     *            authenticate:
     *            authorization:
     *    digest: {@link EmSecure.AuthWall#DIGEST}
     *        options:
     *            filename:
     *        provider:
     *            authenticate:
     *            authorization:
     *    oauth2: {@link EmSecure.AuthWall#OAUTH2}
     *        options:
     *            callback:
     *        provider:
     *            authenticate:
     *            authorization:
     * cors:  {@see io.vertx.up.secure.config.CorsConfig}
     *    credentials:
     *    methods:
     *    headers:
     *    origin:
     * </code></pre>
     */
    interface cors extends YmlCors {
    }

    interface secure extends YmlSecure {
    }

    /**
     * vertx-shell.yml
     * <pre><code>
     * shell:
     *     debug:
     *     welcome:
     *         banner:
     *         version:
     *         message:
     *             environment:
     *             wait:
     *             quit:
     *             back:
     *             header:
     *             help:
     *             footer:
     *             empty:
     *             invalid:
     *             previous:
     *             usage:
     *    commands:
     *        default:
     *        defined:
     *    validate:
     *        input:
     *            required:
     *            existing:
     *        args:
     *            - start
     *            - config
     * </code></pre>
     */
    interface shell extends YmlShell {

    }

    /**
     * vertx-jet.yml
     * <pre><code>
     * router:              {@see io.vertx.mod.jet.atom.JtConfig}
     *     wall:
     *     worker:          {@link io.vertx.core.DeploymentOptions}
     *        instances:
     *     agent:           {@link io.vertx.core.DeploymentOptions}
     *        instances:
     *     unity:           {@see io.horizon.spi.environment.UnityApp}
     * deployment:          {@see io.vertx.up.atom.agent.Arrange}
     *     mode:            {@see io.vertx.up.eon.em.DeployMode}
     *     delivery:        {@link io.vertx.core.eventbus.DeliveryOptions}
     *     options:
     *        <name>:       {@link io.vertx.core.DeploymentOptions}
     *            instances:
     * </code></pre>
     */
    interface router extends YmlRouter {
    }

    interface deployment extends YmlDeployment {

    }

    /**
     * vertx-extension.yml
     * <pre><code>
     * init:
     *     configure:
     *       - component:
     *         config:
     *     compile:
     *       - component:
     *         config:
     * extension:
     *     region:
     *         component:
     *         config:
     *             prefix:
     *     auditor:
     *         component:
     *         config:
     *             include:
     *             exclude:
     * </code></pre>
     */
    interface init extends YmlInit {

    }

    interface extension extends YmlExtension {

    }

    /**
     * vertx-job.yml
     * <pre><code>
     * job:  {@see io.vertx.up.uca.job.store.JobConfig}
     *     store:
     *        component:
     *        config:
     *     client:
     *        component:
     *        config:
     *     interval:
     *        component:
     *        config:
     * </code></pre>
     */
    interface job extends YmlJob {
    }

    /**
     * vertx-cache.yml
     * <pre><code>
     * cache:
     *     l1:
     *         component:
     *         worker:
     *         address:
     *         options:
     *         matrix:
     *     l2:
     *         component:
     *         worker:
     *         address:
     *         options:
     *         matrix:
     *     l3:
     *         component:
     *         worker:
     *         address:
     *         options:
     *         matrix:
     * </code></pre>
     */
    interface cache extends YmlCache {
    }

    /**
     * vertx-development.yml
     * <pre><code>
     * development:
     *    ENV:
     *
     * </code></pre>
     */
    interface development extends YmlDevelopment {
    }

    /**
     * vertx-tp.yml
     * <pre><code>
     * shared:
     *    config:
     *        async:
     * </code></pre>
     */
    interface shared extends YmlShared {

    }

    /**
     * vertx-excel.yml
     * <pre><code>
     * excel:
     *     pen:
     *     environment:
     *       - name:
     *         path:
     *         alias:
     *     temp:
     *     tenant:
     *     mapping:    {@see io.vertx.up.plugin.booting.KConnect}
     *       - pojoFile
     *         dao
     *         pojo
     *         key
     *         unique
     *         table
     * </code></pre>
     */
    interface excel extends YmlExcel {

    }

    /**
     * vertx-rpc.yml
     * <pre><code>
     * rpc:
     *     ssl:
     *     name:
     *     addr:
     *     path:
     *     rpc_client:
     *     type: {@link EmSecure.CertType}
     *     extension:
     *     uniform:
     * etcd:
     *     micro:
     *     nodes:
     *       - host:
     *         port:
     *     timeout:
     * circuit: {@see io.vertx.circuitbreaker.CircuitBreakerOptions}
     *
     * </code></pre>
     */
    interface rpc extends YmlRpc {

    }

    interface etcd extends YmlEtcd {

    }

    interface circuit extends YmlCircuit {

    }

    /**
     * vertx-workflow.yml
     * <pre><code>
     * workflow: {@see io.vertx.mod.workflow.atom.configuration.MetaWorkflow}
     *    name:
     *    builtIn:
     *    resource:
     *    database:
     * </code></pre>
     */
    interface workflow extends YmlWorkflow {

    }

    interface module extends YmlModule {

    }
}










