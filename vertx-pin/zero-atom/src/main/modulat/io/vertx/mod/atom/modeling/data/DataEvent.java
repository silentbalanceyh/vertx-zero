package io.vertx.mod.atom.modeling.data;

import io.horizon.eon.VValue;
import io.horizon.exception.WebException;
import io.horizon.uca.qr.Criteria;
import io.horizon.uca.qr.Pager;
import io.horizon.uca.qr.Sorter;
import io.horizon.uca.qr.syntax.Ir;
import io.modello.dynamic.modular.io.AoIo;
import io.modello.dynamic.modular.metadata.AoSentence;
import io.modello.dynamic.modular.plugin.IoHub;
import io.modello.eon.em.EmModel;
import io.modello.specification.HRecord;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.atom.cv.em.EventType;
import io.vertx.mod.atom.error._417DataRowNullException;
import io.vertx.mod.atom.modeling.Model;
import io.vertx.mod.atom.modeling.builtin.DataAtom;
import io.vertx.mod.atom.modeling.element.DataRow;
import io.vertx.mod.atom.modeling.element.DataTpl;
import io.vertx.mod.atom.refine.Ao;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class DataEvent implements Serializable {

    /*
     * 元数据定义（一份足够了）：
     *     attribute = table
     *     属性 = 表名
     */
    private final transient DataAtom atom;  // Delay 模式
    private final transient DataTpl tpl;
    private final transient Set<String> projection = new HashSet<>();
    private transient WebException exception;
    // private transient DataInOut input;
    private transient AoIo io;
    private transient Criteria criteria;
    private transient Ir qr;
    private transient long counter;

    private DataEvent(final DataAtom atom, final AoSentence sentence) {
        this.atom = atom;
        /*
         * DataTpl 专用，模板处理，创建 DataEvent的时候 Tpl对应的模板就固定下来
         * 固定下来过后成为只读对象
         * 1. AoIo 中的数据依靠 DataTpl 来实现数据本身的初始化动作
         * 2. DataTpl 保存了当前系统中需要使用的 Table 以及 Matrix
         */
        final Model model = atom.model();
        this.tpl = DataTpl.create()
            .on(sentence)
            .on(atom);
        // 初始化 Tpl 模板
        /* 连接专用填充 ItemMatrix */
        Bridge.connect(model,
            // 字段基本函数
            (schema) -> (field, attribute) -> this.tpl.initTpl(schema, field, attribute));
        /* 连接专用填充 ItemMatrix - 主键类 */
        Bridge.join(model,
            // 主键关联函数，虚拟键，不填充 this.sources
            (schema) -> (field, attribute) ->
                this.tpl.initKey(schema, field, attribute));
    }

    public static DataEvent create(final DataAtom atom, final AoSentence sentence) {
        // 每次创建一个新的 Data Event
        return new DataEvent(atom, sentence);
    }

    /* 非数据层面的第一个方法 */
    public DataEvent init(final EventType type) {
        /*
         * 不同的类型创建的 AoIo 会有所区别
         * 但是 AoIo 需要和 DataTpl 绑定到一起，使得 AoIo 可以直接消费 DataTpl 中的定义数据
         */
        this.io = AoIo.create(type);
        if (null != this.io) {
            /*
             * IO 数据和 Tpl 绑定
             */
            this.io.on(this.tpl);
        }
        /* 绑定 DataAtom */
        return this;
    }

    // --------------- 数据填充环节 ----------------

    /*
     * 按主键查询、按主键更新、删除时调用的方法
     * 只设置主键，其他不设置
     * 起点：创建新的 DataRow
     */
    @SafeVarargs
    public final <ID> DataEvent keys(final ID... keys) {
        this.io.keys(keys);
        return this;
    }

    /*
     * 直接设置为 Record 的值
     * 起点：创建新的 DataRow
     */
    public DataEvent records(final HRecord... records) {
        this.io.records(records);
        return this;
    }

    /*
     * 非起点：只能在有数据的时候执行该方法
     * 给每一行设置 UUID 的主键
     */
    public DataEvent uuid() {
        this.io.uuid();
        return this;
    }

    // --------------- 查询专用 --------------------
    /*
     * 起点：设置当前 DataEvent的查询条件，查询条件分为两种
     * 1）直接的 criteria 查询条件，没有分页、排序功能
     * 2）完整的 criteria 查询条件，分页、排序、列过滤
     */
    public DataEvent criteria(final Criteria criteria) {
        this.criteria = criteria;
        return this;
    }

    public DataEvent qr(final Ir qr) {
        this.qr = qr;
        if (Objects.nonNull(qr)) {
            /*
             * Qr 和 Criteria 的关系嵌套
             */
            this.criteria = qr.getCriteria();
            // java.lang.NullPointerException for Qr
            if (Objects.isNull(this.criteria)) {
                this.criteria = Criteria.create(new JsonObject());
            }
            final Set<String> projections = Optional.ofNullable(qr.getProjection()).orElse(new HashSet<>());
            if (!projections.isEmpty()) {
                this.projection.addAll(projections);
            }
        }
        return this;
    }

    // --------------- 存储专用 --------------------
    public DataEvent stored(final List<DataRow> rows) {
        this.io.on(rows);
        return this;
    }

    public DataEvent stored(final Long counter) {
        this.counter = counter;
        return this;
    }

    public Long getCounter() {
        return this.counter;
    }

    public DataTpl getTpl() {
        return this.tpl;
    }

    public Criteria getCriteria() {
        return this.criteria;
    }

    public Set<String> getProjection() {
        return this.projection;
    }

    public Pager getPager() {
        if (Objects.nonNull(this.qr)) {
            return this.qr.getPager();
        } else {
            return null;
        }
    }

    public Sorter getSorter() {
        if (Objects.nonNull(this.qr)) {
            return this.qr.getSorter();
        } else {
            return null;
        }
    }

    public EmModel.Type getType() {
        return this.atom.model().type();
    }

    // --------------- 数据获取环节 ----------------
    /*
     * 获取独立行，单记录操作必须
     */
    @SuppressWarnings("all")
    public DataRow dataRow() {
        /* DataRow 不能为空，有操作旧必须保证 rows 中有数据 */
        final List<DataRow> rows = this.io.getRows();

        Fn.outWeb(null == rows || rows.isEmpty(), _417DataRowNullException.class, this.getClass(),
            /* ARG1：当前 Model 的模型标识符 */ this.atom.identifier());

        final DataRow row = rows.get(VValue.IDX);

        Fn.outWeb(null == row, _417DataRowNullException.class, this.getClass(),
            /* ARG1：当前 Model 的模型标识符 */ this.atom.identifier());

        return row;
    }

    /*
     * 获取行集合，批量操作必须
     */
    public List<DataRow> dataRows() {
        return this.io.getRows();
    }

    /*
     * 获取独立行，单记录操作必须的方法
     * 对于 Record 的读取而言，不能抛出 getRow 中的核心异常，所以这里需要重写
     * IoHub Output Single
     */
    public HRecord dataR() {
        final HRecord record = this.record();
        final IoHub hub = IoHub.instance();
        return hub.out(record, this.tpl);
    }

    public Future<HRecord> dataRAsync() {
        final HRecord record = this.record();
        final IoHub hub = IoHub.instance();
        return hub.outAsync(record, this.tpl);
    }

    /*
     * 获取记录集合，批量操作：第二层
     * IoHub Output Batch
     */
    public HRecord[] dataA() {
        final HRecord[] response = this.records();
        final IoHub hub = IoHub.instance();
        return hub.out(response, this.tpl);
    }

    public Future<HRecord[]> dataAAsync() {
        final HRecord[] response = this.records();
        final IoHub hub = IoHub.instance();
        return hub.outAsync(response, this.tpl);
    }

    /*
     * 特殊结构返回
     * {
     *     "list":[],
     *     "count": 1
     * }
     */
    public JsonObject dataP() {
        final HRecord[] records = this.dataA();
        return this.dataP(records);
    }

    public Future<JsonObject> dataPAsync() {
        return this.dataAAsync()
            .compose(records -> Ux.future(this.dataP(records)));
    }

    // Private ----------------------
    private HRecord record() {
        final List<DataRow> rows = this.io.getRows();
        HRecord record = Ao.record(this.atom);
        if (null != rows && !rows.isEmpty()) {
            final DataRow row = rows.get(VValue.IDX);
            if (null != row) {
                record = row.getRecord();
            }
        }
        return record;
    }

    private HRecord[] records() {
        final List<DataRow> rows = this.dataRows();
        return rows.stream()
            .map(DataRow::getRecord)
            .filter(Objects::nonNull)
            .collect(Collectors.toList())
            .toArray(new HRecord[]{});
    }

    private JsonObject dataP(final HRecord[] records) {
        final JsonArray list = new JsonArray();
        Arrays.stream(records).map(HRecord::toJson).forEach(list::add);
        return Ux.pageData(list, this.counter);
    }
    // ------------ 事件执行结果处理 --------------

    public void failure(final WebException ex) {
        this.exception = ex;
    }

    /*
     * 异常响应：执行过程中的错误信息
     */
    public WebException getError() {
        return this.exception;
    }

    public Boolean succeed() {
        final List<DataRow> rows = this.dataRows();
        final long counter = rows.stream().filter(DataRow::succeed)
            .count();
        /*
         * 两个条件：
         * 1. 每一行的处理结果 succeed
         * 2. 当前 WebException 为 null
         */
        return rows.size() == counter
            && null == this.exception;
    }

    // ------------ 私有函数 --------------

    public void consoleAll() {
        Debug.tpl(this.tpl);
        Debug.io(this.io.getRows());
    }
}
