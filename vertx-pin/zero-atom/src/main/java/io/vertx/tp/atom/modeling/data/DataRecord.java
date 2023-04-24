package io.vertx.tp.atom.modeling.data;

import cn.vertxup.atom.domain.tables.pojos.MJoin;
import io.horizon.specification.modeler.Record;
import io.vertx.tp.atom.modeling.Model;
import io.vertx.tp.atom.modeling.builtin.DataAtom;
import io.vertx.tp.atom.modeling.element.DataKey;
import io.vertx.tp.atom.refine.Ao;
import io.vertx.tp.modular.id.AoId;
import io.vertx.up.annotations.Contract;
import io.vertx.up.commune.ActiveRecord;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class DataRecord extends ActiveRecord {
    /*
     * 契约专用的 DataAtom 模型定义，使用契约模式穿透处理创建完成
     */
    @Contract
    private transient DataAtom atom;

    /*
     * 创建一个新记录
     */
    @Override
    public Record createNew() {
        return Ao.record(this.atom);
    }

    /*
     * 「Optional Override」读取记录关联信息
     */
    @Override
    public Set<String> joins() {
        return this.atom.model().dbJoins().stream()
            .map(MJoin::getEntityKey)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
    }

    /*
     * 返回记录支持的所有属性
     */
    @Override
    public Set<String> declaredFields() {
        return this.atom.attribute();
    }

    @Override
    public ConcurrentMap<String, Class<?>> types() {
        return this.atom.type();
    }

    // ------------- 主键处理 --------------
    @Override
    public <ID> ID key() {
        return this.id().key(this, this.atom.model());
    }

    @Override
    public String identifier() {
        return Objects.isNull(this.atom) ? super.identifier() : this.atom.identifier();
    }

    @Override
    public <ID> void key(final ID key) {
        this.id().key(this, this.atom.model(), key);
    }

    private AoId id() {
        final Model model = this.atom.model();
        final DataKey keyRef = model.key();
        return AoId.get(keyRef.getMode());
    }

    // ------------ 重写hashCode和equals
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DataRecord)) {
            return false;
        }
        final DataRecord that = (DataRecord) o;
        return this.data().equals(that.data()) &&
            this.atom.equals(that.atom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.data(), this.atom);
    }
}
