package io.vertx.up.commune.wffs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class Regulation implements Serializable {

    private final List<Formula> hookOnly = new ArrayList<>();

    private final List<Formula> hookWithLog = new ArrayList<>();

    public Regulation() {
    }

    public Regulation add(final Formula formula) {
        Objects.requireNonNull(formula);
        if (formula.logging()) {
            this.hookWithLog.add(formula);
        } else {
            this.hookOnly.add(formula);
        }
        return this;
    }
}
