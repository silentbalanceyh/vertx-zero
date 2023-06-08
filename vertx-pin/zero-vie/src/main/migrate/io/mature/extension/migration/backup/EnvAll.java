package io.mature.extension.migration.backup;

import io.horizon.eon.em.Environment;
import io.mature.extension.migration.AbstractStep;
import io.mature.extension.migration.MigrateStep;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;

public class EnvAll extends AbstractStep {

    private final transient MigrateStep path;

    public EnvAll(final Environment environment) {
        super(environment);
        this.path = new EnvPath(environment);
    }

    @Override
    public Future<JsonObject> procAsync(final JsonObject config) {
        this.banner("001. 初始化环境");
        return Ux.future(config)
            /* 路径处理 */
            .compose(this.path.bind(this.ark)::procAsync);
    }
}
