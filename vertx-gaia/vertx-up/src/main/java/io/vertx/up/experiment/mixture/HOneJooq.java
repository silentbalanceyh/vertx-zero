package io.vertx.up.experiment.mixture;

import io.vertx.core.MultiMap;
import io.vertx.tp.plugin.database.DS;
import io.vertx.up.eon.em.DSMode;
import io.vertx.up.experiment.specification.KModule;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class HOneJooq implements HOne<UxJooq> {

    @Override
    public UxJooq combine(final KModule module, final KModule connect, final MultiMap headers) {
        Objects.requireNonNull(module);
        Objects.requireNonNull(headers);
        /*
         * This is single One UxJooq Extracting ( It does not support join )
         * In this kind of situation, it will convert configuration to MultiMap of headers
         * 1) May call XHeader combine
         * 2) May call Json combine
         */
        final Class<?> daoCls = module.getDaoCls();
        assert Objects.nonNull(daoCls) : "The Dao class must not be null, check configuration";


        // ================ Build UxJooq Object ===================
        final UxJooq dao;
        // 1. Extract Mode from 'IxModule' for data source switching
        final DSMode mode = module.getMode();
        if (DSMode.DYNAMIC == mode) {
            dao = Ux.channelS(DS.class,
                /* `provider` configured */
                () -> Ux.Jooq.on(daoCls),
                /* Dynamic Data Source Here */
                ds -> Ux.Jooq.on(daoCls, ds.switchDs(headers))
            );
        } else {
            if (DSMode.HISTORY == mode) {
                /* `orbit` configured */
                dao = Ux.Jooq.ons(daoCls);
            } else if (DSMode.EXTENSION == mode) {
                final String modeKey = module.getModeKey();
                if (Ut.isNil(modeKey)) {
                    /* `provider` configured */
                    dao = Ux.Jooq.on(daoCls);
                } else {
                    /* `<key>` configured */
                    dao = Ux.Jooq.on(daoCls, modeKey);
                }
            } else {
                /* `provider` configured */
                dao = Ux.Jooq.on(daoCls);
            }
        }

        // =========== Where existing pojo.yml =================
        // ( Zero support yml file to define mapping )
        final String pojo = module.getPojo();
        if (Ut.notNil(pojo)) {
            dao.on(pojo);
        }
        return dao;
    }
}
