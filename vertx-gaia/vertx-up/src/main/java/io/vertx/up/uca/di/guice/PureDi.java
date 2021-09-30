package io.vertx.up.uca.di.guice;

import com.google.inject.AbstractModule;
import io.vertx.up.log.Annal;

/**
 * Prepared
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class PureDi extends AbstractModule {
    private static final Annal LOGGER = Annal.get(PureDi.class);

    @Override
    protected void configure() {
        LOGGER.info("[ DI ] The DI environment will be initialized!");

    }
}
