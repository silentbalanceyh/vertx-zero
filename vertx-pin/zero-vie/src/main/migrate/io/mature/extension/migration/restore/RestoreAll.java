package io.mature.extension.migration.restore;

import io.horizon.eon.em.Environment;
import io.mature.extension.migration.AbstractStep;
import io.mature.extension.migration.MigrateStep;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;

public class RestoreAll extends AbstractStep {
    private transient final MigrateStep history;
    private transient final MigrateStep organize;
    private transient final MigrateStep user;
    private transient final MigrateStep system;
    private transient final MigrateStep adjuster;

    public RestoreAll(final Environment environment) {
        super(environment);
        this.history = new RestoreHistory(environment);
        this.organize = new RestoreOrg(environment);
        this.user = new RestoreUser(environment);
        this.system = new RestoreSystem(environment);
        this.adjuster = new AdjustNumber(environment);
    }

    @Override
    public Future<JsonObject> procAsync(final JsonObject config) {
        this.banner("003. 数据还原");
        return Ux.future(config)
            /* Before */
            .compose(processed -> this.aspectAsync(processed, "before-restore"))
            /* 组织架构还原 */
            .compose(this.organize.bind(this.ark)::procAsync)
            /* 账号体系还原 */
            .compose(this.user.bind(this.ark)::procAsync)
            /* 系统数据还原 */
            .compose(this.system.bind(this.ark)::procAsync)
            /* 历史数据还原 */
            .compose(this.history.bind(this.ark)::procAsync)
            /* 后修复 number （双重保证） */
            .compose(this.adjuster.bind(this.ark)::procAsync)
            /* After */
            .compose(processed -> this.aspectAsync(processed, "after-restore"));
    }
}
