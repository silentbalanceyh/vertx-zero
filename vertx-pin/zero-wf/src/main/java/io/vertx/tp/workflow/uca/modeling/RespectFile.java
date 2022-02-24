package io.vertx.tp.workflow.uca.modeling;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.WRecord;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class RespectFile extends AbstractRespect {

    private transient final String CLS_DAO = "cn.vertxup.ambient.domain.tables.daos.XAttachmentDao";

    public RespectFile(final JsonObject query) {
        super(query);
    }

    @Override
    public Future<JsonArray> syncAsync(final JsonArray data, final JsonObject params, final WRecord record) {
        return Ux.futureA();
    }

    @Override
    public Future<JsonArray> fetchAsync(final WRecord record) {
        return Ux.futureA();
    }
}
