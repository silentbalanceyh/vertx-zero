package io.vertx.tp.plugin.excel;

import com.fasterxml.jackson.core.type.TypeReference;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.error._404ExcelFileNullException;
import io.vertx.tp.plugin.booting.KBoot;
import io.vertx.tp.plugin.booting.KConnect;
import io.vertx.tp.plugin.excel.atom.ExRecord;
import io.vertx.tp.plugin.excel.atom.ExTable;
import io.vertx.tp.plugin.excel.atom.ExTenant;
import io.vertx.tp.plugin.excel.ranger.ExBound;
import io.vertx.tp.plugin.excel.ranger.RowBound;
import io.vertx.up.eon.FileSuffix;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Strings;
import io.vertx.up.experiment.mixture.HTAtom;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.cache.Cc;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/*
 * Excel Helper to help ExcelClient to do some object building
 */
class ExcelHelper {

    private static final Cc<String, ExcelHelper> CC_HELPER = Cc.open();
    private static final Cc<String, Workbook> CC_WORKBOOK = Cc.open();
    private static final Cc<Integer, Workbook> CC_WORKBOOK_STREAM = Cc.open();
    private static final Map<String, Workbook> REFERENCES = new ConcurrentHashMap<>();
    private transient final Class<?> target;
    ConcurrentMap<String, KConnect> CONNECTS = new ConcurrentHashMap<>();
    private transient ExTpl tpl;
    private transient ExTenant tenant;

    private ExcelHelper(final Class<?> target) {
        this.target = target;
    }

    static ExcelHelper helper(final Class<?> target) {
        return CC_HELPER.pick(() -> new ExcelHelper(target), target.getName());
        // Fn.po?l(Pool.HELPERS, target.getName(), () -> new ExcelHelper(target));
    }

    Future<JsonArray> extract(final Set<ExTable> tables) {
        final List<Future<JsonArray>> futures = new ArrayList<>();
        tables.forEach(table -> futures.add(this.extract(table)));
        return Fn.compressA(futures);
    }

    /*
     * {
     *      "source",
     *      "mapping"
     * }
     */
    private Future<JsonArray> extractDynamic(final JsonArray dataArray, final String name) {
        /* Source Processing */
        if (Objects.isNull(this.tenant)) {
            return Ux.future(dataArray);
        } else {
            // Append Global
            Ut.itJArray(dataArray)
                .forEach(json -> json.mergeIn(this.tenant.valueDefault(), true));
            // Extract Mapping
            final ConcurrentMap<String, String> first = this.tenant.dictionaryDefinition(name);
            if (first.isEmpty()) {
                return Ux.future(dataArray);
            } else {
                // Directory
                return this.tenant.dictionary().compose(dataMap -> {
                    if (!dataMap.isEmpty()) {
                        /*
                         * mapping
                         * field = name
                         * dataMap
                         * name = JsonObject ( from = to )
                         * --->
                         *
                         * field -> JsonObject
                         */
                        final ConcurrentMap<String, JsonObject> combine
                            = Ut.elementZip(first, dataMap);

                        combine.forEach((key, value) -> Ut.itJArray(dataArray).forEach(json -> {
                            final String fromValue = json.getString(key);
                            if (Ut.notNil(fromValue) && value.containsKey(fromValue)) {
                                final Object toValue = value.getValue(fromValue);
                                // Replace
                                json.put(key, toValue);
                            }
                        }));
                    }
                    return Ux.future(dataArray);
                });
            }
        }
    }

    /*
     * static
     * {
     *      "dictionary"
     * }
     */
    private JsonArray extractStatic(final JsonArray dataArray, final String name) {
        final ConcurrentMap<String, ConcurrentMap<String, String>> tree = this.tenant.tree(name);
        if (!tree.isEmpty()) {
            tree.forEach((field, map) -> Ut.itJArray(dataArray).forEach(record -> {
                final String input = record.getString(field);
                if (map.containsKey(input)) {
                    final String output = map.get(input);
                    record.put(field, output);
                }
            }));
        }
        return dataArray;
    }

    private Future<JsonArray> extractForbidden(final JsonArray dataArray, final String name) {
        final ConcurrentMap<String, Set<String>> forbidden = this.tenant.valueCriteria(name);
        if (forbidden.isEmpty()) {
            return Ux.future(dataArray);
        } else {
            final JsonArray normalized = new JsonArray();
            Ut.itJArray(dataArray).filter(item -> forbidden.keySet().stream().allMatch(field -> {
                if (item.containsKey(field)) {
                    final Set<String> values = forbidden.get(field);
                    final String value = item.getString(field);
                    return !values.contains(value);
                } else {
                    return true;
                }
            })).forEach(normalized::add);
            return Ux.future(normalized);
        }
    }

    Future<JsonArray> extract(final ExTable table) {
        /* Records extracting */
        final List<ExRecord> records = table.get();
        final String tableName = table.getName();
        /* Pojo Processing */
        final JsonArray dataArray = new JsonArray();
        records.stream().filter(Objects::nonNull)
            .map(ExRecord::toJson)
            .forEach(dataArray::add);

        /* dictionary for static part */
        return Ux.future(this.extractStatic(dataArray, tableName))
            /* dictionary for dynamic part */
            .compose(extracted -> this.extractDynamic(extracted, tableName))
            /* forbidden record filter */
            .compose(extracted -> this.extractForbidden(extracted, tableName));
    }

    private void extractIngest(final Set<ExTable> dataSet) {
        if (Objects.nonNull(this.tenant)) {
            final JsonObject dataGlobal = this.tenant.valueDefault();
            if (Ut.notNil(dataGlobal)) {
                /*
                 * New for developer account importing cross different
                 * apps
                 * {
                 *     "developer":
                 * }
                 */
                final JsonObject developer = Ut.valueJObject(dataGlobal, KName.DEVELOPER).copy();
                final JsonObject normalized = dataGlobal.copy();
                normalized.remove(KName.DEVELOPER);
                dataSet.forEach(table -> {
                    // Developer Checking
                    if ("S_USER".equals(table.getName()) && Ut.notNil(developer)) {
                        // JsonObject ( user = employeeId )
                        table.get().forEach(record -> {
                            // Mount Global Data
                            record.put(normalized);
                            // EmployeeId Replacement for `lang.yu` or other developer account
                            final String username = record.get(KName.USERNAME);
                            if (developer.containsKey(username)) {
                                record.put(KName.MODEL_KEY, developer.getString(username));
                            }
                        });
                    } else {
                        // Mount Global Data into the ingest data.
                        table.get().forEach(record -> record.put(normalized));
                    }
                });
            }
        }
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
            workbook = CC_WORKBOOK.pick(() -> Fn.getJvm(() -> new HSSFWorkbook(in)), filename);
            // Fn.po?l(Pool.WORKBOOKS, filename, () -> Fn.getJvm(() -> new HSSFWorkbook(in)));
        } else {
            workbook = CC_WORKBOOK.pick(() -> Fn.getJvm(() -> new XSSFWorkbook(in)), filename);
            // Fn.po?l(Pool.WORKBOOKS, filename, () -> Fn.getJvm(() -> new XSSFWorkbook(in)));
        }
        return workbook;
    }

    @SuppressWarnings("all")
    Workbook getWorkbook(final InputStream in, final boolean isXlsx) {
        Fn.outWeb(null == in, _404ExcelFileNullException.class, this.target, "Stream");
        final Workbook workbook;
        if (isXlsx) {
            workbook = CC_WORKBOOK_STREAM.pick(() -> Fn.getJvm(() -> new XSSFWorkbook(in)), in.hashCode());
            // Fn.po?l(Pool.WORKBOOKS_STREAM, in.hashCode(), () -> Fn.getJvm(() -> new XSSFWorkbook(in)));
        } else {
            workbook = CC_WORKBOOK_STREAM.pick(() -> Fn.getJvm(() -> new HSSFWorkbook(in)), in.hashCode());
            // Fn.po?l(Pool.WORKBOOKS_STREAM, in.hashCode(), () -> Fn.getJvm(() -> new HSSFWorkbook(in)));
        }
        /* Force to recalculation for evaluator */
        workbook.setForceFormulaRecalculation(Boolean.TRUE);
        return workbook;
    }

    /*
     * Get Set<ExSheet> collection based on workbook
     */
    Set<ExTable> getExTables(final Workbook workbook, final HTAtom HTAtom) {
        return Fn.getNull(new HashSet<>(), () -> {
            /* FormulaEvaluator reference */
            final FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

            /*
             * Workbook pool for FormulaEvaluator
             * 1）Local variable to replace global
             **/
            final Map<String, FormulaEvaluator> references = new ConcurrentHashMap<>();
            REFERENCES.forEach((field, workbookRef) -> {
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
                final Set<ExTable> dataSet = exSheet.analyzed(range, HTAtom);
                /*
                 * Here for critical injection, mount the data of
                 * {
                 *      "global": {
                 *      }
                 * }
                 * */
                this.extractIngest(dataSet);
                sheets.addAll(dataSet);
            }
            return sheets;
        }, workbook);
    }

    void brush(final Workbook workbook, final Sheet sheet, final HTAtom HTAtom) {
        if (Objects.nonNull(this.tpl)) {
            this.tpl.bind(workbook);
            this.tpl.applyStyle(sheet, HTAtom);
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
            final List<KConnect> connectList = Ut.deserialize(connects, new TypeReference<List<KConnect>>() {
            });
            connectList.stream().filter(Objects::nonNull)
                .filter(connect -> Objects.nonNull(connect.getTable()))
                .forEach(connect -> Pool.CONNECTS.put(connect.getTable(), connect));
            /* boot */
            final Set<KBoot> boots = KBoot.initialize();
            boots.forEach(boot -> Pool.CONNECTS.putAll(boot.configure()));
        }
    }

    void initEnvironment(final JsonArray environments) {
        environments.stream().filter(Objects::nonNull)
            .map(item -> (JsonObject) item)
            .forEach(each -> {
                /*
                 * Build reference
                 */
                final String path = each.getString("path");
                /*
                 * Reference Evaluator
                 */
                final String name = each.getString("name");
                final Workbook workbook = this.getWorkbook(path);
                REFERENCES.put(name, workbook);
                this.initEnvironment(each, workbook);
            });
    }

    void initTenant(final ExTenant tenant) {
        this.tenant = tenant;
    }

    private void initEnvironment(final JsonObject each, final Workbook workbook) {
        /*
         * Alias Parsing
         */
        if (each.containsKey(KName.ALIAS)) {
            final JsonArray alias = each.getJsonArray(KName.ALIAS);
            final File current = new File(Strings.EMPTY);
            Ut.itJArray(alias, String.class, (item, index) -> {
                final String filename = current.getAbsolutePath() + item;
                final File file = new File(filename);
                if (file.exists()) {
                    REFERENCES.put(file.getAbsolutePath(), workbook);
                }
            });
        }
    }

    /*
     * For Insert to avoid duplicated situation
     * 1. Key duplicated
     * 2. Unique duplicated
     */
    <T> List<T> compress(final List<T> input, final ExTable table) {
        final String key = table.pkIn();
        if (Objects.isNull(key)) {
            // Relation Table
            return input;
        }
        final List<T> keyList = new ArrayList<>();
        final Set<Object> keys = new HashSet<>();
        final AtomicInteger counter = new AtomicInteger(0);
        input.forEach(item -> {
            final Object value = Ut.field(item, key);
            if (Objects.nonNull(value) && !keys.contains(value)) {
                keys.add(value);
                keyList.add(item);
            } else {
                counter.incrementAndGet();
            }
        });
        final int ignored = counter.get();
        if (0 < ignored) {
            final Annal annal = Annal.get(this.target);
            annal.warn("[ Έξοδος ] Ignore table `{0}` with size `{1}`", table.getName(), ignored);
        }
        // Entity Release
        return keyList;
    }
}
