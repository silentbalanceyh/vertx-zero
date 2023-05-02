package io.vertx.up.uca.wffs.script;

import io.horizon.eon.em.typed.ChangeFlag;
import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;
import org.apache.commons.jexl3.JexlContext;

import java.util.HashMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class InletData extends AbstractInlet {

    InletData(final boolean isPrefix) {
        super(isPrefix);
    }

    @Override
    public void compile(final JexlContext context, final JsonObject data, final JsonObject config) {
        /*
         * New Data / Old Data Checking
         * The latest data
         * {
         *
         * }
         * The old data
         * {
         *     "__data": {}
         * }
         */
        final ChangeFlag flag = Ut.aiFlag(data);
        this.console("[ Script ] ( Flag ) Execution Operation: {0}", flag);
        final String zo = this.variable("zo");
        if (ChangeFlag.ADD == flag) {
            context.set(zo, new HashMap<>());
            this.console("[ Script ] ( Data O-N ) The variable `{0}` has been empty", zo);
        } else {
            final JsonObject valueO = Ut.aiDataO(data);
            context.set(zo, Ut.toMap(valueO));
            this.console("[ Script ] ( Data OLD ) The variable `{0}` has been bind: {1}", zo, valueO.encode());
        }

        final String zn = this.variable("zn");
        if (ChangeFlag.DELETE == flag) {
            context.set(zn, new HashMap<>());
            this.console("[ Script ] ( Data N-N ) The variable `{0}` has been empty", zn);
        } else {
            final JsonObject valueN = Ut.aiDataN(data);
            context.set(zn, Ut.toMap(valueN));
            this.console("[ Script ] ( Data NEW ) The variable `{0}` has been bind: {1}", zn, valueN.encode());
        }
    }
}
