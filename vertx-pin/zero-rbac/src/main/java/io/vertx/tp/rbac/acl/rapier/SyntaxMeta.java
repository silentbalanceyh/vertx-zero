package io.vertx.tp.rbac.acl.rapier;

import cn.vertxup.rbac.domain.tables.daos.SViewDao;
import cn.vertxup.rbac.domain.tables.pojos.SPacket;
import cn.vertxup.rbac.domain.tables.pojos.SResource;
import cn.vertxup.rbac.domain.tables.pojos.SView;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.ClusterSerializable;
import io.vertx.tp.rbac.atom.ScOwner;
import io.vertx.tp.rbac.cv.em.PackType;
import io.vertx.tp.rbac.ruler.element.HEyelet;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/**
 * 管理平台专用类，用于处理管理部分的值读取操作
 * 「访问者」语义
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class SyntaxMeta {
    // 根据定义、资源、拥有者处理视图信息
    /*
     * {
     *         "v": {
     *             "mapping": {},
     *             "config": {},
     *             "value": ...
     *         },
     *         "h": {
     *             "mapping": {},
     *             "config": {},
     *             "value": ...
     *         },
     *         "q": {
     *             "mapping": {},
     *             "config": {},
     *             "value": ...
     *         }
     *     }
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
            .put(KName.NAME, owner.view())
            .put(KName.POSITION, owner.position())
            .put(KName.RESOURCE_ID, resource.getKey());
        return Ux.Jooq.on(SViewDao.class).<SView>fetchOneAsync(viewQr).compose(view -> {
            final ConcurrentMap<String, Future<JsonObject>> eyeletM = new ConcurrentHashMap<>();
            eyeletM.put(KName.Rbac.PACK_V, this.regionV(packet, view));
            eyeletM.put(KName.Rbac.PACK_H, this.regionH(packet, view));
            eyeletM.put(KName.Rbac.PACK_Q, this.regionQ(packet, view));
            return Fn.combineM(eyeletM).compose(map -> Ux.future(Ut.toJObject(map)));
        });
    }

    // ----------------------- 私有方法 ----------------------
    private Future<JsonObject> regionV(final SPacket packet, final SView view) {
        final PackType.VType vType = Ut.toEnum(packet::getVType, PackType.VType.class, PackType.VType.NONE);
        if (PackType.VType.NONE == vType) {
            return Ux.futureJ();
        }
        final JsonObject response = new JsonObject();
        response.put(KName.MAPPING, Ut.toJObject(packet.getVMapping()));
        response.put(KName.CONFIG, Ut.toJObject(packet.getVConfig()));
        return this.regionValue(response, vType, eyelet -> eyelet.ingest(packet, view));
    }

    private Future<JsonObject> regionH(final SPacket packet, final SView view) {
        final PackType.HType hType = Ut.toEnum(packet::getHType, PackType.HType.class, PackType.HType.NONE);
        if (PackType.HType.NONE == hType) {
            return Ux.futureJ();
        }
        final JsonObject response = new JsonObject();
        response.put(KName.MAPPING, Ut.toJObject(packet.getHMapping()));
        response.put(KName.CONFIG, Ut.toJObject(packet.getHConfig()));
        return this.regionValue(response, hType, eyelet -> eyelet.ingest(packet, view));
    }

    private Future<JsonObject> regionQ(final SPacket packet, final SView view) {
        final PackType.QType qType = Ut.toEnum(packet::getQType, PackType.QType.class, PackType.QType.NONE);
        if (PackType.QType.NONE == qType) {
            return Ux.futureJ();
        }
        final JsonObject response = new JsonObject();
        response.put(KName.MAPPING, Ut.toJObject(packet.getQMapping()));
        response.put(KName.CONFIG, Ut.toJObject(packet.getQConfig()));
        return this.regionValue(response, qType, eyelet -> eyelet.ingest(packet, view));
    }

    /*
     *  v -> view
     *  h -> view
     *  q -> view
     */
    @SuppressWarnings("all")
    private Future<JsonObject> regionValue(final JsonObject response,
                                           final Enum eyeType,
                                           final Function<HEyelet, Future<ClusterSerializable>> consumer) {
        final HEyelet eyelet = HEyelet.instance(eyeType);
        if (Objects.isNull(eyelet)) {
            return Ux.future(response);
        } else {
            return consumer.apply(eyelet).compose(vJ -> {
                response.put(KName.VALUE, vJ);
                return Ux.future(response);
            });
        }
    }
}
