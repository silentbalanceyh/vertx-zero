package cn.originx.migration.backup;

import cn.originx.migration.AbstractStep;
import cn.originx.migration.MigrateStep;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.horizon.eon.em.Environment;
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
            .compose(this.path.bind(this.app)::procAsync);
    }
}
