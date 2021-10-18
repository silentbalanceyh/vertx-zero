package cn.originx.uca.modello;

import io.vertx.tp.modular.plugin.OExpression;
import io.vertx.up.atom.Kv;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class ExprUUID implements OExpression {
    @Override
    public Object after(final Kv<String, Object> kv) {
        if (Objects.isNull(kv.getValue())) {
            return null;
        } else {
            return kv.getValue().toString().replaceAll("-", "");
        }
    }
}
