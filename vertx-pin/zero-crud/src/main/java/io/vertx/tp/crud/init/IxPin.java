package io.vertx.tp.crud.init;

import io.vertx.core.MultiMap;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.tp.crud.uca.desk.IxIn;
import io.vertx.tp.ke.atom.KModule;
import io.vertx.tp.ke.atom.connect.KPoint;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.up.atom.Rule;
import io.vertx.up.commune.Envelop;
import io.vertx.up.uca.jooq.UxJoin;
import io.vertx.up.uca.jooq.UxJooq;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/*
 * Init Plugin for `init` static life
 */
public class IxPin {

    public static void init() {
        Ke.banner("「Εκδήλωση」- Crud ( Ix )");

        Ix.Log.init(IxPin.class, "IxConfiguration...");
        /* Configuration Init */
        IxConfiguration.init();

        Ix.Log.init(IxPin.class, "IxDao...");
        /* Dao Init */
        IxDao.init();

        Ix.Log.init(IxPin.class, "IxValidator...");
        /* Validator Init */
        IxValidator.init();
    }

    public static KModule getActor(final String actor) {
        return IxDao.get(actor);
    }

    @Deprecated
    public static UxJooq getDao(final KModule config, final MultiMap headers) {
        return IxDao.get(config, headers);
    }

    public static UxJooq jooq(final IxIn in) {
        final Envelop envelop = in.envelop();
        return IxDao.get(in.module(), envelop.headers());
    }

    public static UxJoin join(final IxIn in, final KModule connect) {
        return IxDao.get(in.module(), connect);
    }

    public static KPoint point(final IxIn in) {
        return IxDao.getPoint(in);
    }

    public static Set<String> getUris() {
        return IxConfiguration.getUris();
    }

    public static ConcurrentMap<String, List<Rule>> getRules(final String actor) {
        return IxValidator.getRules(actor);
    }

    public static String getColumnKey() {
        return IxConfiguration.getField();
    }

    public static String getColumnLabel() {
        return IxConfiguration.getLabel();
    }
}
