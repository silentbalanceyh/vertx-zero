package cn.originx.migration.restore;

import cn.originx.migration.tookit.AbstractStatic;
import cn.vertxup.ambient.domain.tables.daos.XActivityChangeDao;
import cn.vertxup.ambient.domain.tables.daos.XActivityDao;
import cn.vertxup.ambient.domain.tables.daos.XLogDao;
import cn.vertxup.workflow.domain.tables.daos.WTodoDao;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.horizon.eon.em.Environment;
import io.vertx.up.unity.Ux;

public class RestoreHistory extends AbstractStatic {

    public RestoreHistory(final Environment environment) {
        super(environment);
    }

    @Override
    public Future<JsonObject> procAsync(final JsonObject config) {
        this.banner("003.4 大表数据直接还原");
        final String folder = "history";
        return Ux.future(config)
            /* XActivityChange */
            .compose(this.restoreH(XActivityChangeDao.class, folder)::procAsync)
            /* XActivity */
            .compose(this.restoreH(XActivityDao.class, folder)::procAsync)
            /* XLog */
            .compose(this.restoreH(XLogDao.class, folder)::procAsync)
            /* WTodo */
            .compose(this.restoreT(WTodoDao.class, folder)::procAsync);
    }
}
