package io.vertx.tp.workflow.uca.component;

import io.vertx.tp.workflow.atom.WMove;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractDivert implements Divert {

    private final transient ConcurrentMap<String, WMove> moveMap = new ConcurrentHashMap<>();

    @Override
    public Divert bind(final ConcurrentMap<String, WMove> moveMap) {
        if (Objects.nonNull(moveMap)) {
            this.moveMap.clear();
            this.moveMap.putAll(moveMap);
        }
        return this;
    }

    protected WMove rule(final String node) {
        return this.moveMap.getOrDefault(node, WMove.empty());
    }
}
