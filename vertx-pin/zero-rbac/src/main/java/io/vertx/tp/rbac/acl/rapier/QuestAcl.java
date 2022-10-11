package io.vertx.tp.rbac.acl.rapier;

import cn.vertxup.rbac.domain.tables.daos.SResourceDao;
import cn.vertxup.rbac.domain.tables.pojos.SPacket;
import cn.vertxup.rbac.domain.tables.pojos.SResource;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.rbac.atom.ScOwner;
import io.vertx.up.commune.Envelop;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class QuestAcl implements Quest {
    private final transient SyntaxMeta syntaxMeta;

    QuestAcl() {
        this.syntaxMeta = new SyntaxMeta();
    }

    @Override
    public Future<JsonObject> fetchAsync(final JsonObject input, final List<SPacket> packets,
                                         final ScOwner owner) {
        if (packets.isEmpty()) {
            return Ux.futureJ();
        }
        /*
         * 1. 提取 SPacket 中配置的资源编码：resource code, 属性名：resource
         * 2. 根据列表提取完整的 sigma 数据
         * 3. 构造最终的资源条件
         *    SIGMA = ? AND RESOURCE IN (?, ?)
         */
        final List<SPacket> packetData = packets.stream()
            .filter(Objects::nonNull)
            .filter(item -> Ut.notNil(item.getResource()))
            .toList();

        final Set<String> restCodes = packetData.stream()
            .map(SPacket::getResource)
            .collect(Collectors.toSet());
        final String sigma = Ut.valueString(input, KName.SIGMA);
        final JsonObject condition = Ux.whereAnd()
            .put(KName.SIGMA, sigma)
            .put(KName.CODE + ",i", Ut.toJArray(restCodes));


        return Ux.Jooq.on(SResourceDao.class).<SResource>fetchAsync(condition).compose(resources -> {
            // 根据资源记录读取所需视图集
            final ConcurrentMap<String, SResource> resourceMap =
                Ut.elementMap(resources, SResource::getCode);
            // resource -> json
            final ConcurrentMap<String, Future<JsonObject>> futureMap =
                new ConcurrentHashMap<>();
            packetData.forEach(packet -> {
                final SResource resource = resourceMap.get(packet.getResource());
                futureMap.put(resource.getCode(), this.syntaxMeta.regionJ(resource, owner, packet));
            });
            return Fn.combineM(futureMap);
        }).compose(map -> Ux.future(Ut.toJObject(map)));
    }

    @Override
    public Future<Envelop> beforeAsync(final Envelop request, final JsonObject matrixJ) {
        return null;
    }

    @Override
    public Future<Envelop> afterAsync(final Envelop request, final JsonObject matrixJ) {
        return null;
    }
}
