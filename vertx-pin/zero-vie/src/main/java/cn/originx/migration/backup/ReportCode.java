package cn.originx.migration.backup;

import cn.originx.migration.AbstractStep;
import cn.originx.refine.Ox;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.em.Environment;
import io.aeon.experiment.mixture.HDao;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

public class ReportCode extends AbstractStep {

    public ReportCode(final Environment environment) {
        super(environment);
    }

    /*
     * 如果没有 code 那么该配置项在系统中属于不合法的
     * 1. 检查没有 code 的配置项数据清单
     * 2. 可修正这份数据生成新的 code
     * 3. 前提条件是 ReportNumber 已经修复完成
     */
    @Override
    public Future<JsonObject> procAsync(final JsonObject config) {
        this.banner("002.3. 生成 Code 报表");
        return Ux.future(config).compose(nil -> {
            /*
             * 防止 Database 交换
             */
            final HDao dao = this.ioDao("ci.device");

            final JsonObject filters = new JsonObject();
            filters.put(KName.CODE + ",n", Boolean.TRUE);
            return dao.fetchAsync(filters).compose(Ux::futureA);
        }).compose(ready -> {
            Ox.Log.infoShell(this.getClass(), "非法数据数量：{0}", String.valueOf(ready.size()));
            this.report(ready);
            /*
             * 处理结构
             */
            final String folder = this.ioRoot(config);
            final String file = folder + "report/cis/no-code.json";
            return this.writeAsync(ready, file).compose(nil -> Ux.future(config));
        });
    }

    private void report(final JsonArray invalidData) {
        if (Ut.notNil(invalidData)) {
            final StringBuilder content = new StringBuilder();
            final String width = "\t\t\t\t\t\t";
            content.append(Ut.fromAdjust("配置项名称", 45)).append(width);
            content.append(Ut.fromAdjust("UCMDB ID", 15)).append(width);
            content.append(Ut.fromAdjust("是否确认", 12)).append(width);
            content.append(Ut.fromAdjust("工作流", 12)).append("\n");
            Ut.itJArray(invalidData).forEach(item -> {
                content.append(Ut.fromAdjust(item.getString(KName.NAME), 50)).append(width);
                content.append(item.getString(KName.GLOBAL_ID)).append(width);
                content.append(item.getString("confirmStatus")).append(width);
                content.append(item.getString("lifecycle")).append("\n");
            });
            Ox.Log.infoShell(this.getClass(), "完整报表：\n{0}", content.toString());
        }
    }
}
