package io.vertx.up.uca.options;

import io.horizon.eon.VMessage;
import io.horizon.exception.ProgramException;
import io.horizon.uca.log.Annal;
import io.vertx.circuitbreaker.CircuitBreakerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.Ruler;
import io.vertx.up.eon.configure.YmlCore;
import io.vertx.up.fn.Fn;
import io.vertx.up.runtime.ZeroStore;

public class CircuitVisitor implements Visitor<CircuitBreakerOptions> {

    private static final Annal LOGGER = Annal.get(CircuitVisitor.class);

    @Override
    public CircuitBreakerOptions visit(final String... key)
        throws ProgramException {
        // 3. CircuitBreakerOptions building.
        final JsonObject config = Fn.runOr(
            ZeroStore.is(YmlCore.circuit.__KEY), LOGGER,
            () -> ZeroStore.option(YmlCore.circuit.__KEY),
            JsonObject::new);
        // 4. Verify the configuration data
        assert config != null;
        return this.visit(config);
    }

    private CircuitBreakerOptions visit(final JsonObject data)
        throws ProgramException {
        LOGGER.info(VMessage.Visitor.V_BEFORE, YmlCore.circuit.__KEY, "Circuit", data.encode());
        Ruler.verify(YmlCore.circuit.__KEY, data);
        return new CircuitBreakerOptions(data);
    }
}
