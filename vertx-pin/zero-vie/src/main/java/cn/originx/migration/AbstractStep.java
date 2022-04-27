package cn.originx.migration;

import cn.originx.refine.Ox;
import cn.vertxup.ambient.service.DatumService;
import cn.vertxup.ambient.service.DatumStub;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.builtin.DataAtom;
import io.vertx.tp.atom.refine.Ao;
import io.vertx.tp.jet.atom.JtApp;
import io.vertx.up.eon.em.Environment;
import io.vertx.up.experiment.mixture.HDao;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

public abstract class AbstractStep implements MigrateStep {
    /*
     * 子类共享（Development/Production）
     */
    protected final transient Environment environment;
    /*
     * 子类共享：XNumber, XTabular
     */
    protected final transient DatumStub stub = Ut.singleton(DatumService.class);
    /*
     * 子类共享：XApp, XSource
     */
    protected transient JtApp app;

    public AbstractStep(final Environment environment) {
        this.environment = environment;
    }

    @Override
    public MigrateStep bind(final JtApp app) {
        /*
         * 数据库切换隔离操作
         */
        // this.app = DatabaseSwitcher.on(app);
        this.app = app;
        return this;
    }

    protected HDao ioDao(final String identifier) {
        final DataAtom atom = Ox.toAtom(this.app.getAppId(), identifier);
        return Ao.toDao(atom, this.app.getSource());
    }

    protected String ioRoot(final JsonObject config) {
        final String output = config.getString("output");
        String folder = Ox.toRoot("migration/" + output, this.environment);
        if (!folder.endsWith("/")) {
            folder = folder + "/";
        }
        return folder;
    }

    protected Future<JsonArray> writeAsync(final JsonArray combined, final String file) {
        Ox.Log.infoShell(this.getClass(), "写入数据（A)：{0}", file);
        /*
         * 过滤 null
         */
        final JsonArray normalized = new JsonArray();
        Ut.itJArray(combined).forEach(normalized::add);
        Ut.ioOut(file, normalized);
        return Ux.future(normalized);
    }

    protected Future<JsonArray> writeCompressAsync(final JsonArray combined, final String file) {
        Ox.Log.infoShell(this.getClass(), "写入压缩数据（A)：{0}", file);
        final JsonArray normalized = new JsonArray();
        Ut.itJArray(combined).forEach(normalized::add);
        Ut.ioOutCompress(file, normalized);
        return Ux.future(normalized);
    }

    protected void banner(final String title) {
        System.out.println();
        System.out.println("========> " + title + "         ");
        System.out.println();
    }

    /*
     * 配置层专用处理，注意合约模式
     */
    public Future<JsonObject> aspectAsync(final JsonObject config, final String key) {
        return Around.create(this.environment).bind(this.app)
            .aspectAsync(config, key);
    }
}
