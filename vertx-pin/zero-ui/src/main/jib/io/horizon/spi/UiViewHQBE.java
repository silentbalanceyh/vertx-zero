package io.horizon.spi;

import cn.vertxup.ui.domain.tables.daos.UiViewDao;
import cn.vertxup.ui.domain.tables.pojos.UiView;
import io.horizon.specification.action.HQR;
import io.horizon.spi.cloud.query.HQBE;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.ui.uca.qbe.QBECache;
import io.vertx.up.commune.Envelop;
import io.vertx.up.commune.config.XHeader;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class UiViewHQBE implements HQBE {


    @Override
    public Future<Envelop> before(final JsonObject qbeJ, final Envelop envelop) {
        final XHeader header = new XHeader();
        header.fromHeader(envelop.headers());
        // SIGMA / CODE / NAME
        final JsonObject condition = qbeJ.copy();
        condition.put(KName.SIGMA, header.getSigma());
        // From cached to fetch the list qr object ( reference )
        return QBECache.cached(condition, () -> Ux.Jooq.on(UiViewDao.class).fetchOneAsync(condition))
            // Processing the `criteria` modification
            .compose(listQr -> this.before(listQr, envelop));
    }

    private Future<Envelop> before(final UiView listQr, final Envelop envelop) {
        final Class<?> qrComponent = Ut.clazz(listQr.getQrComponent(), null);
        final Future<JsonObject> future;
        final JsonObject request = this.beforeArgs(envelop);
        if (Objects.isNull(qrComponent) || !Ut.isImplement(qrComponent, HQR.class)) {
            future = this.beforeInternal(listQr, request);
        } else {
            future = this.beforeInternal(listQr, request, qrComponent);
        }
        return future.compose(criteria -> {
            envelop.onH(criteria);
            return Ux.future(envelop);
        });
    }

    private JsonObject beforeArgs(final Envelop envelop) {
        final JsonObject args = new JsonObject();
        /*
         * appId
         * appKey
         * sigma
         * language
         * tenantId
         */
        args.mergeIn(envelop.headersX(), true);
        /*
         * user
         * role for future usage
         */
        args.put(KName.USER, Ux.keyUser(envelop.user()));
        return args;
    }

    // criteria field only
    private Future<JsonObject> beforeInternal(final UiView listQr, final JsonObject request) {
        final JsonObject criteriaJ = Ut.toJObject(listQr.getCriteria());
        return Ux.future(Ut.fromExpression(criteriaJ, request));
    }

    // qrComponent extension
    private Future<JsonObject> beforeInternal(final UiView listQr, final JsonObject request, final Class<?> qrComponent) {
        return this.beforeInternal(listQr, request).compose(configured -> {
            final HQR cond = QBECache.CCT_H_COND.pick(() -> Ut.instance(qrComponent));
            final JsonObject qrConfig = Ut.toJObject(listQr.getQrConfig());
            return cond.compile(request, qrConfig);
        });
    }
}
