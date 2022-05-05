package io.vertx.up.uca.wffs.script;

import io.vertx.up.log.Annal;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractInlet implements Inlet {

    protected Annal logger() {
        return Annal.get(this.getClass());
    }
}
