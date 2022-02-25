package cn.originx.migration.tookit;

import cn.originx.migration.AbstractStep;
import cn.originx.refine.Ox;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.database.DataPool;
import io.vertx.tp.plugin.jooq.JooqDsl;
import io.vertx.tp.plugin.jooq.JooqInfix;
import io.vertx.up.annotations.Contract;
import io.vertx.up.eon.em.Environment;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import org.jooq.DSLContext;

import java.util.Objects;

@SuppressWarnings("all")
public abstract class AbstractTool extends AbstractStep {

    @Contract
    protected transient String folder;

    public AbstractTool(final Environment environment) {
        super(environment);
    }

    /*
     * Jooq 专用静态处理共享方法，子类调用
     * 主要读取备份专用目录
     */
    protected String ioBackup(final JsonObject config, final String folder) {
        /*
         * 目录创建
         */
        final String table = this.table();
        final String root = this.ioRoot(config);
        final String targetFolder = root + "backup/" + folder + "/" + this.folder + "/" + table;
        final boolean created = Ut.ioOut(targetFolder);
        if (created) {
            Ox.Log.infoShell(this.getClass(), "目录创建结果：{0}, 目录：{1}",
                created, targetFolder);
        }
        return targetFolder;
    }

    /*
     * 清空表中数据
     */
    protected void tableEmpty(final String table) {
        final String sql = "TRUNCATE " + table;
        final DataPool pool = this.pool();
        final DSLContext context = pool.getExecutor();
        context.execute(sql);
    }

    /*
     * 当前操作表名
     */
    public abstract String table();

    /*
     * 读取专用的 DataPool
     */
    public abstract DataPool pool();

    /*
     * 返回当前的 Dao Class
     */
    public abstract Class<?> dao();


    protected Future<JsonObject> ioAsync(final JsonObject data,
                                         final JsonObject config,
                                         final Integer page) {
        /*
         * 目录创建
         */
        final String targetFolder = this.ioBackup(config, "data");
        /*
         * 文件名称生成
         */
        final String replaced = Ut.fromAdjust(page, 3, '0');
        final String file = targetFolder + "/data" + replaced + ".zero";
        /*
         * 写入文件
         */
        final JsonArray result = Ut.valueJArray(data.getJsonArray("list"));
        Ox.Log.infoShell(this.getClass(), "第 {2} 页，处理 {0} 条, 文件：{1}",
            String.valueOf(result.size()), file, String.valueOf(page));
        Ut.ioOutCompress(file, result);
        return Ux.future(data);
    }

    protected Future<JsonArray> ioAsync(final String file) {
        final String literal = Ut.ioCompress(file);
        final JsonArray data = Ut.toJArray(literal);
        final Class<?> type = ioType();
        if (Objects.isNull(type)) {
            return Ux.future(new JsonArray());
        }
        return Ux.Jooq.on(this.dao())
            .insertAsync(Ux.fromJson(data, type))
            .compose(Ux::futureA);
    }

    protected Class<?> ioType() {
        final JooqDsl dsl = JooqInfix.getDao(this.dao());
        return Ut.field(dsl.dao(), "type");
    }
}
