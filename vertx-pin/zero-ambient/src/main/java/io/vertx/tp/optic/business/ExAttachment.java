package io.vertx.tp.optic.business;

import cn.vertxup.ambient.domain.tables.daos.XAttachmentDao;
import cn.vertxup.ambient.domain.tables.pojos.XAttachment;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.List;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class ExAttachment implements ExFile {
    @Override
    public Future<JsonArray> createAsync(final JsonArray data) {
        Ut.ifStrings(data, KName.METADATA);
        final List<XAttachment> attachments = Ux.fromJson(data, XAttachment.class);
        return Ux.Jooq.on(XAttachmentDao.class).insertJAsync(attachments);
    }

    @Override
    public Future<JsonArray> saveAsync(final JsonObject condition, final JsonArray data) {
        Ut.ifStrings(data, KName.METADATA);
        final List<XAttachment> attachments = Ux.fromJson(data, XAttachment.class);
        final UxJooq jq = Ux.Jooq.on(XAttachmentDao.class);
        return jq.deleteByAsync(condition).compose(nil -> jq.insertJAsync(attachments));
    }

    @Override
    public Future<JsonArray> fetchAsync(final JsonObject condition) {
        return Ux.Jooq.on(XAttachmentDao.class).fetchJAsync(condition)
            .compose(Ut.ifJArray(KName.METADATA));
    }
}
