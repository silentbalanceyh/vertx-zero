package io.vertx.tp.ui.uca.qbe;

import cn.vertxup.ui.domain.tables.pojos.UiListQr;
import io.vertx.aeon.specification.query.HCond;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ui.cv.UiCv;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Strings;
import io.vertx.up.fn.Fn;
import io.vertx.up.uca.cache.Cc;
import io.vertx.up.uca.cache.Rapid;
import io.vertx.up.unity.Ux;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/*
 * HQBE Cache 防止多次查询，此处查询比较频繁
 */
public class QBECache {
    public static final Cc<String, HCond> CCT_H_COND = Cc.openThread();
    private static final Rapid<String, UiListQr> RAPID = Rapid.t(UiCv.POOL_LIST_QR, 600);

    public static Future<List<UiListQr>> cached(final List<UiListQr> listQr) {
        final List<Future<Boolean>> futures = new ArrayList<>();
        listQr.forEach(qr -> {
            // <sigma> / <code> / <name>
            final String key = qr.getSigma() + Strings.SLASH +
                    qr.getCode() + Strings.SLASH +
                    qr.getName();
            futures.add(RAPID.write(key, qr).compose(v -> Ux.futureT()));
        });
        return Fn.combineT(futures).compose(done -> Ux.future(listQr));
    }

    public static Future<UiListQr> cached(final JsonObject qr, final Supplier<Future<UiListQr>> executor) {
        final String key = qr.getString(KName.SIGMA) + Strings.SLASH +
                qr.getString(KName.CODE) + Strings.SLASH +
                qr.getString(KName.NAME);
        return RAPID.cached(key, executor);
    }
}
