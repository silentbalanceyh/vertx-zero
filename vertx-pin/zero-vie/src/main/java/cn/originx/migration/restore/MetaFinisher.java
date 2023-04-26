package cn.originx.migration.restore;

import cn.originx.migration.AbstractStep;
import cn.originx.migration.MigrateStep;
import io.horizon.eon.em.Environment;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.refine.Ao;
import io.vertx.tp.ke.booter.Bt;
import io.vertx.up.unity.Ux;

import static cn.originx.refine.Ox.LOG;

public class MetaFinisher extends AbstractStep {
    private final transient MigrateStep report;
    private final transient MigrateStep limit;

    public MetaFinisher(final Environment environment) {
        super(environment);
        this.report = new MetaReport(environment);
        this.limit = new MetaLimit(environment);
    }

    @Override
    public Future<JsonObject> procAsync(final JsonObject config) {
        this.banner("002.3. 重新建模");
        /* XApp */
        return Bt.loadAsync(Ao.PATH.PATH_EXCEL + "schema/").compose(nil -> {
            LOG.Shell.info(this.getClass(), "建模数据已经成功导入到系统！Successfully");
            return Ux.future(config)
                /* Meta 专用报表 */
                .compose(this.report.bind(this.app)::procAsync)
                /* Meta 建模修正数据 */
                .compose(this.limit.bind(this.app)::procAsync);
        });
    }
}
