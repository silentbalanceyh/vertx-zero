package io.horizon.spi.business;

import cn.vertxup.jet.domain.tables.daos.IApiDao;
import cn.vertxup.jet.domain.tables.pojos.IApi;
import io.horizon.spi.web.Routine;
import io.macrocosm.specification.program.HArk;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.jet.refine.Jt;
import io.vertx.mod.ke.refine.Ke;
import io.vertx.up.eon.KName;
import io.vertx.up.uca.soul.UriMeta;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class ExRouteApi implements Routine {

    @Override
    public Future<List<UriMeta>> searchAsync(final String keyword, final String sigma) {
        // 截断
        if (Ut.isNil(keyword) || Ut.isNil(sigma)) {
            return Ux.future(new ArrayList<>());
        }
        final HArk ark = Ke.ark(sigma);
        // 截断
        if (Objects.isNull(ark)) {
            return Ux.future(new ArrayList<>());
        }

        final JsonObject condition = new JsonObject();
        condition.put(KName.SIGMA, sigma);
        /*
         * Criteria for `keyword`
         */
        final JsonObject criteria = new JsonObject();
        criteria.put("name,c", keyword);
        criteria.put("comment,c", keyword);
        criteria.put("uri,c", keyword);
        condition.put("$0", criteria);
        /*
         * JtApp process
         */
        return Ux.Jooq.on(IApiDao.class).<IApi>fetchAndAsync(condition).compose(apis -> {
            /*
             * UriMeta building
             */
            final List<UriMeta> uris = new ArrayList<>();
            apis.forEach(api -> {
                final UriMeta meta = new UriMeta();
                meta.setDynamic(Boolean.TRUE);
                meta.setKey(api.getKey());
                /*
                 * Api Processing
                 */
                final String uri = Jt.toPath(ark, api::getUri, api.getSecure());
                meta.setUri(uri);
                // meta.setMethod(Ut.toEnum(api::getMethod, HttpMethod.class, HttpMethod.GET));
                meta.setMethod(Ut.toMethod(api::getMethod));
                /*
                 * Comment analyzing
                 */
                meta.setName(api.getName());
                meta.setComment(api.getComment());
                uris.add(meta);
            });
            /*
             * The final results
             */
            return Ux.future(uris);
        });
    }
}
