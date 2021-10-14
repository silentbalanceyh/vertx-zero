package cn.originx.quiz.atom;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.data.DataAtom;
import io.vertx.tp.atom.refine.Ao;
import io.vertx.up.commune.Record;
import io.vertx.up.exception.WebException;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * ## 「Pojo」测试输入模型
 *
 * ### 1. 基本介绍
 *
 * 构造Ox平台模型专用的测试输入对象，它提供如下功能
 *
 * - 存储{@link DataAtom}模型定义，引用某一类模型。
 * - 根据模型定义和JsonObject数据，构造{@link Record}记录。
 * - 根据模型定义和JsonArray数据，构造`{@link Record}[]`记录数组（批量）。
 *
 * ### 2. 模拟数据格式
 *
 * <pre><code>
 * {
 *     "identifier": "模型标识符",
 *     "key": "主键",
 *     "data": {
 *
 *     }
 * }
 * </code></pre>
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class QModel implements Serializable {

    /**
     * Zero专用日志器，{@link Annal}
     */
    private static final Annal LOGGER = Annal.get(QModel.class);
    /**
     * {@link DataAtom}内部模型定义对象引用。
     */
    private final transient DataAtom atom;
    /**
     * {@link String} 测试数据主键
     */
    private transient String key;
    /**
     * {@link Record}数据记录（单记录）。
     */
    private transient Record record;
    /**
     * {@link Record}[]数据记录（批量）。
     */
    private transient Record[] records;

    /**
     * 单记录构造函数
     *
     * @param atom {@link DataAtom} 模型定义引用
     * @param data {@link JsonObject} Json对象数据
     */
    public QModel(final DataAtom atom, final JsonObject data) {
        this.atom = atom;
        this.record = newRecord(atom, data);
    }

    /**
     * @param atom {@link DataAtom} 模型定义引用
     * @param key  {@link String} 主键数据
     */
    public QModel(final DataAtom atom, final String key) {
        this.atom = atom;
        this.key = key;
    }

    /**
     * 多记录构造函数
     *
     * @param atom {@link DataAtom} 模型定义引用
     * @param data {@link JsonArray} Json数组数据
     */
    public QModel(final DataAtom atom, final JsonArray data) {
        this.atom = atom;
        final List<Record> recordList = new ArrayList<>();
        Ut.itJArray(data).map(json -> newRecord(atom, json))
            .forEach(recordList::add);
        this.records = recordList.toArray(new Record[]{});
    }

    /**
     * 单记录构造器，直接根据{@link JsonObject}对象构造{@link Record}记录对象。
     *
     * @param atom {@link DataAtom} 模型定义引用
     * @param data {@link JsonObject} Json对象数据
     *
     * @return {@link Record}
     */
    public static Record newRecord(final DataAtom atom, final JsonObject data) {
        Record record;
        try {
            record = Ao.record(atom);
            record.fromJson(data);
        } catch (final WebException ex) {
            LOGGER.jvm(ex);
            record = null;
        }
        return record;
    }

    /**
     * 获取单记录Json数据
     *
     * @return {@link JsonObject}
     */
    public JsonObject getRecordData() {
        return this.record.toJson();
    }

    /**
     * 获取多记录Json数据
     *
     * @return {@link JsonArray}
     */
    public JsonArray getRecordsData() {
        return Ut.toJArray(this.records);
    }

    /**
     * 获取模型标识符identifier
     *
     * @return {@link String}
     */
    public String identifier() {
        return this.atom.identifier();
    }

    /**
     * 获取单记录对象
     *
     * @return {@link Record}
     */
    public Record getRecord() {
        return this.record;
    }

    /**
     * 获取单记录数组对象
     *
     * @return {@link Record}[]
     */
    public Record[] getRecords() {
        return this.records;
    }

    /**
     * 获取模型定义引用
     *
     * @return {@link DataAtom}
     */
    public DataAtom getAtom() {
        return this.atom;
    }

    /**
     * @return {@link String}
     */
    public String getKey() {
        return this.key;
    }
}
