package io.vertx.up.secure.bridge;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
interface INFO {

    String AUTH_401_METHOD = "[ Auth ] Your `@Wall` class missed @Authenticate method ! {0}";
    String AUTH_401_SERVICE = "[ Auth ] Your `Lee` in service-loader /META-INF/services/ is missing....";
    String AUTH_401_HANDLER = "[ Auth ] You have configured secure, but the authenticate handler is null! type = {0}";
}
