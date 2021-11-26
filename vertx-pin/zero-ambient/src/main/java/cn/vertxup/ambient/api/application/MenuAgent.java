package cn.vertxup.ambient.api.application;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.ambient.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;

import javax.ws.rs.BodyParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@EndPoint
@Path("/api")
public interface MenuAgent {
    /*
     * Fetch Menu List by
     * - page
     * - position
     * - type
     */
    @POST
    @Path("/my/menu/fetch")
    @Address(Addr.Menu.MY_FETCH)
    JsonObject fetchMy(@BodyParam JsonObject body);

    /*
     * Create new My menu
     * - X_MENU
     *      - icon, text, uri, uiMenu
     * - X_MENU_MY
     *      - type， FIXED
     *      - page， FIXED
     *      - position， FIXED
     *      - owner, XHeader
     *      - uiSort    Ui
     *      - uiParent  Ui
     *      - uiColorFg - Ui  ( Color Picker )
     *      - uiColorBg - Ui  ( Color Picker )
     */
    @POST
    @Path("/my/menu/save")
    @Address(Addr.Menu.MY_SAVE)
    JsonObject saveMy(@BodyParam JsonObject body);
}
