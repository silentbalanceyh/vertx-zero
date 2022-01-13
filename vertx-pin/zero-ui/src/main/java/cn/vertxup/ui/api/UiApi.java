package cn.vertxup.ui.api;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ui.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;
import io.vertx.up.eon.ID;
import io.vertx.up.eon.KName;

import javax.ws.rs.*;

@EndPoint
@Path("/api")
public interface UiApi {
    /*
     * Condition should be:
     * sigma +
     * {
     *     app,
     *     module,
     *     page
     * }
     * AMP means three parameters here.
     */
    @Path("/ui/page")
    @POST
    @Address(Addr.Page.FETCH_AMP)
    JsonObject fetchPage(@HeaderParam(ID.Header.X_SIGMA) String sigma,
                         @BodyParam JsonObject body);

    /*
     * Condition should be:
     * {
     *     control, ( form Id, list Id )
     *     type
     * }
     */
    @Path("/ui/control")
    @POST
    @Address(Addr.Control.FETCH_BY_ID)
    JsonObject fetchControl(@BodyParam JsonObject body);

    /*
     * Condition should be:
     * {
     *     control
     * }
     */
    @Path("/ui/ops")
    @POST
    @Address(Addr.Control.FETCH_OP)
    JsonObject fetchOp(@BodyParam JsonObject body);

    /*
     * Condition should be:
     * 1. Path Parameter: page, identifier
     * 2. Body Parameter:
     * {
     *      "view": "DEFAULT",
     *      "position": "DEFAULT",
     *      "type": "FORM / LIST",
     * }
     *
     * The most frequency situation is
     * 1. identifier, page, processing based on model and page
     * -  The page navigate you to the fixed page in your app
     * -  The identifier navigate you to the correct model
     * 2. The parameters contain view, position, type to search correct control id
     * -  The target control id is FORM/LIST id that could fetch following:
     *   -  Fetch fields by FORM id ( controlId )
     *   -  Fetch columns by LIST id ( controlId )
     *   -  Fetch ops by LIST id ( controlId )
     */
    @Path("/ui/visitor/:identifier/:page")
    @POST
    @Address(Addr.Control.FETCH_BY_VISITOR)
    JsonObject fetchVisitor(@PathParam(KName.Ui.PAGE) String page,
                            @PathParam(KName.IDENTIFIER) String identifier,
                            @BodyParam JsonObject params);

    /*
     * Fetch form configuration by
     * code, this method is for single form fetch
     */
    @Path("/ui/form/:code")
    @GET
    @Address(Addr.Control.FETCH_FORM_BY_CODE)
    JsonObject fetchForm(@HeaderParam(ID.Header.X_SIGMA) String sigma,
                         @PathParam(KName.CODE) String name);

    /*
     * Fetch form configuration by
     * identifier, this method is for multi forms fetch
     */
    @Path("/ui/forms/:identifier")
    @GET
    @Address(Addr.Control.FETCH_FORM_BY_IDENTIFIER)
    JsonArray fetchForms(@HeaderParam(ID.Header.X_SIGMA) String sigma,
                         @PathParam(KName.IDENTIFIER) String identifier);

    /*
     * Fetch list configuration by
     * identifier, this method is for multi lists fetch
     */
    @Path("/ui/lists/:identifier")
    @GET
    @Address(Addr.Control.FETCH_LIST_BY_IDENTIFIER)
    JsonArray fetchLists(@HeaderParam(ID.Header.X_SIGMA) String sigma,
                         @PathParam(KName.IDENTIFIER) String identifier);

}
