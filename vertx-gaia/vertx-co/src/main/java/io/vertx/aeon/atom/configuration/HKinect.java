package io.vertx.aeon.atom.configuration;

import io.vertx.aeon.eon.em.AeonMode;

import java.io.Serializable;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class HKinect implements Serializable {
    /*
     * 默认使用公有模式
     */
    private AeonMode mode = AeonMode.MIN;

    public AeonMode getMode() {
        return this.mode;
    }

    public void setMode(final AeonMode mode) {
        this.mode = mode;
    }

}
