package io.vertx.mod.jet;

import io.horizon.uca.cache.Cc;
import io.vertx.mod.jet.error._400RequiredParamException;
import io.vertx.mod.jet.error._500DefinitionErrorException;
import io.vertx.up.commune.Envelop;

/*
 * Uniform Error throw out and build Envelop ( Failure )
 * Define vertx-readible.yml for ui message.
 */
public class JetThanatos {

    private static final Cc<Class<?>, JetThanatos> CC_ENSURER = Cc.open();
    private transient final Class<?> target;

    private JetThanatos(final Class<?> target) {
        this.target = target;
    }

    public static JetThanatos create(final Class<?> target) {
        return CC_ENSURER.pick(() -> new JetThanatos(target), target);
        // return Fn.po?l(Pool.ENSURERS, target, () -> new JetThanatos(target));
    }

    public Envelop to400RequiredParam(final String filename) {
        return Envelop.failure(new _400RequiredParamException(this.target, filename));
    }

    public Envelop to500DefinitionError(final String key) {
        return Envelop.failure(new _500DefinitionErrorException(this.target, key));
    }
}
