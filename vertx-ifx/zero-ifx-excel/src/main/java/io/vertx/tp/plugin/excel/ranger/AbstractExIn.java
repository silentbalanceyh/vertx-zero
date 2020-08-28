package io.vertx.tp.plugin.excel.ranger;

import io.vertx.up.log.Annal;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public abstract class AbstractExIn implements ExIn {
    protected transient Sheet sheet;
    protected transient FormulaEvaluator evaluator;

    public AbstractExIn(final Sheet sheet) {
        this.sheet = sheet;
    }

    @Override
    public ExIn bind(final FormulaEvaluator evaluator) {
        this.evaluator = evaluator;
        return this;
    }

    protected Annal logger() {
        return Annal.get(this.getClass());
    }
}
