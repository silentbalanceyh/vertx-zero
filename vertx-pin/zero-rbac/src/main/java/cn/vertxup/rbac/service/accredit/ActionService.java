package cn.vertxup.rbac.service.accredit;

import cn.vertxup.rbac.domain.tables.daos.SActionDao;
import cn.vertxup.rbac.domain.tables.daos.SResourceDao;
import cn.vertxup.rbac.domain.tables.pojos.SAction;
import cn.vertxup.rbac.domain.tables.pojos.SResource;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.cv.KeField;
import io.vertx.up.eon.Strings;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.List;

public class ActionService implements ActionStub {

    @Override
    public Future<SAction> fetchAction(final String normalizedUri,
                                       final HttpMethod method) {
        return this.fetchAction(normalizedUri, method, null);
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

    @Override
    public Future<List<SAction>> seekAction(final String keyword, final String sigma) {
        if (Ut.isNil(sigma) || Ut.isNil(keyword)) {
            return Ux.future(new ArrayList<>());
        } else {
            /*
             * Build condition for spec situations
             *
             * 1. The method must be filtered ( Valid for GET / POST )
             * 2. The records must belong to application with the same `sigma`
             */
            final JsonObject condition = new JsonObject();
            condition.put(KeField.SIGMA, sigma);
            final JsonArray methods = new JsonArray();
            methods.add(HttpMethod.POST.name());
            methods.add(HttpMethod.GET.name());
            condition.put(KeField.METHOD, methods);
            /*
             * 3. keyword processing
             */
            final JsonObject criteria = new JsonObject();
            criteria.put(KeField.NAME + ",c", keyword);
            criteria.put(KeField.CODE + ",c", keyword);
            criteria.put(KeField.URI + ",c", keyword);
            condition.put("$0", criteria);
            return Ux.Jooq.on(SActionDao.class).fetchAndAsync(condition);
        }
    }
}
