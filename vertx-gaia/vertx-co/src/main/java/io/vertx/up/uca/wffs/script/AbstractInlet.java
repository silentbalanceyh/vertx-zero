package io.vertx.up.uca.wffs.script;

import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractInlet implements Inlet {
    /*
     * Capture the data of `__data` ( Specification Define )
     */
    protected ChangeFlag flag(final JsonObject data) {
        final JsonObject copy = Ut.valueJObject(data);
        final String flag = copy.getString(KName.__.FLAG, ChangeFlag.NONE.name());
        return Ut.toEnum(ChangeFlag.class, flag);
    }

    protected JsonObject data(final JsonObject data, final boolean previous) {
        final JsonObject copy = Ut.valueJObject(data).copy();
        if (previous) {
            /*
             * Previous Old Data
             */
            return Ut.valueJObject(data, KName.__.DATA);
        } else {
            copy.remove(KName.Flow.WORKFLOW);       // Remove workflow
            copy.remove(KName.USER);                // Remove user
            copy.remove(KName.__.DATA);
            copy.remove(KName.__.INPUT);
            copy.remove(KName.__.FLAG);
            return copy;
        }
    }

    protected Annal logger() {
        return Annal.get(this.getClass());
    }
}
