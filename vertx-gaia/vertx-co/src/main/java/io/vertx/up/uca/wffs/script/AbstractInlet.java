package io.vertx.up.uca.wffs.script;

import io.vertx.up.log.Annal;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractInlet implements Inlet {

    protected transient boolean isPrefix = true;

    protected AbstractInlet(final boolean isPrefix) {
        this.isPrefix = isPrefix;
    }

    protected Annal logger() {
        return Annal.get(this.getClass());
    }

    protected String variable(final String name) {
        if (this.isPrefix) {
            return "$" + name;
        } else {
            return name;
        }
    }
}
