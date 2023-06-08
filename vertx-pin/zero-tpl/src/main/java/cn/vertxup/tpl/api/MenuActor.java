package cn.vertxup.tpl.api;

import cn.vertxup.tpl.service.MenuStub;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.mod.tpl.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Me;
import io.vertx.up.annotations.Queue;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import jakarta.inject.Inject;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@Queue
public class MenuActor {

    @Inject
    private transient MenuStub menuStub;

    @Address(Addr.Menu.MY_FETCH)
    public Future<JsonArray> fetchMy(final JsonObject condition, final User user) {
        condition.put(KName.OWNER, Ux.keyUser(user));
        return this.menuStub.fetchMy(condition);
    }

    @Me
    @Address(Addr.Menu.MY_SAVE)
    public Future<JsonArray> saveMy(final JsonObject data, final User user) {
        data.put(KName.OWNER, Ux.keyUser(user));
        /* Condition Building */
        final JsonObject condition = Ux.whereAnd();
        // data -> condition
        Ut.valueCopy(condition, data,
            KName.OWNER,
            KName.Ui.PAGE,
            KName.POSITION,
            KName.TYPE
        );
        /* Data Building */
        JsonArray menus = data.getJsonArray("menus", new JsonArray());
        menus = menus.copy();
        final JsonObject combine = new JsonObject();
        // data -> combine
        Ut.valueCopy(combine, data,
            KName.OWNER,
            KName.Ui.PAGE,
            KName.POSITION,
            KName.TYPE,
            KName.LANGUAGE,
            KName.SIGMA,
            KName.UPDATED_AT,
            KName.UPDATED_BY,
            KName.CREATED_AT,
            KName.CREATED_BY
        );
        Ut.itJArray(menus).forEach(menu -> menu.mergeIn(combine));
        return this.menuStub.saveMy(condition, menus);
    }
}
