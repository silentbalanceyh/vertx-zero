package io.vertx.tp.atom.modeling.rule;

import io.vertx.core.json.JsonArray;

import java.util.*;

interface Tool {
    /*
     * 无优先级规则
     */
    static Set<RuleTerm> ruleSet(final JsonArray data) {
        if (Objects.isNull(data)) {
            return new HashSet<>();
        } else {
            final Set<RuleTerm> term = new HashSet<>();
            data.forEach(item -> {
                if (item instanceof String) {
                    term.add(new RuleTerm((String) item));
                } else if (item instanceof JsonArray) {
                    term.add(new RuleTerm((JsonArray) item));
                }
            });
            return term;
        }
    }

    /*
     * 带优先级规则
     */
    static List<RuleTerm> ruleList(final JsonArray data) {
        if (Objects.isNull(data)) {
            return new ArrayList<>();
        } else {
            final List<RuleTerm> term = new ArrayList<>();
            data.forEach(item -> {
                if (item instanceof String) {
                    term.add(new RuleTerm((String) item));
                } else if (item instanceof JsonArray) {
                    term.add(new RuleTerm((JsonArray) item));
                }
            });
            return term;
        }
    }
}
