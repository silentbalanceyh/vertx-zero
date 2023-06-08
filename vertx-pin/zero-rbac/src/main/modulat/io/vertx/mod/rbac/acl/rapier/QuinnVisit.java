package io.vertx.mod.rbac.acl.rapier;

import cn.vertxup.rbac.domain.tables.daos.SViewDao;
import cn.vertxup.rbac.domain.tables.daos.SVisitantDao;
import cn.vertxup.rbac.domain.tables.pojos.SResource;
import cn.vertxup.rbac.domain.tables.pojos.SView;
import cn.vertxup.rbac.domain.tables.pojos.SVisitant;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.rbac.atom.ScOwner;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class QuinnVisit implements Quinn {
    private final transient SyntaxRegion syntaxRegion;

    public QuinnVisit() {
        this.syntaxRegion = new SyntaxRegion();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Future<T> saveAsync(final SResource resource, final ScOwner owner, final JsonObject data) {
        final JsonArray visitantData = Ut.valueJArray(data, KName.Rbac.VISITANT);
        final JsonObject viewData = data.copy();
        // Set visitant = true when Data Not Empty
        viewData.put(KName.Rbac.VISITANT, Ut.isNotNil(visitantData));
        Ut.itJArray(visitantData).forEach(visitantJ -> Ut.valueCopy(visitantJ, viewData,
            KName.SIGMA, KName.LANGUAGE, KName.UPDATED_BY
        ));
        return Quinn.view().<SView>saveAsync(resource.getKey(), owner, viewData).compose(view -> {
            final Boolean virtual = Objects.isNull(resource.getVirtual()) ? Boolean.FALSE : resource.getVirtual();
            if (!virtual) {
                return Ux.futureJ(view);
            }
            // 资源访问者保存流程
            return this.saveVisitant(view, visitantData)
                .compose(visitants -> this.syntaxRegion.regionJ(view, visitants));
        }).compose(json -> Ux.future((T) json));
    }

    /*
     * 保存时从 visitant 中提取 JsonArray 数据，执行保存
     * 输入：
     * - mode               R,= REPLACE / APPEND ( Default = REPLACE )
     * - phase              R,= EAGER | DELAY
     * - seekKey            R
     * - type               R,= VIEW | FORM | LIST | OP
     * - identifier         Optional
     * - viewId             R
     * 区域 / 表单
     * - dmRow
     * - dmQr
     * - dmColumn
     *
     * - aclVisible
     * - aclView
     * - aclVariety
     * - aclVerge
     * - aclVow
     */
    private Future<List<SVisitant>> saveVisitant(final SView viewInput, final JsonArray visitData) {
        // 同一个SView中的 ADD / SAVE
        final ConcurrentMap<String, JsonObject> seekMap = Ut.elementMap(visitData, KName.Rbac.SEEK_KEY);
        final UxJooq jq = Ux.Jooq.on(SVisitantDao.class);
        return jq.<SVisitant>fetchAsync(KName.VIEW_ID, viewInput.getKey()).compose(visitors -> {
            // If existing seekKey
            final ConcurrentMap<String, SVisitant> visitorM = Ut.elementMap(visitors, SVisitant::getSeekKey);
            final List<SVisitant> qUp = new ArrayList<>();
            final List<SVisitant> qAdd = new ArrayList<>();
            seekMap.forEach((seekKey, updateData) -> {
                final LocalDateTime at = LocalDateTime.now();
                if (visitorM.containsKey(seekKey)) {
                    // UPDATE
                    final SVisitant visitor = visitorM.get(seekKey);
                    final SVisitant updated = Ux.updateT(visitor, updateData);
                    {
                        updated.setUpdatedAt(at);
                    }
                    qUp.add(updated);
                } else {
                    // INSERT
                    final SVisitant insert = Ux.fromJson(updateData, SVisitant.class);
                    {
                        // Add Data
                        insert.setKey(UUID.randomUUID().toString());
                        insert.setViewId(viewInput.getKey());
                        insert.setCreatedBy(insert.getUpdatedBy());
                        insert.setCreatedAt(at);
                        insert.setUpdatedAt(at);
                        insert.setActive(Boolean.TRUE);
                    }
                    qAdd.add(insert);
                }
            });
            final List<Future<List<SVisitant>>> futures = new ArrayList<>();
            futures.add(jq.insertAsync(qAdd));
            futures.add(jq.updateAsync(qUp));
            return Fn.combineT(futures).compose(matrix -> {
                final List<SVisitant> visitants = new ArrayList<>();
                matrix.forEach(visitants::addAll);
                return Ux.future(visitants);
            });
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Future<T> fetchAsync(final String viewId, final ScOwner owner) {
        return Ux.Jooq.on(SViewDao.class).<SView>fetchByIdAsync(viewId).compose(view -> {
            if (Objects.isNull(view)) {
                return Ux.futureJ();
            }
            return Ux.Jooq.on(SVisitantDao.class).<SVisitant>fetchAsync(KName.VIEW_ID, view.getKey())
                .compose(visitants -> this.syntaxRegion.regionJ(view, visitants));
        }).compose(json -> Ux.future((T) json));
    }
}
