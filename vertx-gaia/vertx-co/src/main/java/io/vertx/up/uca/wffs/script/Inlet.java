package io.vertx.up.uca.wffs.script;

import io.horizon.uca.cache.Cc;
import io.vertx.core.json.JsonObject;
import org.apache.commons.jexl3.JexlContext;

/**
 * Inlet for parameter processing on JEXL context
 * The code should be:
 * JexlContext context = new MapContext();
 * context.put(..., ...);
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Inlet {
    Cc<String, Inlet> CC_INLET = Cc.openThread();

    static Inlet data(final boolean prefix) {
        return CC_INLET.pick(() -> new InletData(prefix),
            InletData.class.getName() + String.valueOf(prefix).toUpperCase());
    }

    static Inlet flow(final boolean prefix) {
        return CC_INLET.pick(() -> new InletFlow(prefix),
            InletFlow.class.getName() + String.valueOf(prefix).toUpperCase());
    }

    static Inlet user(final boolean prefix) {
        return CC_INLET.pick(() -> new InletUser(prefix),
            InletUser.class.getName() + String.valueOf(prefix).toUpperCase());
    }

    /*
     * JexlContext will put the data into different part for variables here
     */
    void compile(JexlContext context, JsonObject data, JsonObject config);

    default void compile(final JexlContext context, final JsonObject data) {
        this.compile(context, data, new JsonObject());
    }
}
