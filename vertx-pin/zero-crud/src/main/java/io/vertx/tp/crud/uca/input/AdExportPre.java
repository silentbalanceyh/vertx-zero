package io.vertx.tp.crud.uca.input;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.tp.crud.uca.desk.IxMod;
import io.vertx.tp.ke.atom.specification.KField;
import io.vertx.tp.ke.atom.specification.KModule;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.optic.business.ExUser;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class AdExportPre implements Pre {
    @Override
    public Future<JsonArray> inAAsync(final JsonArray data, final IxMod in) {
        final KModule module = in.module();
        final KField field = module.getField();
        final Set<String> keys = this.inKeys(data, field);
        /*
         * Get the whole map
         */
        if (keys.isEmpty()) {
            return Ux.future(data);
        } else {
            return Ke.channel(ExUser.class, () -> data, stub -> stub.transAuditor(keys)
                .compose(map -> {
                    if (map.isEmpty()) {
                        return Ux.future(data);
                    } else {
                        /*
                         * createdBy
                         * updatedBy
                         */
                        return this.outKeys(data, field.fieldAudit(), map);
                    }
                }));
        }
    }

    private Future<JsonArray> outKeys(final JsonArray data, final Set<String> keys,
                                      final ConcurrentMap<String, String> map) {
        Ut.itJArray(data).forEach(each -> keys.forEach(field -> {
            final String value = each.getString(field);
            if (Ut.notNil(value)) {
                final String toValue = map.getOrDefault(value, value);
                each.put(field, toValue);
            }
        }));
        return Ux.future(data);
    }

    private Set<String> inKeys(final JsonArray data, final KField field) {
        final Set<String> auditSet = field.fieldAudit();
        final Set<String> idSet = new HashSet<>();
        auditSet.forEach(each -> {
            final Set<String> keys = Ut.mapString(data, each);
            idSet.addAll(keys);
        });
        return idSet;
    }
}
