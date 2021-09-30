package io.vertx.up.uca.web.origin;

import com.google.inject.Guice;
import com.google.inject.Injector;

import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class GuiceInquirer implements Inquirer<Injector> {
    @Override
    public Injector scan(final Set<Class<?>> clazzes) {
        final Injector injector = Guice.createInjector();
        return null;
    }
}
