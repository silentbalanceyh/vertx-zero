package io.vertx.up.uca.wffs.script;

import io.vertx.core.json.JsonObject;
import io.vertx.up.uca.cache.Cc;
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

    static Inlet data() {
        return CC_INLET.pick(InletData::new, InletData.class.getName());
    }

    static Inlet flow() {
        return CC_INLET.pick(InletFlow::new, InletFlow.class.getName());
    }

    static Inlet user() {
        return CC_INLET.pick(InletUser::new, InletUser.class.getName());
    }

    /*
     * JexlContext will put the data into different part for variables here
     */
    void compile(JexlContext context, JsonObject data, JsonObject config);

    default void compile(final JexlContext context, final JsonObject data) {
        this.compile(context, data, new JsonObject());
    }
}
