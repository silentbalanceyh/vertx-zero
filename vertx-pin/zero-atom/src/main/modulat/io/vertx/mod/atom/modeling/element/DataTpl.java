package io.vertx.mod.atom.modeling.element;

import cn.vertxup.atom.domain.tables.pojos.MAttribute;
import cn.vertxup.atom.domain.tables.pojos.MField;
import cn.vertxup.atom.domain.tables.pojos.MJoin;
import io.horizon.uca.cache.Cc;
import io.modello.dynamic.modular.metadata.AoSentence;
import io.modello.eon.em.EmModel;
import io.modello.specification.HRecord;
import io.vertx.mod.atom.modeling.Model;
import io.vertx.mod.atom.modeling.Schema;
import io.vertx.mod.atom.modeling.builtin.DataAtom;
import io.vertx.mod.atom.refine.Ao;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class DataTpl implements Serializable {
    /*
     * 模板专用，在初始化时使用，只需要
     */
    private final transient ConcurrentMap<String, DataMatrix> tpl
        = new ConcurrentHashMap<>();
    private final transient ConcurrentMap<String, String> sources
        = new ConcurrentHashMap<>();

    private transient AoSentence sentence;
    private transient DataAtom atom;

    private DataTpl() {
    }

    public static DataTpl create() {
        return new DataTpl();
    }

    public String identifier() {
        return Objects.isNull(this.atom) ? null : this.atom.identifier();
    }

    public DataTpl on(final AoSentence sentence) {
        this.sentence = sentence;
        return this;
    }

    public DataTpl on(final DataAtom atom) {
        this.atom = atom;
        return this;
    }

    /* 创建一个新的主键 */
    DataKey createKey() {
        final DataKey key = this.atom.model().key();
        return key.cloneKey();
    }

    /* 创建一个新的记录 */
    HRecord createRecord() {
        return Ao.record(this.atom);
    }


    // -------------- Join 专用 -----------
    /*
     * Join时的主表
     */
    public MJoin joinLeader() {
        if (EmModel.Type.JOINED != this.atom.model().type()) {
            /*
             * leader只有join会有效，如果是 DIRECT 属于单表处理，则不需要
             * 类似 leader 的角色出现
             * VIEW 和 DIRECT 两种模式都可以直接处理
             */
            return null;
        } else {
            final Set<MJoin> joins = this.atom.model().dbJoins();
            /*
             * 此时 joins 的尺寸 > 1
             * 默认的 JOIN 模式下会筛选 leader 来处理
             * LEFT / RIGHT 两种模式的JOIN
             * 1）priority 没填写：直接做自然连接
             * 2）priority 最小的执行 LEFT 左连接
             */
            final MJoin join = joins.stream()
                .filter(item -> Objects.nonNull(item.getPriority()))
                .filter(item -> 0 < item.getPriority())     // 0 表示没有优先级
                .min(Comparator.comparing(MJoin::getPriority))
                .orElse(null);
            return Objects.isNull(join) ? null : join;
        }
    }

    /*
     * 读取所有的被 join 的相关信息
     * MJoin 中的 entityKey / entity 的集合
     */
    public ConcurrentMap<String, String> joinVoters(final Predicate<MJoin> predicate) {
        final ConcurrentMap<String, String> pointer = new ConcurrentHashMap<>();
        this.atom.model().dbJoins()
            .stream().filter(predicate)
            .forEach(join -> pointer.put(join.getEntity(), join.getEntityKey()));
        return pointer;
    }

    public ConcurrentMap<String, String> joinVoters() {
        return this.joinVoters(join -> true);
    }

    /*
     * 根据 identifier 查找对应的 table 名
     */
    public Schema schema(final String identifier) {
        final Model model = this.atom.model();
        return model.schema(identifier);
    }

    // -------------- 处理 ----------------
    public String column(final String field) {
        return this.tpl.values().stream()
            .filter(matrix -> Objects.nonNull(matrix.getColumn(field)))
            .map(matrix -> matrix.getColumn(field))
            .findAny().orElse(null);
    }

    public ConcurrentMap<String, DataMatrix> matrixData() {
        return this.tpl;
    }

    ConcurrentMap<String, DataMatrix> matrixKey() {
        return this.getKey().getMatrix();
    }

    public DataKey getKey() {
        return this.atom.model().key();
    }

    public DataAtom atom() {
        return this.atom;
    }

    public void initTpl(final Schema schema, final MField field, final MAttribute attribute) {
        // 读取 Table Name
        final String tableName = schema.getTable();
        this.initMatrix(schema, field, attribute).accept(this.tpl);
        // 执行 Source 的初始化
        this.sources.put(attribute.getName(), tableName);
    }

    public void initKey(final Schema schema, final MField field, final MAttribute attribute) {
        final DataKey key = this.getKey();
        if (null != key) {
            this.initMatrix(schema, field, attribute).accept(key.getMatrix());
        }
    }

    private Consumer<ConcurrentMap<String, DataMatrix>> initMatrix(final Schema schema, final MField field, final MAttribute attribute) {
        return tpl -> {
            // 读取 Table Name
            final String tableName = schema.getTable();

            final DataMatrix matrix = Cc.pool(tpl, tableName, DataMatrix::create);
            // Fn.po?l(tpl, tableName, DataMatrix::create);
            // 是否绑定 AoSentence
            if (null != this.sentence) {
                matrix.on(this.sentence);
            }
            // 添加字段信息
            matrix.add(field, attribute);
        };
    }

    public void appendConsole(final StringBuilder buffer) {
        this.sources.forEach((attribute, tableName) -> buffer.append(String.format("%-20s", attribute))
            .append(String.format("%-20s", tableName))
            .append("\n"));
        final DataKey key = this.getKey();
        if (null != key) {
            buffer.append("\n [Key] 主键，DataMatrix -> : \n");
            key.getMatrix().forEach((table, matrix) -> {
                buffer.append("表名：").append(table).append("\n");
                matrix.appendConsole(buffer);
                buffer.append("\n");
            });
        }
        buffer.append("[Table] 表， DataMatrix -> ：\n");
        this.tpl.forEach((table, matrix) -> {
            buffer.append("表名：").append(table).append("\n");
            matrix.appendConsole(buffer);
            buffer.append("\n");
        });
    }
}
