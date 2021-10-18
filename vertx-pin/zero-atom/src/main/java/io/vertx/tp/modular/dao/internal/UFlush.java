package io.vertx.tp.modular.dao.internal;

import io.vertx.tp.atom.modeling.data.DataEvent;
import io.vertx.tp.atom.refine.Ao;
import io.vertx.up.commune.Record;

import java.util.Arrays;

/**
 * 工具类
 * 1. 支持 INSERT
 * 2. 支持 UPDATE
 * 3. 支持 DELETE
 */
public class UFlush extends AbstractUtil<UFlush> {

    private UFlush() {
    }

    public static UFlush create() {
        return new UFlush();
    }

    public Record insert(final Record record) {
        Ao.infoSQL(this.getLogger(), "执行方法：UFlush.insert(Record)");
        // Input
        final DataEvent input = this.uuid(record);
        // Output
        return this.output(input, this.jooq::insert, false);
    }

    public Record[] insert(final Record... records) {
        Ao.infoSQL(this.getLogger(), "执行方法：UFlush.insert(Record...)");
        // Input
        final DataEvent input = this.uuids(records);
        // Output
        return this.output(input, this.jooq::insertBatch, true);
    }

    public Record update(final Record record) {
        Ao.infoSQL(this.getLogger(), "执行方法：UFlush.update(Record)");
        // Input
        final DataEvent input = this.record(record);
        // Output
        return this.output(input, this.jooq::update, false);
    }

    public Record[] update(final Record... records) {
        Ao.infoSQL(this.getLogger(), "执行方法：UFlush.update(Record...)");
        // Input
        final DataEvent input = this.records(records);
        // Output
        return this.output(input, this.jooq::updateBatch, true);
    }

    public Boolean delete(final Record record) {
        Ao.infoSQL(this.getLogger(), "执行方法：UFlush.delete(Record)");
        // Input
        final DataEvent input = this.key(record);
        // Output
        return this.jooq.delete(input).succeed();
    }

    public Boolean delete(final Record... records) {
        Ao.infoSQL(this.getLogger(), "执行方法：UFlush.delete(Record...)");
        /* Input 解析参数，生成 Arguments */
        final DataEvent input = this.keys(records);

        return this.jooq.deleteBatch(input).succeed();
    }

    // ----------------------- Private ----------------------
    /*
     * 更新专用，原始拷贝
     */
    private DataEvent record(final Record record) {
        return this.event().records(record);
    }

    private DataEvent records(final Record... records) {
        return this.events().records(records);
    }

    /*
     * 起点：为当前 DataEvent 中绑定的 Record
     */
    private DataEvent uuid(final Record records) {
        return this.event().records(records).uuid();
    }

    private DataEvent uuids(final Record... records) {
        return this.events().records(records).uuid();
    }

    /*
     * 起点：通过传入的 Record 列表生成带有主键的 DataEvent
     */
    private DataEvent key(final Record record) {
        final Object[] ids = new Object[]{record.key()};
        return this.event().keys(ids);
    }

    private DataEvent keys(final Record... records) {
        final Object[] ids = Arrays.stream(records)
            .map(Record::key).toArray();
        return this.events().keys(ids);
    }
}
