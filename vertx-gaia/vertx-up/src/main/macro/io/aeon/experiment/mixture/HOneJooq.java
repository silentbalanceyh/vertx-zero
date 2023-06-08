package io.aeon.experiment.mixture;

import io.aeon.experiment.specification.KModule;
import io.horizon.eon.em.EmDS;
import io.vertx.core.MultiMap;
import io.vertx.up.plugin.database.DS;
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
        final EmDS.Stored mode = module.getMode();
        if (EmDS.Stored.DYNAMIC == mode) {
            dao = Ux.channelS(DS.class,
                /* `provider` configured */
                () -> Ux.Jooq.on(daoCls),
                /* Dynamic Data Source Here */
                ds -> Ux.Jooq.on(daoCls, ds.switchDs(headers))
            );
        } else {
            if (EmDS.Stored.HISTORY == mode) {
                /* `orbit` configured */
                dao = Ux.Jooq.ons(daoCls);
            } else if (EmDS.Stored.EXTENSION == mode) {
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
        if (Ut.isNotNil(pojo)) {
            dao.on(pojo);
        }
        return dao;
    }
}
