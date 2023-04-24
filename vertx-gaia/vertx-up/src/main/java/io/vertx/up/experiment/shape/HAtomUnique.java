package io.vertx.up.experiment.shape;

import io.aeon.experiment.mixture.HModel;
import io.aeon.experiment.rule.RuleUnique;

import java.util.Objects;

/**
 * 「Atom Internal」Atom Internal Channel Usage
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class HAtomUnique {
    private final HModel modelRef;
    /*
     * The rule unique of channel ( Running Internal )
     */
    private RuleUnique channelRule;

    public HAtomUnique(final HModel modelRef) {
        Objects.requireNonNull(modelRef);
        /* HModel Reference */
        this.modelRef = modelRef;
    }

    // 「Active」Return to the unique rule stored into model
    public RuleUnique rule() {
        return this.modelRef.rule();
    }

    // 「StandBy」Return to the unique rule that connect by Api
    public RuleUnique ruleDirect() {
        return this.channelRule;
    }

    public void connect(final RuleUnique channelRule) {
        this.channelRule = channelRule;
    }

    // ===================== Smart Method =====================
    /*
     * Here are the critical code Logical
     * 1) Search the channel rule first: Slave
     * 2) Search the model stored rule:  Master
     */
    public RuleUnique ruleSmart() {
        if (Objects.nonNull(this.channelRule)) {
            return this.channelRule;
        } else {
            return this.modelRef.rule();
        }
    }
}
