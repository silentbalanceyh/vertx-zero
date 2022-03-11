package cn.vertxup.integration.service;

import cn.vertxup.integration.domain.tables.daos.IDirectoryDao;
import cn.vertxup.integration.domain.tables.pojos.IDirectory;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.is.uca.FsHelper;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class DirService implements DirStub {
    @Override
    public Future<JsonObject> create(final JsonObject directoryJ) {
        return FsHelper.componentRun(directoryJ, fs -> {
            // Cannot deserialize value of type `java.lang.String` from Array value (token `JsonToken.START_ARRAY`)
            Ut.ifString(directoryJ,
                KName.METADATA,
                KName.VISIT_GROUP,
                KName.VISIT_ROLE,
                KName.VISIT_MODE
            );
            final IDirectory directory = fs.initialize(directoryJ);
            return Ux.Jooq.on(IDirectoryDao.class).insertJAsync(directory)
                .compose(Ut.ifJObject(
                    KName.METADATA,
                    KName.VISIT_GROUP,
                    KName.VISIT_ROLE,
                    KName.VISIT_MODE
                ))
                .compose(fs::mkdir);
        });
    }
}
