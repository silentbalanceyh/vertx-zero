package io.vertx.tp.workflow.atom;

import cn.zeroup.macrocosm.cv.em.TodoCase;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.em.ChangeFlag;

import java.io.Serializable;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class ConfigRecord implements Serializable {
    private transient String unique = KName.KEY;
    private transient String indent;
    private transient ChangeFlag flag;
    private transient TodoCase mode = TodoCase.DAO;

    public String getIndent() {
        return this.indent;
    }

    public void setIndent(final String indent) {
        this.indent = indent;
    }

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

    public String unique(final JsonObject params) {
        return params.getString(this.unique, null);
    }
}
