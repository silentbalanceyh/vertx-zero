package io.modello.dynamic.modular.io;

import io.modello.specification.HRecord;
import io.vertx.mod.atom.cv.em.EventType;
import io.vertx.mod.atom.modeling.element.DataRow;
import io.vertx.mod.atom.modeling.element.DataTpl;

import java.util.List;

/*
 * In：缩写I，表示输入
 * Out：缩写O，表示输出
 */
@SuppressWarnings("all")
public interface AoIo {
    static AoIo create(final EventType type) {
        /*
         * 注意，这里不可以开缓存，即使开缓存也不能按 type 来开
         * 如果这里按 type 开缓存会引起的问题：
         * 1）DataEvent 会共享 AoIo，如果共享过后，会导致跨 identifier 的交叉引用问题
         * 2）同样的，在这样的场景中，添加 / 删除 / 更新 会因为（单/批）的不同而导致共享主键问题
         * 3）共享主键后会导致空的 Duplicated 记录问题
         * ！！！这里的缓存不可以打开
         */
        if (EventType.SINGLE == type) {
            return new IoSingle();
        } else if (EventType.BATCH == type) {
            return new IoBatch();
        } else {
            return null;
        }
    }

    /*
     * 绑定数据模板
     */
    AoIo on(DataTpl tpl);

    /*
     * 替换数据行信息
     */
    AoIo on(List<DataRow> rows);

    /*
     * 读取数据行信息
     */
    List<DataRow> getRows();
    // -------------- 数据填充环节 ----------------

    /*
     * 清空所有 Rows
     * @return
     */
    boolean clearRows();

    /* 新创建：填充每一个主键 */
    <ID> AoIo keys(ID... keys);

    /* 新创建：填充多记录 */
    AoIo records(HRecord... records);

    // -------------- 追加 -----------------------
    /*
     * 追加：为每一行追加UUID主键（主要用于添加）
     **/
    AoIo uuid();
}
