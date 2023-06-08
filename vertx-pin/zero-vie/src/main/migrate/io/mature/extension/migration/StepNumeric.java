package io.mature.extension.migration;

import io.horizon.eon.em.Environment;
import io.mature.extension.migration.backup.ReportNumber;
import io.mature.extension.migration.restore.AdjustNumber;
import io.mature.extension.refine.Ox;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;

/*
 * 单命令专用类
 * 1. 查询序号的报表
 * 2. 修复所有序号问题
 */
public class StepNumeric extends AbstractStep {

    private final transient MigrateStep before;
    private final transient MigrateStep after;

    public StepNumeric(final Environment environment) {
        super(environment);
        this.before = new ReportNumber(environment);
        this.after = new AdjustNumber(environment);
    }

    @Override
    public Future<JsonObject> procAsync(final JsonObject config) {

        Ox.LOG.Shell.info(this.getClass(), "执行 Number 还原");
        return Ux.future(config)
            /* 001 - 容器环境初始化 */
            .compose(Actor.environment(this.environment).bind(this.ark)::procAsync)
            /* 出 Number 报表 */
            .compose(this.before.bind(this.ark)::procAsync)
            /* 执行 Number 修复 */
            .compose(this.after.bind(this.ark)::procAsync);
    }
}
