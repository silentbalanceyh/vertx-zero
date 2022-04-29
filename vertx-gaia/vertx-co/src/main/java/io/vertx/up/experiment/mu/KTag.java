package io.vertx.up.experiment.mu;

import io.vertx.up.eon.Strings;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.Objects;

/**
 * Processing for Attribute
 * 1. active      ( Configured )
 * 2. track       ( Configured )
 * 3. lock        ( Configured )
 * 4. confirm     ( Configured )
 * 5. array       ( Configured )
 * 6. syncIn      ( Configured )
 * 7. syncOut     ( Configured )
 * 8. refer       ( Configured + Auto )
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class KTag implements Serializable {
    private static final Boolean[] DEFAULT_VALUE = new Boolean[]{
        // active, track, lock, confirm
        true, true, false, true,
        // array, syncIn, syncOut, refer
        false, true, true, false
    };
    private final Boolean active;
    private final Boolean track;
    private final Boolean lock;
    private final Boolean confirm;
    private final Boolean array;
    private final Boolean syncIn;
    private final Boolean syncOut;
    private final Boolean refer;

    public KTag() {
        this(Strings.EMPTY);
    }

    public KTag(final String literal) {
        // X,X,X,X,X,X,X,X
        final Boolean[] normalized;
        if (Ut.isNil(literal)) {
            // Default：1,1,0,0,0,1,1,0
            normalized = DEFAULT_VALUE;
        } else {
            normalized = this.initialize(literal);
        }
        assert 8 == normalized.length : "Length must be 8";
        this.active = normalized[0];
        this.track = normalized[1];
        this.lock = normalized[2];
        this.confirm = normalized[3];
        this.array = normalized[4];
        this.syncIn = normalized[5];
        this.syncOut = normalized[6];
        this.refer = normalized[7];
    }

    private Boolean[] initialize(final String literal) {
        // Split
        final String[] parsed = literal.split(Strings.COMMA);
        final Boolean[] result = new Boolean[8];
        final int actual = parsed.length;
        for (int i = 0; i < 8; i++) {
            // If i is invalid
            if (i < (actual - 1)) {
                final String item = parsed[i];
                if (Objects.isNull(item) || "NULL".equals(item)) {
                    // null for default value.
                    result[i] = null;
                } else if ("1".equals(item)) {
                    // Because parseBoolean / valueOf will be false, here must support 1 = true
                    result[i] = true;
                } else {
                    // Other string will start parsing workflow ( standard )
                    result[i] = Boolean.parseBoolean(parsed[i]);
                }
            } else {
                // null for default value.
                result[i] = DEFAULT_VALUE[i];
            }
        }
        return result;
    }

    public boolean isActive() {
        return this.active;
    }

    public boolean isTrack() {
        return this.track;
    }

    public boolean isLock() {
        return this.lock;
    }

    public boolean isConfirm() {
        return this.confirm;
    }

    public boolean isArray() {
        return this.array;
    }

    public boolean isSyncIn() {
        return this.syncIn;
    }

    public boolean isSyncOut() {
        return this.syncOut;
    }

    public boolean isRefer() {
        return this.refer;
    }

    @Override
    public String toString() {
        return "KTag{" +
            "active=" + this.active +
            ", track=" + this.track +
            ", lock=" + this.lock +
            ", confirm=" + this.confirm +
            ", array=" + this.array +
            ", syncIn=" + this.syncIn +
            ", syncOut=" + this.syncOut +
            ", refer=" + this.refer +
            '}';
    }
}
