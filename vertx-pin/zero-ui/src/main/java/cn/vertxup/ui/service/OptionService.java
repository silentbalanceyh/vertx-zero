package cn.vertxup.ui.service;

import cn.vertxup.ui.domain.tables.daos.VFragmentDao;
import cn.vertxup.ui.domain.tables.daos.VQueryDao;
import cn.vertxup.ui.domain.tables.daos.VSearchDao;
import cn.vertxup.ui.domain.tables.daos.VTableDao;
import cn.vertxup.ui.domain.tables.pojos.VFragment;
import cn.vertxup.ui.domain.tables.pojos.VQuery;
import cn.vertxup.ui.domain.tables.pojos.VSearch;
import cn.vertxup.ui.domain.tables.pojos.VTable;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.up.unity.Ux;

public class OptionService implements OptionStub {
    @Override
    public Future<JsonObject> fetchQuery(final String id) {
        return Ux.Jooq.on(VQueryDao.class)
                .<VQuery>findByIdAsync(id)
                .compose(Ux::fnJObject)
                .compose(Ke.mount(FIELD_QUERY_CRITERIA))
                .compose(Ke.mountArray(FIELD_QUERY_PROJECTION));
    }

    @Override
    public Future<JsonObject> fetchSearch(final String id) {
        return Ux.Jooq.on(VSearchDao.class)
                .<VSearch>findByIdAsync(id)
                .compose(Ux::fnJObject)
                .compose(Ke.mount(FIELD_SEARCH_NOTICE))
                .compose(Ke.mountArray(FIELD_SEARCH_COND));
    }

    @Override
    public Future<JsonObject> fetchFragment(final String id) {
        return Ux.Jooq.on(VFragmentDao.class)
                .<VFragment>findByIdAsync(id)
                .compose(Ux::fnJObject)
                .compose(Ke.mount(FIELD_FRAGMENT_MODEL))
                .compose(Ke.mount(FIELD_FRAGMENT_NOTICE))
                .compose(Ke.mount(FIELD_FRAGMENT_CONFIG))
                .compose(Ke.mountArray(FIELD_FRAGMENT_BUTTON_GROUP));
    }

    @Override
    public Future<JsonObject> fetchTable(final String id) {
        return Ux.Jooq.on(VTableDao.class)
                .<VTable>findByIdAsync(id)
                .compose(Ux::fnJObject)
                .compose(Ke.mountArray(FIELD_TABLE_OP_CONFIG));
    }
}
