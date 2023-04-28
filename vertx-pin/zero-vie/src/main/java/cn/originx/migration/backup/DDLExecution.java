package cn.originx.migration.backup;

import cn.originx.migration.AbstractStep;
import io.horizon.eon.em.Environment;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.database.DataPool;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Objects;

import static cn.originx.refine.Ox.LOG;

public class DDLExecution extends AbstractStep {

    public DDLExecution(final Environment environment) {
        super(environment);
    }

    @Override
    public Future<JsonObject> procAsync(final JsonObject config) {
        this.banner("002.3. 执行 DDL 报表");
        /*
         * 执行 DDL 语句（每次需要单独配置）
         */
        final String root = this.ioRoot(config);
        final String ddl = root + "report/ddl/data.json";
        /*
         * 是否锁定
         */
        final String lockPath = root + "report/ddl/lock";
        final File lock = Ut.ioFile(lockPath);
        if (Objects.nonNull(lock)) {
            LOG.Shell.info(this.getClass(), "DDL只能执行一次！");
        } else {
            final File file = Ut.ioFile(ddl);
            if (Objects.nonNull(file)) {
                final JsonObject data = Ut.ioJObject(ddl);
                final JsonArray statements = Ut.valueJArray(data.getJsonArray("sql"));
                if (Ut.isNotNil(statements)) {
                    /*
                     * 执行 DDL 语句
                     */
                    Fn.jvmAt(() -> {
                        final DataPool pool = DataPool.create();
                        final Connection connection = pool.getDataSource().getConnection();
                        Ut.itJArray(statements, String.class, (item, index) -> {
                            if (Ut.isNotNil(item)) {
                                Fn.jvmAt(() -> {
                                    final PreparedStatement stmt = connection.prepareStatement(item);
                                    final boolean result = stmt.execute();
                                    LOG.Shell.info(this.getClass(), "执行语句：{0}，结果：{1}", item, result);
                                });
                            }
                        });
                    });
                }
            }

            final boolean locked = Fn.failOr(() -> {
                final File created = new File(lockPath);
                return created.createNewFile();
            });
            if (locked) {
                LOG.Shell.info(this.getClass(), "当前执行器 DDLExecution 完成，锁定结果：{0}", locked);
            }
        }
        return Ux.future(config);
    }
}
