package io.vertx.tp.modular.io;

import io.vertx.tp.atom.modeling.element.DataMatrix;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.Record;
import io.vertx.up.log.Annal;

import java.util.concurrent.ConcurrentMap;

/**
 *
 **/
class Datum {

    private static final Annal LOGGER = Annal.get(Datum.class);

    /**
     * 从 Record 拷贝数据到 Matrix
     * 输入时使用
     */
    static void connect(final Record record,
                        final ConcurrentMap<String, DataMatrix> matrixs) {
        final JsonObject data = record.toJson();
        LOGGER.info("[D] 数据输入：{0}", data.encodePrettily());
        matrixs.values().forEach(matrix -> matrix.getAttributes().forEach(attribute -> {
            final Object value = data.getValue(attribute);
            matrix.set(attribute, value);
        }));
    }

    /**
     * 从 Matrix 拷贝数据到 Record
     * 输出时使用
     */
    static Record connect(final ConcurrentMap<String, DataMatrix> matrixs,
                          final Record record) {
        matrixs.values().forEach(matrix -> matrix.getAttributes().forEach(attribute -> {
            final Object value = matrix.getValue(attribute);
            record.set(attribute, value);
        }));
        LOGGER.info("\n数据输出：{0}", record.toJson().encodePrettily());
        return record;
    }
}