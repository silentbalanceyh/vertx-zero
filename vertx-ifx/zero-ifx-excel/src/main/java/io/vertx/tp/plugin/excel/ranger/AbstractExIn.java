package io.vertx.tp.plugin.excel.ranger;

import io.horizon.uca.log.Annal;
import io.modello.atom.normalize.MetaAtom;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.plugin.excel.atom.ExRecord;
import io.vertx.tp.plugin.excel.cell.ExValue;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Objects;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
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

    protected Object extractValue(final Cell dataCell, final Class<?> type) {
        Object result;
        try {
            /* Cell value extraction based on shape */
            result = ExValue.getValue(dataCell, type, this.evaluator);
        } catch (final Throwable ex) {
            this.logger().fatal(ex);
            // For debug
            ex.printStackTrace();
            result = null;
        }
        return result;
    }

    /*
     * Merge `eachMap` to `dataMap`
     */
    protected void extractComplex(final ConcurrentMap<String, JsonArray> complexMap,
                                  final ConcurrentMap<String, JsonObject> rowMap) {
        rowMap.forEach((field, record) -> {
            JsonArray original = complexMap.get(field);
            if (Objects.isNull(original)) {
                original = new JsonArray();
            }
            if (!ExRecord.isEmpty(record)) {
                original.add(record);
            }
            complexMap.put(field, original);
        });
        /* Add only once */
        rowMap.clear();
    }

    protected BiConsumer<Cell, MetaAtom> cellConsumer(final ConcurrentMap<String, JsonObject> rowMap,
                                                      final String field) {
        return (dataCell, shape) -> {
            /*
             * Calculated
             */
            final String[] fields = field.split("\\.");
            final String parent = fields[0];
            final String child = fields[1];
            /*
             * Do Processing
             */
            JsonObject original = rowMap.get(parent);
            if (Objects.isNull(original)) {
                original = new JsonObject();
            }
            final Class<?> type = shape.type(parent, child);
            final Object value = this.extractValue(dataCell, type);
            original.put(child, value);
            rowMap.put(parent, original);
        };
    }
}
