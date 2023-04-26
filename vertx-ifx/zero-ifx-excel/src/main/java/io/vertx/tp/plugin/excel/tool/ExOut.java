package io.vertx.tp.plugin.excel.tool;

import io.horizon.eon.VString;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
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
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class ExOut {
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
                this.put(String.class, ExOut::outString);
                this.put(char.class, ExOut::outString);
                this.put(Instant.class, ExOut::outDate);
                this.put(LocalDate.class, ExOut::outLocalDate);
                this.put(LocalDateTime.class, ExOut::outLocalDateTime);
                this.put(BigDecimal.class, ExOut::outBigDecimal);
                this.put(Integer.class, ExOut::outNumeric);
                this.put(int.class, ExOut::outNumeric);
                this.put(Long.class, ExOut::outNumeric);
                this.put(long.class, ExOut::outNumeric);
                this.put(Short.class, ExOut::outNumeric);
                this.put(short.class, ExOut::outNumeric);
                this.put(boolean.class, ExOut::outBoolean);
                this.put(Boolean.class, ExOut::outBoolean);
                this.put(JsonArray.class, ExOut::outString);
                this.put(JsonObject.class, ExOut::outString);
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
        final LocalDate date = Ut.toDate(value.toString());
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
        final LocalDateTime dateTime = Ut.toDateTime(value);
        // 修正
        if (Objects.isNull(dateTime)) {
            return LocalDateTime.MIN;   // MIN instead of null value here.
        }
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
            cell.setCellValue(VString.EMPTY);
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
