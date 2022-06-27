package io.vertx.up.uca.marshal;

import io.vertx.core.SockOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class SockSetUp implements JTransformer<SockOptions> {

    private static final Annal LOGGER = Annal.get(SockSetUp.class);

    @Override
    public SockOptions transform(final JsonObject config) {
        return Fn.getSemi(null == config, LOGGER,
            SockOptions::new,
            () -> new SockOptions(config));
    }
}
