package io.vertx.up.uca.wffs.script;

import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;
import org.apache.commons.jexl3.JexlContext;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class InletUser extends AbstractInlet {
    InletUser(final boolean isPrefix) {
        super(isPrefix);
    }

    @Override
    public void compile(final JexlContext context, final JsonObject data, final JsonObject config) {
        final JsonObject userO = this.userData(data, true);
        final String uo = this.variable("uo");
        context.set(uo, userO.getMap());
        this.logger().info("[ Script ] ( User Map ) The variable `{0}` has been bind: {1}",
            uo, userO.encode());

        final JsonObject userN = this.userData(data, false);
        final String un = this.variable("un");
        context.set(un, userO.getMap());
        this.logger().info("[ Script ] ( User Map ) The variable `{0}` has been bind: {1}",
            un, userN.encode());

        final JsonObject loData = Ut.valueJObject(userN, KName.UPDATED_BY);
        final String lo = this.variable("lo");
        context.set(lo, loData.getMap());
        this.logger().info("[ Script ] ( User Now ) The variable `{0}` has been bind: {1}",
            lo, loData.encode());
    }

    protected JsonObject userData(final JsonObject input, final boolean previous) {
        final JsonObject user = Ut.valueJObject(input, KName.__.USER).copy();
        final JsonObject userData = Ut.valueJObject(user, KName.USER);
        if (previous) {
            return Ut.valueJObject(userData, KName.__.DATA).copy();
        } else {
            final JsonObject userN = userData.copy();
            userN.remove(KName.__.DATA);
            return userN;
        }
    }
}
