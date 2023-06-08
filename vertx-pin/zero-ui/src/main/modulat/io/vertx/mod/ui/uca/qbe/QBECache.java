package io.vertx.mod.ui.uca.qbe;

import cn.vertxup.ui.domain.tables.pojos.UiView;
import io.horizon.eon.VString;
import io.horizon.specification.action.HQR;
import io.horizon.uca.cache.Cc;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.ui.cv.UiCv;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.uca.cache.Rapid;
import io.vertx.up.unity.Ux;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/*
 * HQBE Cache 防止多次查询，此处查询比较频繁
 */
public class QBECache {
    public static final Cc<String, HQR> CCT_H_COND = Cc.openThread();
    private static final Rapid<String, UiView> RAPID = Rapid.t(UiCv.POOL_LIST_QR, 600); // 10 min

    public static Future<List<UiView>> cached(final List<UiView> listQr) {
        final List<Future<Boolean>> futures = new ArrayList<>();
        listQr.forEach(qr -> {
            // <sigma> / <code> / <name>
            final String key = qr.getSigma() + VString.SLASH +
                qr.getCode() + VString.SLASH +
                qr.getName();
            futures.add(RAPID.write(key, qr).compose(v -> Ux.futureT()));
        });
        return Fn.combineT(futures).compose(done -> Ux.future(listQr));
    }

    public static Future<UiView> cached(final JsonObject qr, final Supplier<Future<UiView>> executor) {
        final String key = qr.getString(KName.SIGMA) + VString.SLASH +
            qr.getString(KName.CODE) + VString.SLASH +
            qr.getString(KName.NAME);
        return RAPID.cached(key, executor);
    }
}
