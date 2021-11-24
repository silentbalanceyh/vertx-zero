package io.vertx.tp.ke.booter;

import io.vertx.tp.ke.cv.KeIpc;
import io.vertx.tp.plugin.booting.AbstractBoot;
import io.vertx.tp.workflow.init.WfPin;

import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class WfOOB extends AbstractBoot {
    public WfOOB() {
        super(KeIpc.Module.WF);
    }

    @Override
    protected Set<String> configureBuiltIn() {
        return WfPin.getBuiltIn();
    }
}
