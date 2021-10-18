package io.vertx.tp.crud.refine;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.init.IxPin;
import io.vertx.tp.crud.uca.desk.IxMod;
import io.vertx.tp.ke.atom.specification.KField;
import io.vertx.tp.ke.atom.specification.KModule;
import io.vertx.up.atom.Kv;
import io.vertx.up.eon.Constants;
import io.vertx.up.eon.KName;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

import java.text.MessageFormat;
import java.time.Instant;
import java.util.Objects;

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

    static Kv<String, HttpMethod> impact(final IxMod in) {
        final KModule module = in.module();
        final String pattern = "/api/{0}/search";
        final String actor = module.getName();
        return Kv.create(MessageFormat.format(pattern, actor), HttpMethod.POST);
    }

    static Kv<String, String> field(final Object value) {
        if (Constants.DEFAULT_HOLDER.equals(value)) {
            return null;
        }
        final String field;
        final String fieldValue;
        if (value instanceof String) {
            // metadata
            field = value.toString().split(",")[0];
            fieldValue = value.toString().split(",")[1];
        } else {
            final JsonObject column = (JsonObject) value;
            if (column.containsKey(KName.METADATA)) {
                // metadata
                final String metadata = column.getString(KName.METADATA);
                if (Ut.notNil(metadata)) {
                    field = metadata.split(",")[0];
                    fieldValue = value.toString().split(",")[1];
                } else {
                    field = null;
                    fieldValue = null;
                }
            } else {
                // dataIndex
                field = column.getString(IxPin.getColumnKey());
                fieldValue = column.getString(IxPin.getColumnLabel());
            }
        }
        if (Objects.nonNull(field) && Objects.nonNull(fieldValue)) {
            return Kv.create(field, fieldValue);
        } else {
            return null;
        }
    }

    static JsonArray matrix(final KField field) {
        final JsonArray priority = new JsonArray();
        final String keyField = field.getKey();
        /*
         * Add key into group as the high est priority
         */
        priority.add(new JsonArray().add(keyField));
        final JsonArray matrix = Ut.sureJArray(field.getUnique());
        priority.addAll(matrix);
        return priority;
    }

    static JsonObject parameters(final IxMod in) {
        /*
         * module seeking
         * 1. Checking connect module to see whether it's defined in crud configuration
         * 2. When it's null, ( Half Processing )
         *      -- Check the `module` parameters first
         * 3. The last part is current `module` identifier ( such as `tabular` )
         */
        final JsonObject parameters = in.parameters();
        if (!parameters.containsKey(KName.MODULE)) {
            final KModule module = in.module();
            final KModule connect = in.connect();
            if (Objects.isNull(connect)) {
                parameters.put(KName.MODULE, module.getIdentifier());
            } else {
                parameters.put(KName.MODULE, connect.getIdentifier());
            }
        }
        return parameters;
    }
}
