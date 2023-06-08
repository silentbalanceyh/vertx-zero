package io.vertx.up.uca.wffs.script;

import io.horizon.uca.log.Annal;
import io.vertx.up.uca.log.DevEnv;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractInlet implements Inlet {

    protected transient boolean isPrefix;

    protected AbstractInlet(final boolean isPrefix) {
        this.isPrefix = isPrefix;
    }

    protected String variable(final String name) {
        if (this.isPrefix) {
            return "$" + name;
        } else {
            return name;
        }
    }

    protected void console(final String message, final Object... args) {
        if (DevEnv.devExprBind()) {
            final Annal logger = Annal.get(this.getClass());
            logger.info(message, args);
        }
    }
}
