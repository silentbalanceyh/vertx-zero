package io.vertx.tp.crud.init;

import io.vertx.core.MultiMap;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.tp.ke.atom.metadata.KModule;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.up.atom.Rule;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.jooq.UxJooq;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/*
 * Init Plugin for `init` static life
 */
public class IxPin {

    private static final Annal LOGGER = Annal.get(IxPin.class);

    public static void init() {
        Ke.banner("「Εκδήλωση」- Crud ( Ix )");

        Ix.infoInit(LOGGER, "IxConfiguration...");
        /* Configuration Init */
        IxConfiguration.init();

        Ix.infoInit(LOGGER, "IxDao...");
        /* Dao Init */
        IxDao.init();

        Ix.infoInit(LOGGER, "IxValidator...");
        /* Validator Init */
        IxValidator.init();
    }

    public static KModule getActor(final String actor) {
        return IxDao.get(actor);
    }

    public static UxJooq getDao(final KModule config, final MultiMap headers) {
        return IxDao.get(config, headers);
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
