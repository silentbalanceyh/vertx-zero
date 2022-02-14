package cn.vertxup.rbac.api;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.rbac.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Codex;
import io.vertx.up.annotations.EndPoint;
import io.vertx.up.eon.KName;

import javax.ws.rs.BodyParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/*
 * Login Api
 * 1. Provide username/password to access /oauth/login get client_secret field ( Issue when create )
 * 2. Access /oauth/authorize to get authorization code
 * 3. Access /oauth/token to get token
 */
@EndPoint
public interface AuthAgent {

    /*
     * /oauth/login
     *
     * Request:
     * {
     *      username: "lang.yu",
     *      password: "XXX(MD5)",
     *      verifyCode: "When `verifyCode` enabled, here must contains additional part"
     * }
     */
    @POST
    @Path("/oauth/login")
    @Address(Addr.Auth.LOGIN)
    JsonObject login(@BodyParam @Codex JsonObject data);

    /*
     * /oauth/authorize
     *
     * Request:
     * {
     *      client_id: "xxx",
     *      client_secret: "xxx",
     *      response_type: "code",
     *      scope: "xxx"
     * }
     */
    @POST
    @Path("/oauth/authorize")
    @Address(Addr.Auth.AUTHORIZE)
    JsonObject authorize(@BodyParam @Codex JsonObject data);

    /*
     * /oauth/token
     *
     * Request:
     * {
     *      client_id: "xxx",
     *      code: "temp"
     * }
     */
    @POST
    @Path("/oauth/token")
    @Address(Addr.Auth.TOKEN)
    JsonObject token(@BodyParam @Codex JsonObject data);

    // --------------------- Image Code ------------------------


    /*
     * Sigma must be in XHeader for multi application here
     */
    @POST
    @Path("/captcha/image/:username")
    @Address(Addr.Auth.GENERATE_IMAGE)
    JsonObject generateImage(@PathParam(KName.USERNAME) String username);

    @POST
    @Path("/captcha/image-verify")
    @Address(Addr.Auth.IMAGE_VERIFY)
    JsonObject verifyImage(@BodyParam JsonObject request);
}
