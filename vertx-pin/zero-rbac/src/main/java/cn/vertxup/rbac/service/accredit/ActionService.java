package cn.vertxup.rbac.service.accredit;

import cn.vertxup.rbac.domain.tables.daos.SActionDao;
import cn.vertxup.rbac.domain.tables.daos.SResourceDao;
import cn.vertxup.rbac.domain.tables.pojos.SAction;
import cn.vertxup.rbac.domain.tables.pojos.SPermission;
import cn.vertxup.rbac.domain.tables.pojos.SResource;
import io.horizon.eon.VString;
import io.horizon.spi.web.Routine;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.uca.soul.UriAeon;
import io.vertx.up.uca.soul.UriMeta;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

public class ActionService implements ActionStub {

    @Override
    public Future<SAction> fetchAction(final String normalizedUri,
                                       final HttpMethod method) {
        return this.fetchAction(normalizedUri, method, null);
    }

    @Override
    public Future<List<SAction>> fetchAction(final String permissionId) {
        return Ux.Jooq.on(SActionDao.class).fetchAsync(KName.PERMISSION_ID, permissionId);
    }

    @Override
    public Future<SAction> fetchAction(final String normalizedUri,
                                       final HttpMethod method,
                                       final String sigma) {
        final JsonObject actionFilters = new JsonObject();
        actionFilters.put(VString.EMPTY, Boolean.TRUE);
        actionFilters.put(KName.URI, normalizedUri);
        if (Ut.isNotNil(sigma)) {
            actionFilters.put(KName.SIGMA, sigma);
        }
        actionFilters.put(KName.METHOD, method.name());
        return Ux.Jooq.on(SActionDao.class)
            .fetchOneAsync(actionFilters);
    }

    @Override
    public Future<SResource> fetchResource(final String key) {
        return Ux.Jooq.on(SResourceDao.class)
            .fetchByIdAsync(key);
    }

    @Override
    public Future<List<SAction>> searchAuthorized(final String keyword, final String sigma) {
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
            condition.put(KName.SIGMA, sigma);
            final JsonArray methods = new JsonArray();
            methods.add(HttpMethod.POST.name());
            methods.add(HttpMethod.GET.name());
            condition.put(KName.METHOD, methods);
            /*
             * 3. keyword processing
             */
            final JsonObject criteria = new JsonObject();
            criteria.put(KName.NAME + ",c", keyword);
            criteria.put(KName.CODE + ",c", keyword);
            criteria.put(KName.URI + ",c", keyword);
            condition.put("$0", criteria);
            return Ux.Jooq.on(SActionDao.class).fetchAndAsync(condition);
        }
    }

    @Override
    public Future<List<UriMeta>> searchAll(final String keyword, final String sigma) {
        /*
         * Static by `keyword` of UriMeta
         */
        final List<UriMeta> staticList = UriAeon.uriSearch(keyword);
        /*
         * Dynamic by `keyword` and `sigma` ( zero-jet )
         */
        return Ux.channel(Routine.class, ArrayList::new, route -> route.searchAsync(keyword, sigma)).compose(uris -> {
            /*
             * Combine two list of uri metadata
             */
            final List<UriMeta> resultList = new ArrayList<>(uris);
            resultList.addAll(staticList);
            /*
             * After combine, re-order the result list by `uri`
             */
            resultList.sort(Comparator.comparing(UriMeta::getUri));
            return Ux.future(resultList);
        });
    }

    @Override
    public Future<List<SAction>> saveAction(final SPermission permission, final JsonArray actionData) {
        /*
         * Read action list of original
         */
        return Ux.Jooq.on(SActionDao.class).<SAction>fetchAsync(KName.PERMISSION_ID, permission.getKey())
            .compose(oldList -> {
                /*
                 * Get actions of input
                 */
                final List<SAction> inputList = Ux.fromJson(actionData, SAction.class);

                final ConcurrentMap<String, SAction> mapInput = Ut.elementMap(inputList, SAction::getKey);
                final ConcurrentMap<String, SAction> mapStored = Ut.elementMap(oldList, SAction::getKey);
                /*
                 * Remove link
                 */
                final List<SAction> updated = new ArrayList<>();
                oldList.forEach(original -> {
                    /*
                     * Existing in inputMap but not in original
                     * Here should remove link between Permission / Action
                     */
                    if (!mapInput.containsKey(original.getKey())) {
                        this.setAction(original, permission, null);
                        updated.add(original);
                    }
                });
                /*
                 * Add link
                 */
                mapInput.keySet().stream().filter(key -> !mapStored.containsKey(key)).forEach(actionKey -> {
                    final SAction action = mapInput.get(actionKey);
                    this.setAction(action, permission, permission.getKey());
                    updated.add(action);
                });
                return Ux.Jooq.on(SActionDao.class).updateAsync(updated);
            });
    }

    private void setAction(final SAction action, final SPermission permission, final String permissionId) {
        action.setPermissionId(permissionId);
        action.setUpdatedAt(LocalDateTime.now());
        action.setUpdatedBy(permission.getUpdatedBy());
        action.setActive(Boolean.TRUE);
        action.setLanguage(permission.getLanguage());
        action.setSigma(permission.getSigma());
    }

    @Override
    public Future<Boolean> removeAction(final String permissionId, final String userKey) {
        return Ux.Jooq.on(SActionDao.class).<SAction>fetchAsync(KName.PERMISSION_ID, permissionId)
            .compose(actions -> {
                /*
                 * actions modification, no createdBy processing here
                 */
                actions.forEach(action -> {
                    action.setPermissionId(null);
                    action.setUpdatedAt(LocalDateTime.now());
                    action.setUpdatedBy(userKey);
                });
                return Ux.Jooq.on(SActionDao.class).updateAsync(actions);
            })
            .compose(nil -> Ux.future(Boolean.TRUE));
    }
}
