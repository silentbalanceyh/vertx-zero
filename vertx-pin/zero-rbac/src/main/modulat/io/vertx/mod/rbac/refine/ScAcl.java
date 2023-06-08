package io.vertx.mod.rbac.refine;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.em.EmSecure;
import io.vertx.up.specification.secure.Acl;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class ScAcl {

    static void aclRecord(final JsonObject record, final Acl acl) {
        if (Objects.nonNull(acl)) {
            acl.bind(record);
        }
    }

    static JsonArray aclOn(final JsonArray original, final Acl acl) {
        return aclProjection(original, acl, out -> EmSecure.ActPhase.EAGER == out.phase());
    }

    private static JsonArray aclProjection(final JsonArray original, final Acl acl,
                                           final Predicate<Acl> predicate) {
        final JsonArray projection = Ut.valueJArray(original);
        if (Objects.isNull(acl)) {
            /*
             * No acl, default projection defined in S_VIEW
             */
            return projection;
        } else {
            if (predicate.test(acl)) {
                /*
                 * Acl combine with projection
                 */
                return aclProjection(original, acl);
            } else {
                /*
                 * Keep the same, no happen
                 */
                return projection;
            }
        }
    }

    private static JsonArray aclProjection(final JsonArray original, final Acl acl) {
        final Set<String> aclProjection = acl.aclVisible();
        if (aclProjection.isEmpty()) {
            /*
             * acl is empty, it means no definition found in our system
             * If the acl will be modified / configured, it must contains some default fields
             * such as:
             * - active
             * - key
             * - language
             * - sigma
             */
            return original;
        } else {
            /*
             * Mix calculation
             * 1) Acl Projection is major control
             * 2) Matrix Projection is the secondary here, it means that all aclProjection must contains
             */
            final Set<String> replaced = new HashSet<>(aclProjection);
            Ut.itJArray(original, String.class, (field, index) -> {
                if (aclProjection.contains(field)) {
                    replaced.add(field);
                }
            });
            return Ut.toJArray(replaced);
        }
    }
}
