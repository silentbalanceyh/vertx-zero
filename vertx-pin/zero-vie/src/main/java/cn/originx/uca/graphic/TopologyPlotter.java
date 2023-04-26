package cn.originx.uca.graphic;

import cn.vertxup.ambient.domain.tables.daos.XCategoryDao;
import cn.vertxup.ambient.domain.tables.pojos.XCategory;
import io.horizon.eon.VString;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.Refer;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static cn.originx.refine.Ox.LOG;

public class TopologyPlotter extends AbstractPlotter {
    @Override
    public Future<JsonObject> drawAsync(final String recordId, final String relationId) {
        /*
         * ci.device -> nodes
         * rl.device.relation -> edges
         */
        return this.drawAsync(recordId, relationId, () -> {
            final Refer refer = new Refer();
            /* 设备读取（节点）*/
            return this.dao(recordId).fetchAllAsync()
                .compose(Ux::futureA).compose(refer::future)
                /* 关系读取（边）*/
                .compose(nil -> this.dao(relationId).fetchAllAsync())
                .compose(Ux::futureA).compose(relation -> PlotterHelper.drawAsync(refer.get(), relation));
        });
    }

    @Override
    public Future<JsonObject> drawAsync(final String recordId, final String relationId, final Set<String> ignores) {
        return this.drawAsync(recordId, relationId, () -> {
            final String sigma = this.app.getSigma();
            /* 读取 categoryThird */
            final JsonObject condition = new JsonObject();
            condition.put("identifier,i", Ut.toJArray(ignores));
            condition.put(KName.SIGMA, sigma);
            condition.put(VString.EMPTY, Boolean.TRUE);
            return Ux.Jooq.on(XCategoryDao.class).<XCategory>fetchAndAsync(condition).compose(categories -> {
                /* 读取不为 key 的 */
                final Set<String> keys = categories.stream().map(XCategory::getKey).collect(Collectors.toSet());
                final JsonObject criteria = new JsonObject();
                criteria.put("categoryThird,!i", Ut.toJArray(keys));
                /* 设备读取（节点） */
                final Refer refer = new Refer();
                return this.dao(recordId).fetchAsync(criteria)
                    .compose(Ux::futureA).compose(refer::future)
                    /* 关系读取（边）*/
                    .compose(nil -> this.dao(relationId).fetchAllAsync())
                    .compose(Ux::futureA).compose(relation -> PlotterHelper.drawAsync(refer.get(), relation));
            });
        });
    }

    private Future<JsonObject> drawAsync(
        final String recordId, final String relationId,
        final Supplier<Future<JsonObject>> consumer) {
        if (Ut.isNil(recordId) || Ut.isNil(relationId)) {
            LOG.Uca.warn(this.getClass(), "传入模型ID有问题：node = {0}, edge = {1}",
                recordId, relationId);
            return Ux.future(new JsonObject());
        } else {
            return consumer.get();
        }
    }
}
