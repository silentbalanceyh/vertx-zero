package cn.originx.quiz;

import cn.originx.stellaris.Ok;
import io.vertx.core.Future;
import io.vertx.quiz.ZeroBase;
import io.vertx.up.eon.em.Environment;
import org.junit.Before;

/**
 * ## Ox专用测试框架基类
 *
 * ### 1. 测试路径
 *
 * - `test/channel/cmdb-v2/dict-config/ucmdb.json`
 * - `test/channel/cmdb-v2/dict-epsilon/ucmdb.json`
 * - `test/channel/cmdb-v2/mapping/ucmdb.json`
 * - `test/channel/cmdb-v2/options/ucmdb.json`
 *
 * > 默认配置`ucmdb.json`可通过`connect`的API实现配置文件切换。
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractQuiz extends ZeroBase {
    // 基本环境变量
    protected final transient Environment environment;
    protected transient Ok ok;

    // -------------------- 测试用例执行方法 ---------------------
    public AbstractQuiz(final Environment environment) {
        this.environment = environment;
    }

    public AbstractQuiz() {
        this(Environment.Mockito);
    }

    @Before
    @SuppressWarnings("all")
    public Future<Void> setUp() {
        return Ok.ok().compose(initialized -> {
            this.ok = initialized;
            this.logger().info("[ Qz ] Qz Framework has been initialized!!! {0} = ", this.environment);
            return Future.succeededFuture();
        });
    }
}
