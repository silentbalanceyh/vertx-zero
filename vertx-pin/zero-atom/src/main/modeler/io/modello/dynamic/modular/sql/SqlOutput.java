package io.modello.dynamic.modular.sql;

import org.jooq.Record;
import org.jooq.Result;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class SqlOutput {

    public static List<ConcurrentMap<String, Object>> toMatrix(
        final Result<Record> queryResult,
        final String[] columns) {
        final List<ConcurrentMap<String, Object>> results = new ArrayList<>();
        queryResult.forEach(record -> {
            final ConcurrentMap<String, Object> resultRecord = new ConcurrentHashMap<>();
            Arrays.stream(columns).forEach(column -> resultRecord.put(column, null != record.getValue(column) ? record.getValue(column) : "NULL"));
            results.add(resultRecord);
        });
        return results;
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> toList(
        final Result<Record> queryResult,
        final String column
    ) {
        final List<T> results = new ArrayList<>();
        queryResult.forEach(record -> {
            final Object value = record.getValue(column);
            if (null != value) {
                results.add((T) value);
            } else {
                results.add(null);
            }
        });
        return results;
    }
}