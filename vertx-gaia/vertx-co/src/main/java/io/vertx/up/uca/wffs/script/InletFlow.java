package io.vertx.up.uca.wffs.script;

import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;
import org.apache.commons.jexl3.JexlContext;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class InletFlow extends AbstractInlet {
    @Override
    public void compile(final JexlContext context, final JsonObject data, final JsonObject config) {
        final JsonObject workflow = Ut.valueJObject(data, KName.Flow.WORKFLOW);
        context.set("$zw", workflow.getMap());
        this.logger().info("[ Script ] ( Workflow ) The variable `$zw` has been bind: {0}", workflow.encode());
    }
}
