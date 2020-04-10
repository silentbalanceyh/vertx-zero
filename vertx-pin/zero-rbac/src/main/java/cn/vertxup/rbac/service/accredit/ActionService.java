package cn.vertxup.rbac.service.accredit;

import cn.vertxup.rbac.domain.tables.daos.SActionDao;
import cn.vertxup.rbac.domain.tables.daos.SResourceDao;
import cn.vertxup.rbac.domain.tables.pojos.SAction;
import cn.vertxup.rbac.domain.tables.pojos.SResource;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.cv.KeField;
import io.vertx.up.unity.Ux;
import io.vertx.up.eon.Strings;
import io.vertx.up.util.Ut;

public class ActionService implements ActionStub {

    @Override
    public Future<SAction> fetchAction(final String normalizedUri,
                                       final HttpMethod method) {
        return fetchAction(normalizedUri, method, null);
    }

    @Override
    public Future<SAction> fetchAction(final String normalizedUri,
                                       final HttpMethod method,
                                       final String sigma) {
        final JsonObject actionFilters = new JsonObject();
        actionFilters.put(Strings.EMPTY, Boolean.TRUE);
        actionFilters.put(KeField.URI, normalizedUri);
        if (Ut.notNil(sigma)) {
            actionFilters.put(KeField.SIGMA, sigma);
        }
        actionFilters.put(KeField.METHOD, method.name());
        return Ux.Jooq.on(SActionDao.class)
                .fetchOneAsync(actionFilters);
    }

    @Override
    public Future<SResource> fetchResource(final String key) {
        return Ux.Jooq.on(SResourceDao.class)
                .findByIdAsync(key);
    }
}
