package io.vertx.tp.plugin.shell.atom;

import java.io.Serializable;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class CommandArgs implements Serializable {
    private transient String name;

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
