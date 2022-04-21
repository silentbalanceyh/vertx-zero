package io.vertx.up.commune.wffs;

import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class Regulation implements Serializable {
    /*
     * Rule Extract Condition
     */
    private final JsonObject criteria = new JsonObject();
    /*
     * Rule Parameters for
     * - Tpl
     * - Message
     *
     * Clone for each
     */
    private final JsonObject params = new JsonObject();

    private final List<Formula> rules = new ArrayList<>();

    public Regulation() {

    }

    public Regulation bind(final JsonObject criteria, final JsonObject params) {
        final JsonObject criteriaJ = Ut.valueJObject(criteria);
        this.criteria.mergeIn(criteriaJ, true);
        final JsonObject paramsJ = Ut.valueJObject(params);
        this.params.mergeIn(paramsJ, true);
        return this;
    }

    public Regulation add(final Formula formula) {
        this.rules.add(formula);
        return this;
    }
}
