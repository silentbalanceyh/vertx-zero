package io.vertx.tp.plugin.excel.cell;

import io.vertx.up.util.Ut;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

interface Pool {

    ConcurrentMap<String, ExValue> VALUE_MAP = new ConcurrentHashMap<String, ExValue>() {
        {
            this.put(Literal.UUID, Ut.singleton(UuidValue.class));
        }
    };

    ConcurrentMap<String, ExValue> PREFIX_MAP = new ConcurrentHashMap<String, ExValue>() {
        {
            this.put(Literal.Prefix.JSON, Ut.singleton(JsonValue.class));
        }
    };

    ConcurrentMap<CellType, Function<Cell, Object>> FUNS
            = new ConcurrentHashMap<CellType, Function<Cell, Object>>() {
        {
            this.put(CellType.STRING, cell -> {
                if (CellType.NUMERIC == cell.getCellType()) {
                    /*
                     * Fix issue of user operation:
                     * Cannot get a STRING value from a NUMERIC cell
                     */
                    return String.valueOf(cell.getNumericCellValue());
                } else {
                    return cell.getStringCellValue();
                }
            });
            this.put(CellType.BOOLEAN, Cell::getBooleanCellValue);
            this.put(CellType.NUMERIC, DateValue::toNumeric);
        }
    };
}

interface Literal {
    String UUID = "{UUID}";

    interface Prefix {
        String JSON = "JSON";
    }
}
