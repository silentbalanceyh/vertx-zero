package io.aeon.experiment.shape;

import io.modello.specification.atom.HModel;
import io.modello.specification.atom.HUnique;

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
    private HUnique channelRule;

    public HAtomUnique(final HModel modelRef) {
        Objects.requireNonNull(modelRef);
        /* HModel Reference */
        this.modelRef = modelRef;
    }

    // 「Active」Return to the unique rule stored into model
    public HUnique rule() {
        return this.modelRef.rule();
    }

    // 「StandBy」Return to the unique rule that connect by Api
    public HUnique ruleDirect() {
        return this.channelRule;
    }

    public void connect(final HUnique channelRule) {
        this.channelRule = channelRule;
    }

    // ===================== Smart Method =====================
    /*
     * Here are the critical code Logical
     * 1) Search the channel rule first: Slave
     * 2) Search the model stored rule:  Master
     */
    public HUnique ruleSmart() {
        if (Objects.nonNull(this.channelRule)) {
            return this.channelRule;
        } else {
            return this.modelRef.rule();
        }
    }
}
