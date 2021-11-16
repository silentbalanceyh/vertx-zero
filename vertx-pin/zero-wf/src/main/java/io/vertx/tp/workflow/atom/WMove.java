package io.vertx.tp.workflow.atom;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class WMove implements Serializable {
    private transient String node;
    private transient ConcurrentMap<String, String> data = new ConcurrentHashMap<>();

    public String getNode() {
        return this.node;
    }

    public void setNode(final String node) {
        this.node = node;
    }

    public ConcurrentMap<String, String> getData() {
        return this.data;
    }

    public void setData(final ConcurrentMap<String, String> data) {
        this.data = data;
    }
}
