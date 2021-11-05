package io.vertx.tp.ke.booter;

import io.vertx.tp.ke.cv.KeIpc;
import io.vertx.tp.plugin.excel.booter.AbstractBoot;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AmbientOOB extends AbstractBoot {
    public AmbientOOB() {
        super(KeIpc.Module.AMBIENT);
    }
}
