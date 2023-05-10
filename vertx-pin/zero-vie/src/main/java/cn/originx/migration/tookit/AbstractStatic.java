package cn.originx.migration.tookit;

import cn.originx.migration.MigrateStep;
import io.horizon.eon.em.Environment;
import io.horizon.uca.qr.Pager;
import io.horizon.uca.qr.Sorter;
import io.horizon.uca.qr.syntax.Ir;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.database.DataPool;
import io.vertx.up.annotations.Contract;
import io.vertx.up.eon.KName;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.function.Supplier;

public abstract class AbstractStatic extends AbstractTool {
    @Contract
    protected transient UxJooq jooq;
    @Contract
    protected transient Class<?> daoCls;

    public AbstractStatic(final Environment environment) {
        super(environment);
    }

    protected JsonObject toCondition(final Pager pager, final JsonObject filters) {
        final JsonObject condition = new JsonObject();
        condition.put(Ir.KEY_PAGER, pager.toJson());
        condition.put(Ir.KEY_CRITERIA, filters);
        condition.put(Ir.KEY_SORTER, new JsonArray()
            .add(Sorter.create(KName.CREATED_AT, false).toJson()));
        return condition;
    }

    /*
     * 备份工具
     * 1 - stepT：直接调用 Jooq 执行备份（每一页10000条数据，并且压缩）
     *     格式：xxx.zero （压缩格式）
     * 2 - stepH：直接调用 mysqldump 执行备份
     *     格式：xxx.sql（标准Sql备份）
     */
    protected MigrateStep backupT(final Class<?> daoCls, final String folder) {
        return this.step(daoCls, folder, () -> new TableBackup(this.environment));
    }

    protected MigrateStep backupH(final Class<?> daoCls, final String folder) {
        return this.step(daoCls, folder, () -> new TableHugeBackup(this.environment));
    }

    protected MigrateStep restoreH(final Class<?> daoCls, final String folder) {
        return this.step(daoCls, folder, () -> new TableHugeRestore(this.environment));
    }

    protected MigrateStep restoreT(final Class<?> daoCls, final String folder) {
        return this.step(daoCls, folder, () -> new TableRestore(this.environment));
    }

    private MigrateStep step(final Class<?> daoCls, final String folder,
                             final Supplier<MigrateStep> supplier) {
        /*
         * 调用 pool() 处理
         */
        final UxJooq jooq = Ux.Jooq.on(daoCls, this.pool());
        final MigrateStep step = supplier.get().bind(this.app);
        Ut.contract(step, UxJooq.class, jooq);
        Ut.contract(step, Class.class, daoCls);
        Ut.contract(step, String.class, folder);
        return step;
    }

    @Override
    public String table() {
        return this.jooq.table();
    }

    @Override
    public DataPool pool() {
        return DataPool.create();
    }

    @Override
    public Class<?> dao() {
        return this.daoCls;
    }
}
