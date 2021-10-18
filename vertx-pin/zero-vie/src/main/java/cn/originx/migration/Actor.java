package cn.originx.migration;

import cn.originx.migration.backup.BackupAll;
import cn.originx.migration.backup.DDLExecution;
import cn.originx.migration.backup.EnvAll;
import cn.originx.migration.backup.ReportAll;
import cn.originx.migration.restore.RestoreAll;
import cn.originx.migration.restore.RestorePrepare;
import io.vertx.up.eon.em.Environment;

public class Actor {

    public static MigrateStep environment(final Environment environment) {
        return new EnvAll(environment);
    }

    /*
     * Backup 专用
     */
    static MigrateStep report(final Environment environment) {
        return new ReportAll(environment);
    }

    static MigrateStep backup(final Environment environment) {
        return new BackupAll(environment);
    }

    static MigrateStep ddl(final Environment environment) {
        return new DDLExecution(environment);
    }

    /*
     * Restore 专用
     */
    static MigrateStep prepare(final Environment environment) {
        return new RestorePrepare(environment);
    }

    static MigrateStep restore(final Environment environment) {
        return new RestoreAll(environment);
    }
}
