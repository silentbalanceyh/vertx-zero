package io.modello.dynamic.modular.io;

import io.horizon.uca.log.Annal;
import io.vertx.mod.atom.modeling.element.DataRow;
import io.vertx.mod.atom.modeling.element.DataTpl;

import java.util.*;
import java.util.function.Supplier;

/*
 * 通用方法，直接被继承
 */
public abstract class AbstractIo implements AoIo {

    private transient final List<DataRow> rows = new ArrayList<>();
    private transient final Set<Object> rowKeys = new HashSet<>();
    private transient DataTpl tpl;

    @Override
    public AoIo on(final DataTpl tpl) {
        this.tpl = tpl;
        return this;
    }

    @Override
    public AoIo on(final List<DataRow> input) {
        this.clearRows();
        input.forEach(this::addRow);
        return this;
    }

    @Override
    public List<DataRow> getRows() {
        return this.rows;
    }

    @Override
    public boolean clearRows() {
        this.rows.clear();
        this.rowKeys.clear();
        return true;
    }

    @Override
    public AoIo uuid() {
        /* rows获取 */
        final List<DataRow> rows = this.getRows();
        /* 每一行设置主键，没有值的时候设置主键，有值不设置 */
        rows.stream().filter(row -> Objects.isNull(row.getId()))
            .forEach(row -> row.setKey(UUID.randomUUID()));
        return this;
    }

    protected Annal getLogger() {
        return Annal.get(this.getClass());
    }

    DataRow newRow() {
        return new DataRow(this.tpl);
    }

    DataTpl tpl() {
        return this.tpl;
    }

    // 针对数据行的处理
    private void saveRow(final DataRow row) {
        final Object id = row.getId();
        if (Objects.isNull(id)) {
            /*
             * 为空的情况下，证明没调用 setKey 方法
             * 那么在这样的情况下，直接添加 row
             **/
            this.addRow(row);
        } else {
            /*
             * 有 id 的情况下，证明行已经存在主键
             * 那么这种情况就按主键添加
             */
            if (!this.rowKeys.contains(row.getId())) {
                this.addRow(row);
            }
        }
    }

    private void addRow(final DataRow row) {
        this.rowKeys.add(row.getId());
        this.rows.add(row);
    }

    protected AoIo saveRows(final Supplier<List<DataRow>> supplier) {
        final List<DataRow> rows = supplier.get();
        rows.forEach(this::saveRow);
        this.getLogger().debug("[Io] 数据行数量：{0}", String.valueOf(this.rows.size()));
        return this;
    }

    protected AoIo saveRow(final Supplier<DataRow> supplier) {
        this.saveRow(supplier.get());
        this.getLogger().debug("[Io] 数据行数量：{0}", String.valueOf(this.rows.size()));
        return this;
    }
}
