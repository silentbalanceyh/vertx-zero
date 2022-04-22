package io.vertx.up.commune.wffs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class Regulation implements Serializable {

    private final List<Formula> formulas = new ArrayList<>();

    public Regulation() {
    }

    public Regulation add(final Formula formula) {
        Objects.requireNonNull(formula);
        this.formulas.add(formula);
        return this;
    }

    public Formula find(final String key) {
        Objects.requireNonNull(key);
        return this.formulas.stream()
            .filter(formula -> key.equals(formula.key()))
            .findAny().orElse(null);
    }
}
