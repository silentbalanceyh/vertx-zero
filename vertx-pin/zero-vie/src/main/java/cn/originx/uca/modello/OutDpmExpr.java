package cn.originx.uca.modello;

import io.horizon.atom.common.Kv;
import io.modello.specification.HRecord;
import io.horizon.spi.component.ExAttributeComponent;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.modular.plugin.OComponent;
import io.vertx.tp.modular.plugin.OExpression;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class OutDpmExpr extends ExAttributeComponent implements OComponent {

    /**
     * 1. 翻译
     * 2. 执行表达式
     * 3. 执行sourceNorm内容
     */
    @Override
    public Object after(final Kv<String, Object> kv, final HRecord record, final JsonObject combineData) {
        final Object translated = this.translateTo(kv.value(), combineData);
        return this.express(Kv.create(kv.key(), translated), record, combineData);
    }


    public Object express(final Kv<String, Object> kv, final HRecord record, final JsonObject combineData) {
        final List<OExpression> exprChain = new ArrayList<>();
        final String field = kv.key();
        final JsonArray sourceExpressionChain = Ut.valueJArray(combineData.getJsonArray(KName.SOURCE_EXPR_CHAIN));
        sourceExpressionChain.forEach(o -> {
            final String sourceExpr = (String) o;
            exprChain.add(Ut.singleton(sourceExpr));
        });
        Object expressed = kv.value();
        for (final OExpression expression : exprChain) {
            expressed = expression.after(Kv.create(field, expressed));
            record.attach(field, expressed);
        }
        return expressed;
    }
}
