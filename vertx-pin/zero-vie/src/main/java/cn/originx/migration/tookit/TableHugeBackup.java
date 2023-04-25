package cn.originx.migration.tookit;

import cn.originx.refine.Ox;
import io.horizon.eon.em.Environment;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.config.Database;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;

public class TableHugeBackup extends AbstractStatic {

    public TableHugeBackup(final Environment environment) {
        super(environment);
    }

    @Override
    public Future<JsonObject> procAsync(final JsonObject config) {
        /*
         * 目录创建
         */
        final String targetFolder = this.ioBackup(config, "sql");
        /*
         * 文件名
         */
        final String file = targetFolder + "/" + this.jooq.table() + ".sql";
        final boolean done = this.backupTo(file, this.jooq.table());
        Ox.LOG.infoShell(this.getClass(), "备份数据位置：{0}，执行结果：{1}",
            file, done);
        return Ux.future(config);
    }

    private boolean backupTo(final String file, final String tableNames) {
        /*
         * mysqldump --opt
         * --host=localhost
         * --databases backup
         * --tables log_sys sys_user
         * --user=root
         * --password=root
         * --result-file=E:\Sqldata\test.sql
         * --default-character-set=utf8
         */
        final StringBuilder cmd = new StringBuilder();
        final Database database = Database.getCurrent();
        cmd.append("mysqldump").append(" --opt")
            .append(" --host=").append(database.getHostname())
            .append(" --databases ").append(database.getInstance())
            .append(" --tables ").append(tableNames)
            .append(" --user=").append(database.getUsername())
            .append(" --password=").append(database.getSmartPassword())
            .append(" --result-file=").append(file)
            .append(" --skip-comments")
            .append(" --default-character-set=utf8 ");
        return Fn.orJvm(() -> {
            Ox.LOG.infoShell(this.getClass(), "执行命令：{0}", cmd.toString());
            final Process process = Runtime.getRuntime().exec(cmd.toString());
            return process.waitFor() == 0;
        });
    }
}
