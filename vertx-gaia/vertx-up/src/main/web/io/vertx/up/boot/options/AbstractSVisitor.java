package io.vertx.up.boot.options;

import io.horizon.exception.ProgramException;
import io.horizon.uca.log.Annal;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.configure.YmlCore;
import io.vertx.up.exception.daemon.ServerConfigException;
import io.vertx.up.fn.Fn;
import io.vertx.up.runtime.ZeroStore;
import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractSVisitor {

    protected JsonArray serverPre(final int expected, final String... key)
        throws ProgramException {
        // 1. Must be the first line, fixed position.
        //        Fn.verifyLenEq(this.getClass(), expected, (Object[]) key);
        // 2. Visit the node for server, http
        final JsonObject data = ZeroStore.option(YmlCore.server.__KEY);

        Fn.outBug(null == data || !data.containsKey(KName.SERVER), this.logger(),
            ServerConfigException.class,
            this.getClass(), null == data ? null : data.encode());

        return Ut.valueJArray(data, KName.SERVER);
    }

    public Annal logger() {
        return Annal.get(this.getClass());
    }
}
