package cn.originx.uca.modello;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.modular.plugin.OComponent;
import io.vertx.tp.modular.plugin.OExpression;
import io.vertx.tp.optic.business.ExAttributeComponent;
import io.vertx.up.atom.Kv;
import io.vertx.up.commune.Record;
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
    public Object after(final Kv<String, Object> kv, final Record record, final JsonObject combineData) {
        Object translated = this.translateTo(kv.getValue(), combineData);
        return this.express(Kv.create(kv.getKey(), translated), record, combineData);
    }


    public Object express(final Kv<String, Object> kv, final Record record, final JsonObject combineData) {
        List<OExpression> exprChain = new ArrayList<>();
        String field = kv.getKey();
        JsonArray sourceExpressionChain = Ut.sureJArray(combineData.getJsonArray(KName.SOURCE_EXPR_CHAIN));
        sourceExpressionChain.forEach(o -> {
            String sourceExpr = (String) o;
            exprChain.add(Ut.singleton(sourceExpr));
        });
        Object expressed = kv.getValue();
        for (OExpression expression : exprChain) {
            expressed = expression.after(Kv.create(field, expressed));
            record.attach(field, expressed);
        }
        return expressed;
    }
}
