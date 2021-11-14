package io.vertx.tp.workflow.atom.element;

import cn.zeroup.macrocosm.cv.em.TodoCase;
import io.vertx.up.eon.em.ChangeFlag;

import java.io.Serializable;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class WRecord implements Serializable {
    private transient String unique;
    private transient ChangeFlag flag;
    private transient TodoCase mode = TodoCase.DAO;

    public String getUnique() {
        return this.unique;
    }

    public void setUnique(final String unique) {
        this.unique = unique;
    }

    public ChangeFlag getFlag() {
        return this.flag;
    }

    public void setFlag(final ChangeFlag flag) {
        this.flag = flag;
    }

    public TodoCase getMode() {
        return this.mode;
    }

    public void setMode(final TodoCase mode) {
        this.mode = mode;
    }
}
