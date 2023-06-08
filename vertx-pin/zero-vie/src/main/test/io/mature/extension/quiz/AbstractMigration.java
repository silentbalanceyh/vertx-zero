package io.mature.extension.quiz;

import io.horizon.eon.em.Environment;
import io.mature.extension.error._501EnvironmentException;
import io.mature.extension.migration.Around;
import io.mature.extension.migration.Migrate;
import io.mature.extension.migration.MigrateService;
import io.mature.extension.migration.MigrateStep;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
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
        return step.bind(this.ark()).procAsync(params);
    }

    // AOP组件测试
    protected Future<JsonObject> tcAop(final String componentKey, final JsonObject params) {
        return Around.create(this.environment).bind(this.ark()).aspectAsync(params, componentKey);
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
        return migrate.bind(this.environment).bind(this.ark());
    }

    private MigrateStep migrate(final Class<?> clazz) {
        final MigrateStep migrate = Ut.instance(clazz, this.environment);
        return migrate.bind(this.ark());
    }

}
