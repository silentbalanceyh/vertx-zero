package io.vertx.tp.jet;

import io.vertx.tp.error._400RequiredParamException;
import io.vertx.tp.error._500DefinitionErrorException;
import io.vertx.up.commune.Envelop;
import io.vertx.up.fn.Fn;

/*
 * Uniform Error throw out and build Envelop ( Failure )
 * Define vertx-readible.yml for ui message.
 */
public class JetThanatos {

    private transient final Class<?> target;

    private JetThanatos(final Class<?> target) {
        this.target = target;
    }

    public static JetThanatos create(final Class<?> target) {
        return Fn.pool(Pool.ENSURERS, target, () -> new JetThanatos(target));
    }

    public Envelop to400RequiredParam(final String filename) {
        return Envelop.failure(new _400RequiredParamException(this.target, filename));
    }

    public Envelop to500DefinitionError(final String key) {
        return Envelop.failure(new _500DefinitionErrorException(this.target, key));
    }
}
