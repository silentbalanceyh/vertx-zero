package cn.originx.migration.restore;

import cn.originx.migration.AbstractStep;
import cn.originx.migration.MigrateStep;
import io.horizon.eon.em.Environment;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;

public class RestorePrepare extends AbstractStep {
    private transient final MigrateStep cleaner;
    private transient final MigrateStep loader;
    private transient final MigrateStep finisher;

    public RestorePrepare(final Environment environment) {
        super(environment);
        this.cleaner = new MetaCleaner(environment);
        this.loader = new MetaLoader(environment);
        this.finisher = new MetaFinisher(environment);
    }

    @Override
    public Future<JsonObject> procAsync(final JsonObject config) {
        this.banner("002. 元数据重建");
        return Ux.future(config)
            /* 删除 DB_ORIGIN_X 中现存数据 */
            .compose(this.cleaner.bind(this.app)::procAsync)
            /* 初始化导入 DB_ORIGIN_X ：OxLoader 功能 */
            .compose(this.loader.bind(this.app)::procAsync)
            /* 初始化模型 Finisher: OxFinisher功能 */
            .compose(this.finisher.bind(this.app)::procAsync);
    }
}
