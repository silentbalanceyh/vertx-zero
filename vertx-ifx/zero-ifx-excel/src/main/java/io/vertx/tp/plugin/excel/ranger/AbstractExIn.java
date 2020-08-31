package io.vertx.tp.plugin.excel.ranger;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.excel.cell.ExValue;
import io.vertx.up.commune.element.Shape;
import io.vertx.up.log.Annal;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Objects;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;

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

    protected Object extractValue(final Cell dataCell, final Shape shape) {
        Object result;
        try {
            /* Cell value extraction based on shape */
            result = ExValue.getValue(dataCell, this.evaluator);
        } catch (final Throwable ex) {
            this.logger().jvm(ex);
            // For debug
            ex.printStackTrace();
            result = null;
        }
        return result;
    }


    protected void extractComplex(final ConcurrentMap<String, JsonArray> complexMap,
                                  final ConcurrentMap<String, JsonObject> rowMap) {
        rowMap.forEach((field, record) -> {
            JsonArray original = complexMap.get(field);
            if (Objects.isNull(original)) {
                original = new JsonArray();
            }
            original.add(record);
            complexMap.put(field, original);
        });
        /* Add only once */
        rowMap.clear();
    }

    protected BiConsumer<Cell, Shape> cellConsumer(final ConcurrentMap<String, JsonObject> rowMap,
                                                   final String parent, final String field) {
        return (dataCell, shape) -> {
            JsonObject original = rowMap.get(parent);
            if (Objects.isNull(original)) {
                original = new JsonObject();
            }
            final Object value = this.extractValue(dataCell, shape);
            original.put(field, value);
            rowMap.put(parent, original);
        };
    }
}
