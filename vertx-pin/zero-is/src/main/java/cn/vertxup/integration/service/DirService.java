package cn.vertxup.integration.service;

import cn.vertxup.integration.domain.tables.daos.IDirectoryDao;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.is.uca.FsHelper;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class DirService implements DirStub {
    @Override
    public Future<JsonObject> create(final JsonObject directoryJ) {
        return Ux.Jooq.on(IDirectoryDao.class).insertJAsync(directoryJ)
            .compose(json -> FsHelper.componentRun(json, fs -> fs.mkdir(json)));
    }
}
