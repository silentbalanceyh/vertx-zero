package io.vertx.up.uca.wffs.script;

import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;
import org.apache.commons.jexl3.JexlContext;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class InletUser extends AbstractInlet {
    @Override
    public void compile(final JexlContext context, final JsonObject data, final JsonObject config) {
        final JsonObject user = Ut.valueJObject(data, KName.USER).copy();
        context.set("$zu", user.getMap());
        this.logger().info("[ Script ] ( User Map ) The variable `$zu` has been bind: {0}", user.encode());

        final JsonObject lo = Ut.valueJObject(user, KName.UPDATED_BY);
        context.set("$lo", lo.getMap());
        this.logger().info("[ Script ] ( User ) The variable `$lo` has been bind: {0}", user.encode());

    }
}
