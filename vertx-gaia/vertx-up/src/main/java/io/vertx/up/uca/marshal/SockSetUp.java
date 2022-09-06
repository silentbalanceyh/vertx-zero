package io.vertx.up.uca.marshal;

import io.vertx.core.SockOptions;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class SockSetUp implements JTransformer<SockOptions> {

    private static final Annal LOGGER = Annal.get(SockSetUp.class);

    @Override
    public SockOptions transform(final JsonObject config) {
        return Fn.orSemi(null == config, LOGGER, SockOptions::new, () -> {
            /*
             * websocket:       ( SockOptions )
             * config:          ( HttpServerOptions )
             */
            final JsonObject websockJ = Ut.valueJObject(config, KName.WEB_SOCKET);
            final SockOptions options = Ut.deserialize(websockJ, SockOptions.class);

            /*
             * Bind the HttpServerOptions to SockOptions for future usage
             */
            final JsonObject optionJ = Ut.valueJObject(config, KName.CONFIG);
            final HttpServerOptions serverOptions = new HttpServerOptions(optionJ);
            options.options(serverOptions);
            return options;
        });
    }
}
