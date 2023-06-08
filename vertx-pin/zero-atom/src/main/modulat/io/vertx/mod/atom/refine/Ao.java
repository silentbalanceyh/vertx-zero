package io.vertx.mod.atom.refine;

import io.horizon.eon.em.typed.ChangeFlag;
import io.horizon.spi.robin.Switcher;
import io.horizon.uca.log.Log;
import io.horizon.uca.log.LogModule;
import io.modello.specification.HRecord;
import io.modello.specification.action.HDao;
import io.modello.specification.atom.HAtom;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.atom.modeling.Model;
import io.vertx.mod.atom.modeling.Schema;
import io.vertx.mod.atom.modeling.builtin.DataAtom;
import io.vertx.mod.atom.modeling.element.DataMatrix;
import io.vertx.up.atom.element.JBag;
import io.vertx.up.commune.config.Database;
import io.vertx.up.commune.config.Identity;
import io.vertx.up.commune.record.Apt;
import io.vertx.up.plugin.excel.atom.ExTable;
import io.vertx.up.util.Ut;
import org.jooq.Converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

public class Ao {

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
    public static String toNS(final String appName) {
        return AoStore.namespace(appName);
    }

    public static String toNS(final String appName, final String identifier) {
        return AoStore.namespace(appName) + "-" + identifier;
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

    /*
     * - Schema
     *   toSchema(String, JsonObject)
     *   toSchema(String, String)
     *
     * - Model
     *   toModel(String, JsonObject)
     *   toModel(String, String)
     *
     * - Switcher
     *   toSwitcher(Identity, JsonObject)
     */
    public static Schema toSchema(final String appName, final JsonObject schemaJson) {
        return AoImpl.toSchema(appName, schemaJson);
    }

    public static Schema toSchema(final String appName, final String file) {
        return AoImpl.toSchema(appName, file);
    }

    public static Switcher toSwitcher(final Identity identity, final JsonObject options) {
        return AoImpl.toSwitcher(identity, options);
    }

    // ------------------- Model Creating -----------------
    public static Model toModel(final String appName, final JsonObject modelJson) {
        final Model model = AoImpl.toModel(appName);
        model.fromJson(modelJson);
        return model;
    }

    public static Model toModel(final String appName, final String file) {
        final Model model = AoImpl.toModel(appName);
        model.fromFile(file);
        return model;
    }

    // ------------------- Dao / Atom -----------------
    /*
     * 构造 DataAtom 数据
     * 1）传入的是 IService，直接反序列化构造 DataAtom
     * 2）传入 key 和 identifier，反序列化构造 DataAtom，key 可以是 appId 也可以是 sigma
     * 3）传入 options，只支持直接构造
     * {
     *     "name": "应用名称",
     *     "identifier": "模型ID"
     * }
     *
     * 构造 oxDao
     * 1）传入 key 和 identifier，构造 AoDao
     * 2）传入 Database 和 DataAtom，构造 AoDao
     * 3）构造纯的 AoDao（不和任何 DataAtom绑定）
     *
     * 这里有一点需说明：
     * 1. 复杂参数内置了 appId 或 sigma，所以可直接通过对象级获取
     * 2. 简单参数 String 通常只传入 identifier
     */
    public static DataAtom toAtom(final JsonObject options) {
        return AoImpl.toAtom(options);
    }

    public static DataAtom toAtom(final String identifier) {
        return AoImpl.toAtom(identifier);
    }

    public static DataAtom toAtom(final String appName, final String identifier) {
        return AoImpl.toAtom(appName, identifier);
    }

    public static HDao toDao(final HAtom atom) {
        return AoImpl.toDao(atom);
    }

    public static HDao toDao(final HAtom atom, final Database database) {
        return AoImpl.toDao(() -> atom, database);
    }

    public static HDao toDao(final String identifier) {
        return AoImpl.toDao(identifier);
    }

    public static HRecord toRecord(final String identifier, final JsonObject data) {
        return AoImpl.toRecord(identifier, data);
    }

    public static HRecord[] toRecord(final String identifier, final JsonArray data) {
        final List<HRecord> recordList = new ArrayList<>();
        Ut.itJArray(data).forEach(json -> recordList.add(toRecord(identifier, json)));
        return recordList.toArray(new HRecord[]{});
    }

    public static String joinKey(final Model model) {
        return AoKey.joinKey(model);
    }

    // ------------------- Other Information -----------------

    public static ConcurrentMap<String, Object> joinKeys(final Model model, final HRecord record) {
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

    public static void connect(final HRecord record, final ConcurrentMap<String, DataMatrix> keyMatrix, final ConcurrentMap<String, DataMatrix> dataMatrix, final Set<String> joins) {
        AoData.connect(record, keyMatrix, dataMatrix, joins);
    }

    /*
     * Record Building
     */
    public static HRecord record(final DataAtom atom) {
        return AoData.record(atom);
    }

    public static List<HRecord> records(final DataAtom atom, final ExTable table) {
        return AoData.records(atom, table);
    }

    public static HRecord record(final JsonObject data, final DataAtom atom) {
        return AoData.record(data, atom);
    }

    public static HRecord[] records(final JsonArray data, final DataAtom atom) {
        return AoData.records(data, atom);
    }

    @SuppressWarnings("all")
    public static Converter converter(final Class<?> type) {
        return AoData.converter(type);
    }

    public static List<JBag> split(final JBag bag, final Integer size) {
        return AoData.bagSplit(bag, size);
    }

    public interface LOG {
        String MODULE = "διαμορφωτής";
        LogModule Init = Log.modulat(MODULE).program("Init");
        LogModule Atom = Log.modulat(MODULE).program("Atom");
        LogModule Diff = Log.modulat(MODULE).program("Diff");
        LogModule Plugin = Log.modulat(MODULE).program("Infusion");
        LogModule SQL = Log.modulat(MODULE).program("SQL");
        LogModule Uca = Log.modulat(MODULE).program("EmUca");
    }

    /*
     * 路径处理
     */
    public interface PATH {
        String PATH_EXCEL = AoStore.defineExcel();
        String PATH_JSON = AoStore.defineJson();
    }
}
