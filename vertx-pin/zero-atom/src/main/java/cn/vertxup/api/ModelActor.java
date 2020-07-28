package cn.vertxup.api;

import cn.vertxup.atom.domain.tables.daos.MModelDao;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.tp.atom.cv.Addr;
import io.vertx.tp.ke.cv.KeField;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.commune.config.XHeader;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
@Queue
public class ModelActor {

    @Address(Addr.Model.MODEL_FIELDS)
    public Future<JsonArray> fetchAttrs(final String identifier, final XHeader header) {
        return null;
    }

    @Address(Addr.Model.MODELS)
    public Future<JsonArray> fetchModels(final String sigma) {
        return Ux.Jooq.on(MModelDao.class)
                .fetchAsync(KeField.SIGMA, sigma)
                .compose(Ux::fnJArray)
                .compose(array -> {
                    Ut.itJArray(array).forEach(each -> {
                        Ke.mount(each, KeField.RULE_UNIQUE);
                        Ke.mount(each, KeField.METADATA);
                    });
                    return Ux.future(array);
                });
    }
}
