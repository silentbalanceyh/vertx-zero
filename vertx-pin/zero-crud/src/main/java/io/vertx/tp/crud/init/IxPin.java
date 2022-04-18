package io.vertx.tp.crud.init;

import io.vertx.tp.crud.refine.Ix;
import io.vertx.tp.crud.uca.desk.IxMod;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.up.atom.Rule;
import io.vertx.up.commune.Envelop;
import io.vertx.up.experiment.specification.KModule;
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

    public static UxJooq jooq(final IxMod in) {
        final Envelop envelop = in.envelop();
        return IxDao.get(in.module(), envelop.headers());
    }

    public static UxJooq jooq(final KModule module, final Envelop envelop) {
        return IxDao.get(module, envelop.headers());
    }

    public static UxJoin join(final IxMod in, final KModule connect) {
        return IxDao.get(in.module(), connect);
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
