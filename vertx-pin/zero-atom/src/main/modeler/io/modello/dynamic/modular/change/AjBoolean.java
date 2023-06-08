package io.modello.dynamic.modular.change;

import io.vertx.core.json.JsonObject;

import java.util.Objects;

final class AjBoolean extends AbstractAdjuster {

    public AjBoolean() {
        super(Boolean.class);
    }

    @Override
    public Object inValue(final Object ucmdbInput) {
        final String liberal = this.literal(ucmdbInput);
        if (Objects.isNull(liberal)) {
            return Boolean.FALSE;
        } else {
            final JsonObject mapping = this.configIn();
            final String key = liberal.toLowerCase();
            if (mapping.containsKey(key)) {
                return mapping.getBoolean(key);
            } else {
                return Boolean.FALSE;
            }
        }
    }

    @Override
    public Object outValue(final Object input) {
        final Boolean bool = Objects.isNull(input) ? Boolean.FALSE : (Boolean) input;
        /*
         * 读取值
         */
        final JsonObject mapping = this.configOut();
        if (mapping.containsKey(bool.toString())) {
            return mapping.getString(bool.toString());
        } else {
            return bool.toString();
        }
    }

    @Override
    public boolean isSame(final Object oldValue, final Object newValue) {
        final JsonObject mapping = this.configIn();
        return this.isSame(oldValue, newValue,
            /*
             * 基本比较
             */
            () -> {
                /*
                 * 使用 newValue 读取
                 */
                final String normNew = newValue.toString().trim();
                if (mapping.containsKey(normNew.toLowerCase())) {
                    final Boolean newConvert = mapping.getBoolean(normNew.toLowerCase());
                    return newConvert.equals(oldValue);
                } else {
                    return oldValue.toString().trim()
                        .equalsIgnoreCase(normNew);
                }
            });
    }
}
