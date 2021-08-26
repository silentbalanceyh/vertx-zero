package io.vertx.tp.crud.refine;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.atom.KField;
import io.vertx.tp.ke.atom.KModule;
import io.vertx.up.eon.KName;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class IxData {
    private static final Annal LOGGER = Annal.get(IxData.class);

    static void audit(final JsonObject auditor, final JsonObject config, final String userId) {
        if (Objects.nonNull(config) && Ut.notNil(userId)) {
            /* User By */
            final String by = config.getString(KName.BY);
            if (Ut.notNil(by)) {
                /* Audit Process */
                IxLog.infoDao(LOGGER, "( Audit ) By -> \"{0}\" = {1}", by, userId);
                auditor.put(by, userId);
            }
            final String at = config.getString(KName.AT);
            if (Ut.notNil(at)) {
                IxLog.infoDao(LOGGER, "( Audit ) At Field -> {0}", at);
                auditor.put(at, Instant.now());
            }
        }
    }

    static boolean match(final JsonObject record, final KModule module) {
        /*
         * Get unique rule of current module
         */
        final KField fieldConfig = module.getField();
        final JsonArray matrix = fieldConfig.getUnique();
        /*
         * Matrix may be multi group
         */
        final int size = matrix.size();
        for (int idx = 0; idx < size; idx++) {
            final JsonArray group = matrix.getJsonArray(idx);
            if (Ut.notNil(group)) {
                final Set<String> fields = new HashSet<>();
                group.stream().filter(Objects::nonNull)
                        .map(item -> (String) item)
                        .filter(Ut::notNil)
                        .forEach(fields::add);
                final boolean match = fields.stream().allMatch(field -> Objects.nonNull(record.getValue(field)));
                if (!match) {
                    Ix.Log.restW(IxData.class, "Unique checking failure, check fields: `{0}`, data = {1}",
                            Ut.fromJoin(fields), record.encode());
                    return false;
                }
            }
        }
        return true;
    }
}
