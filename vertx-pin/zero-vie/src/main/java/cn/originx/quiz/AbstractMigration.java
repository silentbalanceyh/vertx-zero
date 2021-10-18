package cn.originx.quiz;

import cn.originx.migration.Around;
import cn.originx.migration.Migrate;
import cn.originx.migration.MigrateService;
import cn.originx.migration.MigrateStep;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.error._501EnvironmentException;
import io.vertx.up.eon.em.Environment;
import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractMigration extends AbstractPlatform {
    public AbstractMigration(final Environment environment) {
        super(environment);
        if (Environment.Mockito == environment) {
            this.logger().error("[ Qz ] Migration do not support `Mockito` environment. ");
            throw new _501EnvironmentException(this.getClass());
        }
    }

    // 步骤测试
    protected Future<JsonObject> tcStep(final Class<?> stepCls, final JsonObject params) {
        final MigrateStep step = this.migrate(stepCls);
        return step.bind(this.app()).procAsync(params);
    }

    // AOP组件测试
    protected Future<JsonObject> tcAop(final String componentKey, final JsonObject params) {
        return Around.create(this.environment).bind(this.app()).aspectAsync(params, componentKey);
    }

    // 还原测试
    protected Future<JsonObject> tcRestore(final String output) {
        return this.tcRestore(new JsonObject().put("output", output));
    }

    protected Future<JsonObject> tcRestore(final JsonObject params) {
        final Migrate migrate = this.migrate();
        return migrate.restoreAsync(params);
    }

    // 备份测试
    protected Future<JsonObject> tcBackup(final String output) {
        return this.tcBackup(new JsonObject().put("output", output));
    }

    protected Future<JsonObject> tcBackup(final JsonObject params) {
        final Migrate migrate = this.migrate();
        return migrate.backupAsync(params);
    }

    private Migrate migrate() {
        final Migrate migrate = Ut.singleton(MigrateService.class);
        return migrate.bind(this.environment).bind(this.app());
    }

    private MigrateStep migrate(final Class<?> clazz) {
        final MigrateStep migrate = Ut.instance(clazz, this.environment);
        return migrate.bind(this.app());
    }

}
