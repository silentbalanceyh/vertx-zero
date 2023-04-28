package io.vertx.up.uca.wffs.script;

import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;
import org.apache.commons.jexl3.JexlContext;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class InletFlow extends AbstractInlet {
    InletFlow(final boolean isPrefix) {
        super(isPrefix);
    }

    @Override
    public void compile(final JexlContext context, final JsonObject data, final JsonObject config) {
        final JsonObject workflow = Ut.valueJObject(data, KName.Flow.WORKFLOW);
        final String zw = this.variable("zw");
        context.set(zw, Ut.toMap(workflow));
        this.console("[ Script ] ( Workflow ) The variable `{0}` has been bind: {1}", zw, workflow.encode());
    }
}
