package io.vertx.tp.plugin.excel;

import com.fasterxml.jackson.core.type.TypeReference;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.error._404ExcelFileNullException;
import io.vertx.tp.plugin.excel.atom.ExConnect;
import io.vertx.tp.plugin.excel.atom.ExTable;
import io.vertx.tp.plugin.excel.ranger.ExBound;
import io.vertx.tp.plugin.excel.ranger.RowBound;
import io.vertx.up.commune.element.TypeAtom;
import io.vertx.up.eon.FileSuffix;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/*
 * Excel Helper to help ExcelClient to do some object building
 */
class ExcelHelper {

    private static final Map<String, Workbook> WORKBOOKS = new ConcurrentHashMap<>();
    private transient final Class<?> target;
    private transient ExTpl tpl;

    private ExcelHelper(final Class<?> target) {
        this.target = target;
    }

    static ExcelHelper helper(final Class<?> target) {
        return Fn.pool(Pool.HELPERS, target.getName(), () -> new ExcelHelper(target));
    }

    /*
     * Read file from path to build Excel Workbook object.
     * If this function get null pointer file or file object, zero system
     * will throw exception out.
     */
    @SuppressWarnings("all")
    Workbook getWorkbook(final String filename) {
        Fn.outWeb(null == filename, _404ExcelFileNullException.class, this.target, filename);
        final InputStream in = Ut.ioStream(filename);
        Fn.outWeb(null == in, _404ExcelFileNullException.class, this.target, filename);
        final Workbook workbook;
        if (filename.endsWith(FileSuffix.EXCEL_2003)) {
            workbook = Fn.pool(Pool.WORKBOOKS, filename,
                    () -> Fn.getJvm(() -> new HSSFWorkbook(in)));
        } else {
            workbook = Fn.pool(Pool.WORKBOOKS, filename,
                    () -> Fn.getJvm(() -> new XSSFWorkbook(in)));
        }
        return workbook;
    }

    @SuppressWarnings("all")
    Workbook getWorkbook(final InputStream in, final boolean isXlsx) {
        Fn.outWeb(null == in, _404ExcelFileNullException.class, this.target, "Stream");
        final Workbook workbook;
        if (isXlsx) {
            workbook = Fn.pool(Pool.WORKBOOKS_STREAM, in.hashCode(),
                    () -> Fn.getJvm(() -> new XSSFWorkbook(in)));
        } else {
            workbook = Fn.pool(Pool.WORKBOOKS_STREAM, in.hashCode(),
                    () -> Fn.getJvm(() -> new HSSFWorkbook(in)));
        }
        /* Force to recalculation for evaluator */
        workbook.setForceFormulaRecalculation(Boolean.TRUE);
        return workbook;
    }

    /*
     * Get Set<ExSheet> collection based on workbook
     */
    Set<ExTable> getExTables(final Workbook workbook, final TypeAtom TypeAtom) {
        return Fn.getNull(new HashSet<>(), () -> {
            /* FormulaEvaluator reference */
            final FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

            /*
             * Workbook pool for FormulaEvaluator
             * 1）Local variable to replace global
             **/
            final Map<String, FormulaEvaluator> references = new ConcurrentHashMap<>();
            WORKBOOKS.forEach((field, workbookRef) -> {
                /*
                 * Reference executor processing
                 * Here you must put self reference evaluator and all related here.
                 * It should fix issue: Could not set environment etc.
                 */
                final FormulaEvaluator executorRef = workbookRef.getCreationHelper().createFormulaEvaluator();
                references.put(field, executorRef);
            });
            /*
             * Self evaluator for current calculation
             */
            references.put(workbook.createName().getNameName(), evaluator);

            /*
             * Above one line code resolved following issue:
             * org.apache.poi.ss.formula.CollaboratingWorkbooksEnvironment$WorkbookNotFoundException:
             * Could not resolve external workbook name 'environment.ambient.xlsx'. Workbook environment has not been set up.
             */
            evaluator.setupReferencedWorkbooks(references);
            /*
             * Sheet process
             */
            final Iterator<Sheet> it = workbook.sheetIterator();
            final Set<ExTable> sheets = new HashSet<>();
            while (it.hasNext()) {
                /* Build temp ExSheet */
                final Sheet sheet = it.next();
                /* Build Range ( Row Start - End ) */
                final ExBound range = new RowBound(sheet);

                final SheetAnalyzer exSheet = new SheetAnalyzer(sheet).on(evaluator);
                /* Build Set */
                sheets.addAll(exSheet.analyzed(range, TypeAtom));
            }
            return sheets;
        }, workbook);
    }

    void brush(final Workbook workbook, final Sheet sheet, final TypeAtom TypeAtom) {
        if (Objects.nonNull(this.tpl)) {
            this.tpl.bind(workbook);
            this.tpl.applyStyle(sheet, TypeAtom);
        }
    }

    void initPen(final String componentStr) {
        if (Ut.notNil(componentStr)) {
            final Class<?> tplCls = Ut.clazz(componentStr, null);
            if (Ut.isImplement(tplCls, ExTpl.class)) {
                this.tpl = Ut.singleton(componentStr);
            }
        }
    }

    void initConnect(final JsonArray connects) {
        /* JsonArray serialization */
        if (Pool.CONNECTS.isEmpty()) {
            final List<ExConnect> connectList = Ut.deserialize(connects, new TypeReference<List<ExConnect>>() {
            });
            connectList.stream().filter(Objects::nonNull)
                    .filter(connect -> Objects.nonNull(connect.getTable()))
                    .forEach(connect -> Pool.CONNECTS.put(connect.getTable(), connect));
        }
    }

    void initEnvironment(final JsonArray environments) {
        environments.stream().filter(Objects::nonNull)
                .map(item -> (JsonObject) item)
                .forEach(each -> {
                    /*
                     * name for map key
                     */
                    final String key = each.getString("name");
                    /*
                     * Build reference
                     */
                    final String path = each.getString("path");
                    final Workbook workbook = this.getWorkbook(path);
                    /*
                     * Reference Evaluator
                     */
                    WORKBOOKS.put(key, workbook);
                });
    }

    /*
     * For Insert to avoid duplicated situation
     * 1. Key duplicated
     * 2. Unique duplicated
     */
    <T> List<T> compress(final List<T> input, final ExTable table) {
        final String key = table.fieldKey();
        final List<T> keyList = new ArrayList<>();
        final Set<Object> keys = new HashSet<>();
        input.forEach(item -> {
            final Object value = Ut.field(item, key);
            if (Objects.nonNull(value) && !keys.contains(value)) {
                keys.add(value);
                keyList.add(item);
            }
        });
        // Release
        return keyList;
    }
}
