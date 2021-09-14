package cn.vertxup.crud.api;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.cv.Addr;
import io.vertx.tp.crud.cv.em.ApiSpec;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.tp.crud.uca.desk.IxPanel;
import io.vertx.tp.crud.uca.desk.IxWeb;
import io.vertx.tp.crud.uca.input.Pre;
import io.vertx.tp.crud.uca.op.Agonic;
import io.vertx.tp.crud.uca.output.Post;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.commune.Envelop;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

/*
 * 「新版定制完成」
 */
@Queue
public class GetActor {

    /*
     * GET: /api/{actor}/{key}
     *     200: JqTool Data
     *     204: JqTool No Data
     */
    @Address(Addr.Get.BY_ID)
    public Future<Envelop> getById(final Envelop envelop) {
        final IxWeb request = IxWeb.create(ApiSpec.BODY_STRING).build(envelop);
        return IxPanel.on(request)
            .passion(Agonic.get()::runJAsync, null)
            .<JsonObject, JsonObject, JsonObject>runJ(request.dataK())
            /*
             * 204 / 200
             */
            .compose(Post::successPost);
    }

    /*
     * GET: /api/{actor}/by/sigma
     *      200: JqTool All
     */
    @Address(Addr.Get.BY_SIGMA)
    public Future<Envelop> getAll(final Envelop envelop) {
        final IxWeb request = IxWeb.create(ApiSpec.BODY_NONE).build(envelop);
        /* Headers */
        final JsonObject headers = envelop.headersX();
        final String sigma = headers.getString(KName.SIGMA);
        if (Ut.isNil(sigma)) {
            return Ux.future(Envelop.success(new JsonArray()));
        }
        Ix.Log.filters(this.getClass(), "All data by sigma: `{0}`", sigma);
        return IxPanel.on(request)
            .input(
                /* Build Condition for All */
                Pre.qAll()::inJAsync
            )
            .passion(Agonic.fetch()::runJAAsync, null)
            .runJ(new JsonObject().put(KName.SIGMA, sigma));
    }

}
