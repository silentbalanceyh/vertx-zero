package io.mature.extension.migration;

import io.horizon.eon.em.Environment;
import io.mature.extension.migration.backup.BackupAll;
import io.mature.extension.migration.backup.DDLExecution;
import io.mature.extension.migration.backup.EnvAll;
import io.mature.extension.migration.backup.ReportAll;
import io.mature.extension.migration.restore.RestoreAll;
import io.mature.extension.migration.restore.RestorePrepare;

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
