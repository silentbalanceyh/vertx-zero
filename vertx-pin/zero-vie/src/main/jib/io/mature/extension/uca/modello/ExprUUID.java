package io.mature.extension.uca.modello;

import io.horizon.atom.common.Kv;
import io.modello.specification.uca.OExpression;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class ExprUUID implements OExpression {
    @Override
    public Object after(final Kv<String, Object> kv) {
        if (Objects.isNull(kv.value())) {
            return null;
        } else {
            return kv.value().toString().replaceAll("-", "");
        }
    }
}
