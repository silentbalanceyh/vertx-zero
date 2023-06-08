package cn.vertxup.tpl.api;

import io.vertx.core.json.JsonObject;
import io.vertx.mod.tpl.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;
import jakarta.ws.rs.BodyParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

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
     *      - icon, text, uri
     * - X_MENU_MY
     *      - type， FIXED
     *      - page， FIXED
     *      - position， FIXED
     *      - owner, XHeader
     *      - uiSort    Ui
     *      - uiColorFg - Ui  ( Color Picker )
     *      - uiColorBg - Ui  ( Color Picker )
     *  When Tree
     *      - key
     *      - uiParent
     *
     * - Delete Condition
     * {
     *      "owner": "xxx",
     *      "page": "",
     *      "position": "",
     *      "type": ""
     *      "menus": [
     *      ]
     * }
     */
    @POST
    @Path("/my/menu/save")
    @Address(Addr.Menu.MY_SAVE)
    JsonObject saveMy(@BodyParam JsonObject body);
}
