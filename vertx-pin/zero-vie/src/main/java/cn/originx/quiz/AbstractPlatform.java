package cn.originx.quiz;

import cn.originx.quiz.atom.QModel;
import cn.originx.quiz.atom.QOk;
import cn.originx.quiz.atom.QRequest;
import cn.originx.refine.Ox;
import cn.originx.stellaris.Ok;
import cn.originx.stellaris.OkA;
import cn.originx.uca.elasticsearch.EsIndex;
import cn.originx.uca.graphic.Plotter;
import cn.originx.uca.graphic.TopologyPlotter;
import io.horizon.eon.em.ChangeFlag;
import io.horizon.eon.em.Environment;
import io.horizon.specification.modeler.HDao;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.tp.atom.modeling.builtin.DataAtom;
import io.vertx.tp.jet.atom.JtApp;
import io.vertx.tp.modular.jdbc.AoConnection;
import io.vertx.tp.modular.jdbc.DataConnection;
import io.vertx.tp.modular.jdbc.Pin;
import io.vertx.tp.modular.metadata.AoBuilder;
import io.vertx.tp.plugin.elasticsearch.ElasticSearchClient;
import io.vertx.tp.plugin.elasticsearch.ElasticSearchInfix;
import io.vertx.tp.plugin.neo4j.Neo4jClient;
import io.vertx.tp.plugin.neo4j.Neo4jInfix;
import io.vertx.up.commune.config.Database;
import io.vertx.up.eon.KWeb;
import io.vertx.up.quiz.JooqBase;
import io.vertx.up.util.Ut;
import org.junit.Before;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;

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
public abstract class AbstractPlatform extends JooqBase {
    // 基本环境变量
    protected final transient Environment environment;
    protected transient OkA ok;

    // -------------------- 双构造方法 ---------------------
    public AbstractPlatform(final Environment environment) {
        this.environment = environment;
    }

    public AbstractPlatform() {
        this(Environment.Mockito);
    }

    // -------------------- 环境设置方法 ---------------------
    @Before
    @SuppressWarnings("all")
    public void setUp(final TestContext context, final Async async) {
        Ok.ok().onSuccess(initialized -> {
            this.ok = QOk.create(initialized, this.environment);
            this.logger().info("[ Qz ] Qz Framework has been initialized!!! Env = `{0}`", this.environment);
            final boolean runnable = this.setUpAfter(context, async);
            if (runnable) {
                async.complete();
            }
        });
    }

    public boolean setUpAfter(final TestContext context, final Async async) {
        return true;
    }

    // -------------------- 参数相关信息，平台端专用方法 ---------------------
    protected JtApp app() {
        return Objects.requireNonNull(this.ok).configApp();
    }

    protected Database database() {
        return Objects.requireNonNull(this.ok).configDatabase();
    }

    protected DataAtom atom(final String identifier) {
        return Ox.toAtom(this.app().getAppId(), identifier);
    }

    // -------------------- 访问器引用获取 ---------------------
    // 动态Dao
    protected HDao dbDao(final String identifier) {
        return Ox.toDao(this.atom(identifier));
    }

    // 元数据构造器
    protected AoBuilder dbBuilder() {
        final Database database = this.ok.configDatabase();
        final Pin pin = Pin.getInstance();
        return pin.getBuilder(database);
    }

    // 连接构造器
    protected AoConnection dbConnection() {
        final Database database = this.ok.configDatabase();
        return new DataConnection(database);
    }

    // Neo4J 绘图仪
    protected Plotter neo4P() {
        final JtApp app = this.ok.configApp();
        final Plotter plotter = new TopologyPlotter();
        plotter.bind(app);
        return plotter;
    }

    // Neo4J 原生客户端
    protected Neo4jClient neo4j() {
        return Neo4jInfix.getClient().connect(KWeb.DEPLOY.VERTX_GROUP);
    }

    // Es 原生客户端
    protected ElasticSearchClient es() {
        return ElasticSearchInfix.getClient();
    }

    // Es 索引创建器
    protected EsIndex es(final ChangeFlag flag, final String identifier) {
        return EsIndex.create(flag, identifier);
    }

    // -------------------- 输入专用 ---------------------
    /*
     * 基础模型
     * {
     *     "identifier": "模型标识符",
     *     "key": "主键",
     *     "data": {
     *
     *     }
     * }
     */
    protected QModel inData(final String filename) {
        final JsonObject raw = this.ioJObject(filename);
        final String identifier = raw.getString("identifier");
        final Object dataPart = raw.getValue("data");
        final String key = raw.getString("key");
        return this.inData(identifier, key, dataPart);
    }

    protected QModel inData(final String identifier, final String key) {
        return this.inData(identifier, key, null);
    }

    protected QModel inData(final String identifier, final Object dataPart) {
        return this.inData(identifier, null, dataPart);
    }

    protected QModel inData(final String identifier, final String key, final Object dataPart) {
        final QModel input;
        if (dataPart instanceof JsonArray) {
            input = new QModel(this.atom(identifier), (JsonArray) dataPart);
        } else if (dataPart instanceof JsonObject) {
            input = new QModel(this.atom(identifier), (JsonObject) dataPart);
        } else {
            if (Ut.isNotNil(key)) {
                input = new QModel(this.atom(identifier), key);
            } else {
                throw new RuntimeException("构造 QModel 异常，检查测试相关配置！");
            }
        }
        return input;
    }

    /*
     * 请求模型
     *     {
     *          "request": "请求唯一标识",
     *          "data": "支持两种格式，[]和{}，对应JsonObject和JsonArray",
     *          "headers": {
     *              "...": "请求头哈希表结构，存储appId, appKey, sigma等..."
     *          }
     *     }
     */
    protected QRequest inWeb(final String filename) {
        return new QRequest(this.ioJObject(filename));
    }

    protected QRequest inWeb(final String key, final Object dataPart) {
        final JsonObject data = new JsonObject();
        data.put("request", key);
        data.put("data", dataPart);
        return new QRequest(data);
    }

    /*
     * 写文件
     */
    protected void ioOut(final String filename, final JsonObject content) {
        if (Ut.isNotNil(filename)) {
            final String resolved = Ox.toRoot(filename, this.environment);
            Ut.ioOut(resolved, content);
        }
    }

    // -------------------- 子类专用 ---------------------
    protected <T> Consumer<String> tcDao(final TestContext context,
                                         final BiFunction<QModel, HDao, Future<T>> consumer,
                                         final Consumer<T> callback) {
        return filename -> {
            final QModel model = this.inData(filename);
            final HDao dao = this.dbDao(model.identifier());
            this.tcAsync(context, consumer.apply(model, dao), callback);
        };
    }

    protected <T> Consumer<String> tcDao(final TestContext context,
                                         final BiFunction<QModel, HDao, Future<T>> consumer) {
        return filename -> {
            final QModel model = this.inData(filename);
            final HDao dao = this.dbDao(model.identifier());
            this.tcAsync(context, consumer.apply(model, dao),
                actual -> this.logger().info("[ Qz ] 执行完成：{0}", actual));
        };
    }
}
