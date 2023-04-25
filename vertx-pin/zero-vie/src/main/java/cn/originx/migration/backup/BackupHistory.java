package cn.originx.migration.backup;

import cn.originx.migration.tookit.AbstractStatic;
import cn.vertxup.ambient.domain.tables.daos.XActivityChangeDao;
import cn.vertxup.ambient.domain.tables.daos.XActivityDao;
import cn.vertxup.ambient.domain.tables.daos.XLogDao;
import cn.vertxup.workflow.domain.tables.daos.WTodoDao;
import io.horizon.eon.em.Environment;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;

public class BackupHistory extends AbstractStatic {

    public BackupHistory(final Environment environment) {
        super(environment);
    }

    @Override
    public Future<JsonObject> procAsync(final JsonObject config) {
        this.banner("003.14. 备份历史数据");
        final String folder = "history";
        return Ux.future(config)
            /* XLog */
            .compose(this.backupH(XLogDao.class, folder)::procAsync)
            /* XActivityChange */
            .compose(this.backupH(XActivityChangeDao.class, folder)::procAsync)
            /* XActivity */
            .compose(this.backupH(XActivityDao.class, folder)::procAsync)
            /* WTodo */
            .compose(this.backupT(WTodoDao.class, folder)::procAsync);
    }
}
