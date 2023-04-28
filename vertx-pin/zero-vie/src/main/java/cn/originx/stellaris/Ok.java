package cn.originx.stellaris;

import cn.originx.refine.Ox;
import cn.originx.stellaris.vendor.OkB;
import io.horizon.spi.environment.Ambient;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.builtin.DataAtom;
import io.vertx.tp.error._417DataAtomNullException;
import io.vertx.tp.jet.atom.JtApp;
import io.vertx.up.atom.unity.UTenant;
import io.vertx.up.commune.config.Database;
import io.vertx.up.commune.config.Integration;
import io.vertx.up.commune.exchange.DFabric;
import io.vertx.up.eon.KName;
import io.horizon.uca.log.Annal;
import io.vertx.up.runtime.ZeroArcane;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class Ok implements OkA {
    private static final Annal LOGGER = Annal.get(Ok.class);
    private static Ok INSTANCE;
    /*
     * 新版增加 VENDOR, TENANT
     * - TENANT 甲方
     * - VENDOR 乙方
     */
    private transient final ConcurrentMap<String, OkB> vendors = new ConcurrentHashMap<>();
    private final transient UTenant tenant;
    /*
     * 解析结果
     */
    private transient JtApp app;
    private transient boolean initialized = false;

    private Ok(final JsonObject tenantData) {
        this.tenant = Ut.deserialize(tenantData, UTenant.class);
        this.tenant.vendors().forEach(name -> {
            LOGGER.info("[ Ok ] Vendor {0} has been created!", name);
            final Integration integration = this.tenant.integration(name);
            this.vendors.put(name, OkB.connect(this, integration));
        });
    }

    // --------------- 静态公开API ---------------
    public static void on(final Handler<AsyncResult<Ok>> handler) {
        final Ok ok = configure();
        /*
         * 旧代码
         * handler.handle(ok.initializeAmbient())
         * 顺序不对，应该是future执行完成后让handler捕捉onSuccess的结果，而不是
         * 在此处直接调用 handler.handle 前一个结果，此处 handler 要等待 Future<Ok> 执行
         * 完成
         */
        final Future<Ok> future = ok.initializeAmbient();
        future.onComplete(res -> {
            if (res.succeeded()) {
                handler.handle(Future.succeededFuture(res.result()));
            } else {
                if (Objects.nonNull(res.cause())) {
                    res.cause().printStackTrace();
                }
            }
        });
    }

    /**
     * 「Async」根据传入的模型定义对象构造对应的字典翻译器。
     */
    public static Future<DFabric> fabric(final DataAtom atom, final String bName) {
        if (Objects.isNull(atom)) {
            return Future.failedFuture(new _417DataAtomNullException(Ok.class));
        } else {
            return ok().compose(initialized -> {
                final OkB partyB = initialized.partyB(bName);
                return partyB.fabric(atom.identifier()).compose(fabric -> {
                    fabric.mapping().bind(atom.type());
                    return Ux.future(fabric);
                });
            });
        }
    }

    public static Future<JtApp> app() {
        return ok().compose(initialized -> Ux.future(initialized.configApp()));
    }

    public static Future<OkB> vendor(final String name) {
        return ok().compose(initialized -> Ux.future(initialized.partyB(name)));
    }

    public static Future<Ok> ok() {
        final Ok ok = configure();
        final Future<Ok> future;
        if (ok.initialized) {
            future = Ux.future(ok);
        } else {
            future = ok.initializeAmbient();
        }
        return future;
    }

    // --------------- 环境初始化专用 ---------------
    /*
     * 第一步：Ok初始化专用
     */
    private static Ok configure() {
        if (Objects.isNull(INSTANCE)) {
            final String stellar = Ox.stellarConnect();
            final JsonObject data = Ut.ioJObject(stellar);
            INSTANCE = new Ok(data);
        }
        return INSTANCE;
    }

    /*
     * 第二步：环境初始化，替换原始的 Uquip.on，并带异步回调
     */
    private Future<Ok> initializeAmbient() {
        final Vertx vertx = Ux.nativeVertx();
        if (!this.initialized) {
            OInfix.on(vertx);
            LOGGER.info("[ Ok ] Zero Infix has been initialized!! = {0}", this.tenant);
            return ZeroArcane.startEdge(vertx).compose(nil -> {
                // 应用初始化
                final JsonObject app = this.tenant.getApplication();
                final String sigma = app.getString(KName.SIGMA);
                final String appId = app.getString(KName.APP_ID);
                if (Ut.isNil(appId)) {
                    this.app = Ambient.getApp(sigma);
                } else {
                    this.app = Ambient.getApp(appId);
                }
                // 环境初始化完成
                LOGGER.info("[ Ok ] Tenant Ambient has been initialized!! = {0}", this.tenant);
                this.initialized = true;
                return Future.succeededFuture(this);
            });
        } else {
            return Future.succeededFuture(this);
        }
    }

    // --------------- 专用API ---------------
    @Override
    public boolean initialized() {
        return this.initialized;
    }

    @Override
    public UTenant partyA() {
        return this.tenant;
    }

    @Override
    public OkB partyB(final String name) {
        return this.vendors.get(name);
    }

    @Override
    public JtApp configApp() {
        return this.app;
    }
    // --------------- 静态API，默认模式 ---------------

    @Override
    public Database configDatabase() {
        return Objects.requireNonNull(this.app).getSource();
    }
}
