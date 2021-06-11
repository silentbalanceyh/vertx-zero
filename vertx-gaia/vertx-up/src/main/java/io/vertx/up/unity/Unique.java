package io.vertx.up.unity;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.rule.RuleTerm;
import io.vertx.up.eon.Values;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.util.Ut;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class Unique {

    private Unique() {
    }

    static JsonObject ruleAll(final Collection<RuleTerm> rules, final JsonObject input) {
        final boolean isMatch = rules.stream().allMatch(term -> Objects.nonNull(term.dataMatch(input)));
        return isMatch ? input : null;
    }

    static JsonObject ruleAll(final Collection<RuleTerm> rules, final JsonObject recordO, final JsonObject recordN) {
        final boolean isMatch = rules.stream().allMatch(rule -> ruleMatch(rule, recordO, recordN));
        return isMatch ? ruleTwins(recordO, recordN) : null;
    }

    static JsonObject ruleAll(final Collection<RuleTerm> rules, final JsonArray source, final JsonObject record) {
        return Ut.itJArray(source).map(recordR -> ruleAll(rules, record, recordR))
                .filter(Objects::nonNull).findFirst().orElse(null);
    }

    static JsonObject ruleAny(final Collection<RuleTerm> rules, final JsonObject input) {
        final boolean isMatch = rules.stream().anyMatch(term -> Objects.nonNull(term.dataMatch(input)));
        return isMatch ? input : null;
    }

    static JsonObject ruleAny(final Collection<RuleTerm> rules, final JsonObject recordO, final JsonObject recordN) {
        final boolean isMatch = rules.stream().anyMatch(rule -> ruleMatch(rule, recordO, recordN));
        return isMatch ? ruleTwins(recordO, recordN) : null;
    }

    static JsonObject ruleAny(final Collection<RuleTerm> rules, final JsonArray source, final JsonObject record) {
        return Ut.itJArray(source).map(recordR -> ruleAny(rules, record, recordR))
                .filter(Objects::nonNull).findFirst().orElse(null);
    }

    static ConcurrentMap<Boolean, JsonArray> ruleAll(final Collection<RuleTerm> rules, final JsonArray input) {
        return ruleSplit(rules, input, Unique::ruleAll);
    }

    static ConcurrentMap<Boolean, JsonArray> ruleAny(final Collection<RuleTerm> rules, final JsonArray input) {
        return ruleSplit(rules, input, Unique::ruleAny);
    }

    static JsonObject ruleTwins(final JsonObject recordO, final JsonObject recordN) {
        final JsonObject data = new JsonObject();
        data.put(Values.VS.OLD, recordO);
        data.put(Values.VS.NEW, recordN);
        return data;
    }

    static JsonObject ruleTwinsJ(final JsonObject twins, final ChangeFlag flag) {
        final JsonObject oldRecord = twins.getJsonObject(Values.VS.OLD);
        final JsonObject newRecord = twins.getJsonObject(Values.VS.NEW);
        final JsonObject normalized = new JsonObject();
        if (ChangeFlag.ADD == flag) {
            normalized.mergeIn(Objects.isNull(newRecord) ? oldRecord : newRecord, true);
        } else if (ChangeFlag.DELETE == flag) {
            normalized.mergeIn(Objects.isNull(oldRecord) ? newRecord : oldRecord, true);
        } else if (ChangeFlag.UPDATE == flag) {
            normalized.mergeIn(oldRecord.copy(), true);
            normalized.mergeIn(newRecord.copy(), true);
        }
        return normalized;
    }

    private static boolean ruleMatch(final RuleTerm rule, final JsonObject recordL, final JsonObject recordR) {
        if (Objects.isNull(rule)) {
            /* Compare record directly */
            return recordL.equals(recordR);
        } else {
            /* Extract */
            final JsonObject recordLR = rule.dataRule(recordL);
            final JsonObject recordRR = rule.dataRule(recordR);
            if (Ut.isNil(recordLR) || Ut.isNil(recordRR)) {
                return false;
            } else {
                /* Compare record */
                return recordLR.equals(recordRR);
            }
        }
    }

    private static ConcurrentMap<Boolean, JsonArray> ruleSplit(final Collection<RuleTerm> rules, final JsonArray input,
                                                               final BiFunction<Collection<RuleTerm>, JsonObject, JsonObject> eachFn) {
        final JsonArray valid = new JsonArray();
        final JsonArray inValid = new JsonArray();
        Ut.itJArray(input).forEach(item -> {
            final JsonObject validItem = eachFn.apply(rules, item);
            if (Objects.isNull(validItem)) {
                inValid.add(item);
            } else {
                valid.add(validItem);
            }
        });
        return new ConcurrentHashMap<Boolean, JsonArray>() {
            {
                this.put(Boolean.TRUE, valid);
                this.put(Boolean.FALSE, inValid);
            }
        };
    }
}
