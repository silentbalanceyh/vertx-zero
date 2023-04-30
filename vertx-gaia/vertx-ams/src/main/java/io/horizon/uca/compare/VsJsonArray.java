package io.horizon.uca.compare;

import io.horizon.util.HaS;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Objects;
import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
final class VsJsonArray extends AbstractSame {
    public VsJsonArray() {
        super(JsonArray.class);
    }

    @Override
    public boolean isAnd(final Object valueOld, final Object valueNew) {
        final String valueOStr = valueOld.toString();
        final String valueNStr = valueNew.toString();
        if (HaS.isJArray(valueOStr) && HaS.isJArray(valueNStr)) {
            final JsonArray arrayOld = this.toJArray(valueOld);
            final JsonArray arrayNew = this.toJArray(valueNew);
            if (arrayNew.size() != arrayOld.size()) {
                /*
                 * size is different, not the same ( Fast Checking )
                 */
                return Boolean.FALSE;
            } else {
                /*
                 * java.lang.NullPointerException
                 * at io.horizon.uca.compare.VsJsonArray.isAnd(VsJsonArray.java:31)
                 */
                if (Objects.isNull(this.fieldType)) {
                    return Objects.equals(valueOld, valueNew);
                } else {
                    final Set<String> diffSet = this.fieldType.ruleUnique();
                    return HaS.itJArray(arrayOld).allMatch(jsonOld -> HaS.itJArray(arrayNew).anyMatch(jsonNew -> {
                        final JsonObject checkedNew = HaS.elementSubset(jsonNew, diffSet);
                        final JsonObject checkedOld = HaS.elementSubset(jsonOld, diffSet);
                        return checkedNew.equals(checkedOld);
                    }));
                }
            }
        } else {
            return valueOStr.equals(valueNStr);
        }
    }

    @Override
    public boolean isXor(final Object valueOld, final Object valueNew) {
        final Object valueSelect = Objects.isNull(valueOld) ? valueNew : valueOld;
        final JsonArray normalized;
        if (valueSelect instanceof String) {
            normalized = this.toJArray(valueSelect);
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
        } else {
            return Boolean.FALSE;
        }
    }

    private JsonArray toJArray(final Object value) {
        if (value instanceof JsonArray) {
            return (JsonArray) value;
        } else if (HaS.isJArray(value)) {
            return new JsonArray(value.toString());
        } else {
            return new JsonArray().add(value);
        }
    }
}
