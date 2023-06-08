package io.vertx.mod.rbac.acl.rapier;

import cn.vertxup.rbac.domain.tables.daos.SViewDao;
import cn.vertxup.rbac.domain.tables.daos.SVisitantDao;
import cn.vertxup.rbac.domain.tables.pojos.SPacket;
import cn.vertxup.rbac.domain.tables.pojos.SResource;
import cn.vertxup.rbac.domain.tables.pojos.SView;
import cn.vertxup.rbac.domain.tables.pojos.SVisitant;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.ClusterSerializable;
import io.vertx.mod.rbac.atom.ScOwner;
import io.vertx.mod.rbac.cv.em.PackType;
import io.vertx.mod.rbac.ruler.element.HEyelet;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 管理平台专用类，用于处理管理部分的值读取操作
 * 「访问者」语义
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class SyntaxRegion {
    // 根据定义、资源、拥有者处理视图信息
    /*
     * $bindData新结构
     * S_RESOURCE 和 S_PACKET 的关系为一对一，此处可直接设置
     * resource = {
     *     "metadata": {
     *          v, q, h
     *          此处 v, q, h -> {
     *              "mapping": {},
     *              "config":  {}
     *          }
     *     },
     *     "data": [
     *          {
     *               v, q, h = [],
     *               virtual = true / false,
     *               view / position
     *               visitant = {
     *                   "seekKey1": {
     *                   }
     *               }
     *          }
     *     ]
     * }
     */
    Future<JsonObject> regionJ(final SResource resource, final ScOwner owner, final SPacket packet) {
        // Capture view based on resource / owner
        /*
         * ownerType  = ownerType
         * owner      = ownerId
         * name       = view
         * position   = position
         * resourceId = resourceId
         */
        final JsonObject viewQr = Ux.whereAnd()
            .put(KName.OWNER_TYPE, owner.type().name())
            .put(KName.OWNER, owner.owner())
            // 多视图支持，整体形成一个 view - visitant 的完整结构（多叉树）
            // .put(KName.NAME, owner.view())
            // .put(KName.POSITION, owner.position())
            .put(KName.RESOURCE_ID, resource.getKey());
        return Ux.Jooq.on(SViewDao.class).<SView>fetchAsync(viewQr).compose(views -> {
            // metadata
            final JsonObject response = new JsonObject();
            response.put(KName.METADATA, this.regionMeta(packet));
            // data
            final List<Future<JsonObject>> futures = new ArrayList<>();
            views.forEach(view -> futures.add(this.regionView(resource, view, packet)));
            return Fn.combineA(futures).compose(data -> {
                response.put(KName.DATA, data);
                return Ux.future(response);
            });
        });
    }

    Future<JsonObject> regionJ(final SView view, final List<SVisitant> visitants) {
        final JsonObject response = Ux.toJson(view);
        response.put(KName.VIEW, view.getName());
        response.put(KName.VIRTUAL, Boolean.TRUE);
        {
            // v, q, h
            response.put(KName.Rbac.PACK_H, Ut.toJObject(view.getRows()));
            response.put(KName.Rbac.PACK_Q, Ut.toJObject(view.getCriteria()));
            response.put(KName.Rbac.PACK_V, Ut.toJArray(view.getProjection()));
        }
        final JsonObject visitantJ = new JsonObject();
        visitants.forEach(visitant -> visitantJ.put(visitant.getSeekKey(), Ux.toJson(visitant)));
        response.put(KName.Rbac.VISITANT, visitantJ);
        return Ux.future(response);
    }

    private Future<JsonObject> regionView(final SResource resource, final SView view, final SPacket packet) {
        Objects.requireNonNull(view);
        final JsonObject response = this.regionVInit(resource, view);
        final ConcurrentMap<String, Future<ClusterSerializable>> eyeletM = new ConcurrentHashMap<>();
        eyeletM.put(KName.Rbac.PACK_V, this.regionV(packet, view));
        eyeletM.put(KName.Rbac.PACK_H, this.regionH(packet, view));
        eyeletM.put(KName.Rbac.PACK_Q, this.regionQ(packet, view));
        return Fn.combineM(eyeletM).compose(map -> {
            final JsonObject vqh = Ut.toJObject(map);
            // view / position for front-end calculation
            // 多视图管理时需在前端执行过滤提取数据
            // 1 Resource --- n View --- n Visitant
            // 一对多再对多的两层多叉树
            response.mergeIn(vqh, false);
            return Ux.future(response);
        }).compose(responseJ -> {
            final Boolean virtual = Objects.isNull(resource.getVirtual()) ? Boolean.FALSE : resource.getVirtual();
            if (virtual) {
                return Ux.Jooq.on(SVisitantDao.class).<SVisitant>fetchAsync(KName.VIEW_ID, view.getKey())
                    .compose(visitants -> {
                        final JsonObject visitantJ = new JsonObject();
                        visitants.forEach(visitant -> visitantJ.put(visitant.getSeekKey(), Ux.toJson(visitant)));
                        responseJ.put(KName.Rbac.VISITANT, visitantJ);
                        return Ux.future(responseJ);
                    });
            } else {
                return Ux.future(responseJ);
            }
        });
    }

    // ----------------------- 私有方法 ----------------------
    // ----------------------- metadata ----------------------
    private JsonObject regionMeta(final SPacket packet) {
        final JsonObject metadata = new JsonObject();
        metadata.put(KName.Rbac.PACK_H, this.regionH(packet));
        metadata.put(KName.Rbac.PACK_V, this.regionV(packet));
        metadata.put(KName.Rbac.PACK_Q, this.regionQ(packet));
        return metadata;
    }

    // metadata 节点
    private JsonObject regionV(final SPacket packet) {
        final PackType.VType vType = Ut.toEnum(packet::getVType, PackType.VType.class, PackType.VType.NONE);
        if (PackType.VType.NONE == vType) {
            return new JsonObject();
        }
        final JsonObject response = new JsonObject();
        response.put(KName.MAPPING, Ut.toJObject(packet.getVMapping()));
        response.put(KName.CONFIG, Ut.toJObject(packet.getVConfig()));
        return response;
    }


    private JsonObject regionH(final SPacket packet) {
        final PackType.HType hType = Ut.toEnum(packet::getHType, PackType.HType.class, PackType.HType.NONE);
        if (PackType.HType.NONE == hType) {
            return new JsonObject();
        }
        final JsonObject response = new JsonObject();
        response.put(KName.MAPPING, Ut.toJObject(packet.getHMapping()));
        response.put(KName.CONFIG, Ut.toJObject(packet.getHConfig()));
        return response;
    }

    private JsonObject regionQ(final SPacket packet) {
        final PackType.QType qType = Ut.toEnum(packet::getQType, PackType.QType.class, PackType.QType.NONE);
        if (PackType.QType.NONE == qType) {
            return new JsonObject();
        }
        final JsonObject response = new JsonObject();
        response.put(KName.MAPPING, Ut.toJObject(packet.getQMapping()));
        response.put(KName.CONFIG, Ut.toJObject(packet.getQConfig()));
        return response;
    }

    // ----------------------- data ----------------------
    private Future<ClusterSerializable> regionV(final SPacket packet, final SView view) {
        final PackType.VType vType = Ut.toEnum(packet::getVType, PackType.VType.class, PackType.VType.NONE);
        if (PackType.VType.NONE == vType) {
            return Ux.future(new JsonArray());
        }
        final HEyelet eyelet = HEyelet.instance(vType);
        if (Objects.isNull(eyelet)) {
            return Ux.future(new JsonArray());
        }
        return eyelet.ingest(packet, view);
    }

    private JsonObject regionVInit(final SResource resource, final SView view) {
        final JsonObject response = new JsonObject();
        response.put(KName.VIRTUAL, Objects.isNull(resource.getVirtual()) ? Boolean.FALSE : resource.getVirtual());
        // view / position for front-end calculation
        // 多视图管理时需在前端执行过滤提取数据
        // 1 Resource --- n View --- n Visitant
        // 一对多再对多的两层多叉树
        response.put(KName.VIEW, view.getName());
        response.put(KName.POSITION, view.getPosition());
        return response;
    }

    private Future<ClusterSerializable> regionH(final SPacket packet, final SView view) {
        final PackType.HType hType = Ut.toEnum(packet::getHType, PackType.HType.class, PackType.HType.NONE);
        if (PackType.HType.NONE == hType) {
            return Ux.future(new JsonObject());
        }
        final HEyelet eyelet = HEyelet.instance(hType);
        if (Objects.isNull(eyelet)) {
            return Ux.future(new JsonObject());
        }
        return eyelet.ingest(packet, view);
    }

    private Future<ClusterSerializable> regionQ(final SPacket packet, final SView view) {
        final PackType.QType qType = Ut.toEnum(packet::getQType, PackType.QType.class, PackType.QType.NONE);
        if (PackType.QType.NONE == qType) {
            return Ux.future(new JsonObject());
        }
        final HEyelet eyelet = HEyelet.instance(qType);
        if (Objects.isNull(eyelet)) {
            return Ux.future(new JsonObject());
        }
        return eyelet.ingest(packet, view);
    }
}
