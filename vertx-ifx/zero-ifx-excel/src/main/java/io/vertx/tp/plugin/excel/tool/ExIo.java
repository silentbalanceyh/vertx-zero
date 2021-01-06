package io.vertx.tp.plugin.excel.tool;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.Strings;
import io.vertx.up.util.Ut;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

import java.math.BigDecimal;
import java.time.*;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class ExIo {
    private static final ConcurrentMap<Class<?>, CellType> TYPE_MAP = new ConcurrentHashMap<Class<?>, CellType>() {
        private static final long serialVersionUID = 3360878434696227455L;

        {
            this.put(String.class, CellType.STRING);
            this.put(char.class, CellType.STRING);
            this.put(Instant.class, CellType.NUMERIC);
            this.put(LocalDate.class, CellType.NUMERIC);
            this.put(LocalDateTime.class, CellType.NUMERIC);
            this.put(BigDecimal.class, CellType.NUMERIC);
            this.put(Integer.class, CellType.NUMERIC);
            this.put(int.class, CellType.NUMERIC);
            this.put(Long.class, CellType.NUMERIC);
            this.put(long.class, CellType.NUMERIC);
            this.put(Short.class, CellType.NUMERIC);
            this.put(short.class, CellType.NUMERIC);
            this.put(boolean.class, CellType.BOOLEAN);
            this.put(Boolean.class, CellType.BOOLEAN);
            this.put(JsonArray.class, CellType.STRING);
            this.put(JsonObject.class, CellType.STRING);
        }
    };
    private static final ConcurrentMap<Class<?>, BiConsumer<Cell, Object>> VALUE_MAP =
            new ConcurrentHashMap<Class<?>, BiConsumer<Cell, Object>>() {
                private static final long serialVersionUID = 5714357194532329854L;

                {
                    this.put(String.class, ExIo::outString);
                    this.put(char.class, ExIo::outString);
                    this.put(Instant.class, ExIo::outDate);
                    this.put(LocalDate.class, ExIo::outLocalDate);
                    this.put(LocalDateTime.class, ExIo::outLocalDateTime);
                    this.put(BigDecimal.class, ExIo::outBigDecimal);
                    this.put(Integer.class, ExIo::outNumeric);
                    this.put(int.class, ExIo::outNumeric);
                    this.put(Long.class, ExIo::outNumeric);
                    this.put(long.class, ExIo::outNumeric);
                    this.put(Short.class, ExIo::outNumeric);
                    this.put(short.class, ExIo::outNumeric);
                    this.put(boolean.class, ExIo::outBoolean);
                    this.put(Boolean.class, ExIo::outBoolean);
                    this.put(JsonArray.class, ExIo::outString);
                    this.put(JsonObject.class, ExIo::outString);
                }
            };

    /*
     * setCellValue(String)
     */
    private static void outString(final Cell cell, final Object value) {
        cell.setCellValue(value.toString());
    }

    /*
     * setCellValue(Date)
     */
    private static void outDate(final Cell cell, final Object value) {
        final Date date = Ut.parseFull(value.toString());
        cell.setCellValue(date);
    }

    /*
     * setCellValue(LocalDate)
     */
    private static void outLocalDate(final Cell cell, final Object value) {
        final LocalDate date = outLocalDateTime(value.toString()).toLocalDate();
        cell.setCellValue(date);
    }

    /*
     * setCellValue(LocalDateTime)
     */
    private static void outLocalDateTime(final Cell cell, final Object value) {
        final LocalDateTime dateTime = outLocalDateTime(value.toString());
        cell.setCellValue(dateTime);
    }

    private static LocalDateTime outLocalDateTime(final String value) {
        final LocalDateTime dateTime = Ut.toDateTime(value.toString());
        // 修正
        return LocalDateTime.ofInstant(dateTime.toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
    }

    /*
     * setCellValue(double)
     */
    private static void outBigDecimal(final Cell cell, final Object value) {
        final String liberal = value.toString();
        if (Ut.isInteger(liberal) || Ut.isDecimal(liberal)) {
            final BigDecimal decimal = new BigDecimal(value.toString());
            cell.setCellValue(decimal.doubleValue());
        } else {
            // Number format of empty
            cell.setCellValue(liberal);
        }
    }

    /*
     * setCellValue(double)
     */
    private static void outNumeric(final Cell cell, final Object value) {
        final String liberal = value.toString();
        if (Ut.isInteger(liberal) || Ut.isDecimal(liberal)) {
            final double parsed = Double.parseDouble(liberal);
            cell.setCellValue(parsed);
        } else {
            // Number format of empty
            cell.setCellValue(liberal);
        }
    }

    /*
     * setBoolean(boolean)
     */
    private static void outBoolean(final Cell cell, final Object value) {
        final String literal = value.toString();
        if ("true".equalsIgnoreCase(literal) || "false".equalsIgnoreCase(literal)) {
            /*
             * Standard Processing
             */
            if ("true".equalsIgnoreCase(literal)) {
                cell.setCellValue(Boolean.TRUE);
            } else {
                cell.setCellValue(Boolean.FALSE);
            }
        } else {
            /*
             * Yes, No
             * 是, 否
             */
            cell.setCellValue(literal);
        }
    }

    // --------------------- Api that will be called ----------------------
    /*
     * Process for output to excel file
     * These apis will be used in exporting workflow
     */
    public static CellType type(final Class<?> input) {
        return TYPE_MAP.getOrDefault(input, CellType.STRING);
    }

    static void value(final Cell cell, final Class<?> type, final Object value) {
        /*
         * Terminal for empty
         */
        if (Objects.isNull(value)) {
            cell.setCellValue(Strings.EMPTY);
            return;
        }
        final BiConsumer<Cell, Object> consumer = VALUE_MAP.getOrDefault(type, null);
        if (Objects.isNull(consumer)) {
            /*
             *
             */
            cell.setCellValue(value.toString());
        } else {
            /*
             * Bi consumer
             */
            consumer.accept(cell, value);
        }
    }
}
