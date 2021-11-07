package io.vertx.tp.ke.booter;

import io.vertx.tp.ke.cv.KeIpc;
import io.vertx.tp.plugin.booting.AbstractBoot;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class LbsOOB extends AbstractBoot {
    public LbsOOB() {
        super(KeIpc.Module.LBS);
    }
}
