package io.vertx.tp.atom.refine;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.Model;
import io.vertx.tp.atom.modeling.Schema;
import io.vertx.tp.atom.modeling.data.DataAtom;
import io.vertx.tp.atom.modeling.element.DataMatrix;
import io.vertx.tp.modular.dao.AoDao;
import io.vertx.tp.optic.robin.Switcher;
import io.vertx.tp.plugin.excel.atom.ExTable;
import io.vertx.up.atom.record.Apt;
import io.vertx.up.commune.Record;
import io.vertx.up.commune.config.Database;
import io.vertx.up.commune.config.Identity;
import io.vertx.up.commune.element.JBag;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.log.Annal;
import org.jooq.Converter;

import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

public class Ao {
    /*
     * Logger
     */
    public static void infoInit(final Class<?> clazz, final String pattern, final Object... args) {
        final Annal logger = Annal.get(clazz);
        AoLog.info(logger, "Init", pattern, args);
    }

    public static void debugAtom(final Class<?> clazz, final String pattern, final Object... args) {
        final Annal logger = Annal.get(clazz);
        AoLog.debug(logger, "Atom", pattern, args);
    }

    public static void infoAtom(final Class<?> clazz, final String pattern, final Object... args) {
        final Annal logger = Annal.get(clazz);
        AoLog.debug(logger, "Atom", pattern, args);
    }

    public static void debugUca(final Class<?> clazz, final String pattern, final Object... args) {
        final Annal logger = Annal.get(clazz);
        AoLog.debug(logger, "UCA", pattern, args);
    }

    public static void infoUca(final Class<?> clazz, final String pattern, final Object... args) {
        final Annal logger = Annal.get(clazz);
        AoLog.info(logger, "UCA", pattern, args);
    }

    public static void infoDiff(final Class<?> clazz, final String pattern, final Object... args) {
        final Annal logger = Annal.get(clazz);
        AoLog.info(logger, "Diff", pattern, args);
    }

    public static void infoPlugin(final Class<?> clazz, final String pattern, final Object... args) {
        final Annal logger = Annal.get(clazz);
        AoLog.info(logger, "Plugin", pattern, args);
    }

    public static void infoSQL(final Annal logger, final String pattern, final Object... args) {
        AoLog.info(logger, "Sql", pattern, args);
    }

    public static void infoSQL(final Annal logger, final boolean condition, final String pattern, final Object... args) {
        if (condition) {
            infoSQL(logger, pattern, args);
        }
    }

    /*
     * Diff
     */
    public static ConcurrentMap<ChangeFlag, JsonArray> diffPure(final JsonArray queueOld, final JsonArray queueNew, final DataAtom atom, final Set<String> ignoreSet) {
        return AoCompare.diffPure(queueOld, queueNew, atom, ignoreSet);
    }

    public static JsonObject diffPure(final JsonObject recordO, final JsonObject recordN, final DataAtom atom, final Set<String> ignoreSet) {
        return AoCompare.diffPure(recordO, recordN, atom, ignoreSet);
    }

    public static Apt diffPure(final Apt apt, final DataAtom atom, final Set<String> ignoreSet) {
        return AoCompare.diffPure(apt, atom, ignoreSet);
    }

    public static ConcurrentMap<ChangeFlag, JsonArray> diffPull(final JsonArray queueOld, final JsonArray queueNew, final DataAtom atom, final Set<String> ignoreSet) {
        return AoCompare.diffPull(queueOld, queueNew, atom, ignoreSet);
    }

    public static JsonObject diffPull(final JsonObject recordO, final JsonObject recordN, final DataAtom atom, final Set<String> ignoreSet) {
        return AoCompare.diffPull(recordO, recordN, atom, ignoreSet);
    }

    public static Apt diffPull(final Apt apt, final DataAtom atom, final Set<String> ignoreSet) {
        return AoCompare.diffPull(apt, atom, ignoreSet);
    }

    public static <T> ConcurrentMap<ChangeFlag, List<T>> initMList() {
        return AoCompare.initMList();
    }

    public static <T> ConcurrentMap<ChangeFlag, Queue<T>> initMQueue() {
        return AoCompare.initMQueue();
    }

    public static ConcurrentMap<ChangeFlag, JsonArray> initMArray() {
        return AoCompare.initMArray();
    }

    /*
     * Calculate current modular `namespace`
     * - Each application has only one namespace
     * - The namespace could be calculated by `appName`
     *
     * The api list
     * - 1) toNamespace
     * - 2) toKey
     * - 3) joinKeys
     * - 4) toSchema
     */
    public static String toNamespace(final String appName) {
        return AoStore.toNamespace(appName);
    }

    public static <ID> Object toKey(final ID id) {
        return AoKey.toKey(id);
    }

    public static <ID> ID toKey(final JsonObject data, final DataAtom atom) {
        return AoKey.toKey(data, atom);
    }

    public static <ID> void toKey(final JsonObject data, final DataAtom atom, final ID defaultKey) {
        AoKey.toKey(data, atom, defaultKey);
    }

    public static Schema toSchema(final String appName, final JsonObject schemaJson) {
        return AoImpl.toSchema(appName, schemaJson);
    }

    public static Schema toSchema(final String appName, final String file) {
        return AoImpl.toSchema(appName, file);
    }

    public static Model toModel(final String appName, final JsonObject modelJson) {
        return AoImpl.toModel(appName, modelJson);
    }

    public static Model toModel(final String appName, final String file) {
        return AoImpl.toModel(appName, file);
    }

    public static Switcher toSwitcher(final Identity identity, final JsonObject options) {
        return AoImpl.toSwitcher(identity, options);
    }
    /*
     * 构造 DataAtom 数据
     * 1）传入的是 IService，直接反序列化构造 DataAtom
     * 2）传入 key 和 identifier，反序列化构造 DataAtom，key 可以是 appId 也可以是 sigma
     * 3）传入 options，只支持直接构造
     * {
     *     "name": "应用名称",
     *     "identifier": "模型ID"
     * }
     */

    public static DataAtom toAtom(final JsonObject options) {
        return AoImpl.toAtom(options);
    }

    /*
     * 构造 oxDao
     * 1）传入 key 和 identifier，构造 AoDao
     * 2）传入 Database 和 DataAtom，构造 AoDao
     * 3）构造纯的 AoDao（不和任何 DataAtom绑定）
     */

    public static AoDao toDao(final Database database, final DataAtom atom) {
        return AoImpl.toDao(database, atom);
    }

    public static AoDao toDao(final Database database, final Supplier<DataAtom> supplier) {
        return AoImpl.toDao(database, supplier);
    }

    public static String joinKey(final Model model) {
        return AoKey.joinKey(model);
    }

    public static ConcurrentMap<String, Object> joinKeys(final Model model, final Record record) {
        return AoKey.joinKeys(model, record);
    }

    /*
     * Configuration Other
     */
    public static JsonObject adjuster() {
        return AoStore.configAdjuster();
    }

    public static JsonObject adjuster(final String name) {
        return AoStore.configModeling(name);
    }

    public static boolean isDebug() {
        return AoStore.isDebug();
    }

    /*
     * Class name processing
     */
    public static Class<?> pluginPin() {
        return AoStore.clazzPin();
    }

    public static void connect(final Record record, final ConcurrentMap<String, DataMatrix> keyMatrix, final ConcurrentMap<String, DataMatrix> dataMatrix, final Set<String> joins) {
        AoData.connect(record, keyMatrix, dataMatrix, joins);
    }


    /*
     * Record Building
     */
    public static Record record(final DataAtom atom) {
        return AoData.record(atom);
    }

    public static List<Record> records(final DataAtom atom, final ExTable table) {
        return AoData.records(atom, table);
    }

    public static Record record(final JsonObject data, final DataAtom atom) {
        return AoData.record(data, atom);
    }

    public static Record[] records(final JsonArray data, final DataAtom atom) {
        return AoData.records(data, atom);
    }

    @SuppressWarnings("all")
    public static Converter converter(final Class<?> type) {
        return AoData.converter(type);
    }

    public static List<JBag> split(final JBag bag, final Integer size) {
        return AoData.bagSplit(bag, size);
    }

    /*
     * 路径处理
     */
    public interface Path {
        String PATH_EXCEL = AoStore.defineExcel();
        String PATH_JSON = AoStore.defineJson();
    }
}
