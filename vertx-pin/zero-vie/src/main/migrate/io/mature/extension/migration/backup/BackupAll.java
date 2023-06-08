package io.mature.extension.migration.backup;

import io.horizon.eon.em.Environment;
import io.mature.extension.migration.AbstractStep;
import io.mature.extension.migration.MigrateStep;
import io.mature.extension.migration.restore.AdjustNumber;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;

public class BackupAll extends AbstractStep {
    private transient final MigrateStep organize;
    private transient final MigrateStep user;
    private transient final MigrateStep system;
    private transient final MigrateStep adjuster;

    public BackupAll(final Environment environment) {
        super(environment);
        this.organize = new BackupOrg(environment);
        this.user = new BackupUser(environment);
        this.system = new BackupSystem(environment);
        // this.history = new BackupHistory(environment);
        this.adjuster = new AdjustNumber(environment);
    }

    @Override
    public Future<JsonObject> procAsync(final JsonObject config) {
        /*
         * 绑定处理
         */

        this.banner("003.1. 开始备份");
        /*
         * 扩展备份插件（暂时不扩展）
         */
        return Ux.future(config)
            /* Before */
            .compose(processed -> this.aspectAsync(processed, "before-backup"))
            /* 先修复 number */
            .compose(this.adjuster.bind(this.ark)::procAsync)
            /* 组织架构备份 */
            .compose(this.organize.bind(this.ark)::procAsync)
            /* 账号体系备份 */
            .compose(this.user.bind(this.ark)::procAsync)
            /* 系统数据备份 */
            .compose(this.system.bind(this.ark)::procAsync)
            /* 历史数据备份 */
            // .compose(this.history.bind(this.app)::procAsync)
            /* After */
            .compose(processed -> this.aspectAsync(processed, "after-backup"));
    }
}
