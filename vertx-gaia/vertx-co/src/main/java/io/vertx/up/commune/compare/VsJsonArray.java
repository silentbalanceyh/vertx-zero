package io.vertx.up.commune.compare;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
final class VsJsonArray extends AbstractSame {
    public VsJsonArray() {
        super(JsonArray.class);
    }

    @Override
    public boolean isAnd(final Object valueOld, final Object valueNew) {
        final JsonArray arrayOld = this.toJArray(valueOld);
        final JsonArray arrayNew = this.toJArray(valueNew);
        if (arrayNew.size() != arrayOld.size()) {
            /*
             * size is different, not the same ( Fast Checking )
             */
            return Boolean.FALSE;
        } else {
            return Ut.itJArray(arrayOld).allMatch(jsonOld -> Ut.itJArray(arrayNew).anyMatch(jsonNew -> {
                final JsonObject checkedNew = Ut.elementSubset(jsonNew, this.subset);
                final JsonObject checkedOld = Ut.elementSubset(jsonOld, this.subset);
                return checkedNew.equals(checkedOld);
            }));
        }
    }

    @Override
    public boolean isXor(final Object valueOld, final Object valueNew) {
        final Object valueSelect = Objects.isNull(valueOld) ? valueNew : valueOld;
        final JsonArray normalized;
        if (valueSelect instanceof String) {
            normalized = new JsonArray(valueSelect.toString());
        } else if (valueSelect instanceof JsonArray) {
            normalized = (JsonArray) valueSelect;
        } else {
            normalized = new JsonArray();
        }
        return normalized.isEmpty();
    }

    @Override
    public boolean ok(final Object value) {
        final boolean result = super.ok(value);
        if (result) {
            final JsonArray array = this.toJArray(value);
            return !array.isEmpty();
        } else return Boolean.FALSE;
    }

    private JsonArray toJArray(final Object valueOld) {
        if (valueOld instanceof JsonArray) {
            return (JsonArray) valueOld;
        } else {
            return new JsonArray(valueOld.toString());
        }
    }
}
