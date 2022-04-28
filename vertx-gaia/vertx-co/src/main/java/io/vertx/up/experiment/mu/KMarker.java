package io.vertx.up.experiment.mu;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class KMarker implements Serializable {

    private final boolean trackable;

    private final ConcurrentMap<String, KTag> tagMap = new ConcurrentHashMap<>();

    public KMarker() {
        this(Boolean.TRUE);
    }

    public KMarker(final Boolean trackable) {
        this.trackable = trackable;
    }

    public boolean trackable() {
        return this.trackable;
    }

    public void put(final String name, final KTag tag) {
        this.tagMap.put(name, tag);
    }

    public void put(final String name, final String literal) {
        this.tagMap.put(name, new KTag(literal));
    }

    public KTag tag(final String name) {
        return this.tagMap.getOrDefault(name, new KTag());
    }

    // track
    public Set<String> onTrack() {
        return this.connect(KTag::isTrack, Boolean.TRUE);
    }

    public Set<String> offTrack() {
        return this.connect(KTag::isTrack, Boolean.FALSE);
    }

    // active
    public Set<String> onActive() {
        return this.connect(KTag::isActive, Boolean.TRUE);
    }

    public Set<String> offActive() {
        return this.connect(KTag::isActive, Boolean.FALSE);
    }

    // lock
    public Set<String> onLock() {
        return this.connect(KTag::isLock, Boolean.TRUE);
    }

    public Set<String> offLock() {
        return this.connect(KTag::isLock, Boolean.FALSE);
    }


    // confirm
    public Set<String> onConfirm() {
        return this.connect(KTag::isConfirm, Boolean.TRUE);
    }

    public Set<String> offConfirm() {
        return this.connect(KTag::isConfirm, Boolean.FALSE);
    }


    // syncIn
    public Set<String> onIn() {
        return this.connect(KTag::isSyncIn, Boolean.TRUE);
    }

    public Set<String> offIn() {
        return this.connect(KTag::isSyncIn, Boolean.FALSE);
    }

    // syncOut
    public Set<String> onOut() {
        return this.connect(KTag::isSyncOut, Boolean.TRUE);
    }

    public Set<String> offOut() {
        return this.connect(KTag::isSyncOut, Boolean.FALSE);
    }

    // array
    public Set<String> onArray() {
        return this.connect(KTag::isArray, Boolean.TRUE);
    }

    public Set<String> offArray() {
        return this.connect(KTag::isArray, Boolean.FALSE);
    }

    // refer
    public Set<String> onRefer() {
        return this.connect(KTag::isRefer, Boolean.TRUE);
    }

    public Set<String> offRefer() {
        return this.connect(KTag::isRefer, Boolean.FALSE);
    }

    private Set<String> connect(final Function<KTag, Boolean> function, final Boolean defaultV) {
        final Set<String> set = new HashSet<>();
        this.tagMap.keySet().forEach(field -> {
            final KTag tag = this.tagMap.get(field);
            if (Objects.nonNull(tag)) {
                final Boolean result = function.apply(tag);
                if (Objects.nonNull(result) && defaultV.booleanValue() == result.booleanValue()) {
                    // Skip all NULL value
                    set.add(field);
                }
            }
        });
        return set;
    }
}
