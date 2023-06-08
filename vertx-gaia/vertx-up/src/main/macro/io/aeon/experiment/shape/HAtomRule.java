package io.aeon.experiment.shape;

import io.modello.specification.atom.HModel;
import io.modello.specification.atom.HRule;

import java.util.Objects;

/**
 * 「Atom Internal」Atom Internal Channel Usage
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class HAtomRule {
    private final HModel modelRef;
    /*
     * The rule unique of channel ( Running Internal )
     */
    private HRule channelRule;

    public HAtomRule(final HModel modelRef) {
        Objects.requireNonNull(modelRef);
        /* HModel Reference */
        this.modelRef = modelRef;
    }

    // 「Active」Return to the unique rule stored into model
    public HRule rule() {
        return this.modelRef.rule();
    }

    // 「StandBy」Return to the unique rule that connect by Api
    public HRule ruleDirect() {
        return this.channelRule;
    }

    public void connect(final HRule channelRule) {
        this.channelRule = channelRule;
    }

    // ===================== Smart Method =====================
    /*
     * Here are the critical code Logical
     * 1) Search the channel rule first: Slave
     * 2) Search the model stored rule:  Master
     */
    public HRule ruleSmart() {
        if (Objects.nonNull(this.channelRule)) {
            return this.channelRule;
        } else {
            return this.modelRef.rule();
        }
    }
}
