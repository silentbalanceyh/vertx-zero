package io.vertx.tp.modular.dao.internal;

import io.vertx.tp.atom.modeling.data.DataEvent;
import io.vertx.tp.atom.refine.Ao;
import io.vertx.tp.modular.jooq.internal.Jq;
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
        Ao.infoSQL(this.getLogger(), "执行方法：Partakor.insert(Record)");

        return Jq.outR(this.uuid(record), this.jooq::insert);
    }

    public Record[] insert(final Record... records) {
        Ao.infoSQL(this.getLogger(), "执行方法：Partakor.insert(Record...)");

        return Jq.outRs(this.uuids(records), this.jooq::insertBatch);
    }

    public Record update(final Record record) {
        Ao.infoSQL(this.getLogger(), "执行方法：Partakor.update(Record)");
        return Jq.outR(this.record(record), this.jooq::update);
    }

    public Record[] update(final Record... records) {
        Ao.infoSQL(this.getLogger(), "执行方法：Partakor.update(Record...)");
        return Jq.outRs(this.records(records), this.jooq::updateBatch);
    }

    public Boolean delete(final Record record) {
        Ao.infoSQL(this.getLogger(), "执行方法：Partakor.delete(Record)");

        return Jq.outB(this.key(record), this.jooq::delete);
    }

    public Boolean delete(final Record... records) {
        Ao.infoSQL(this.getLogger(), "执行方法：Partakor.delete(Record...)");
        /* 解析参数，生成 Arguments */
        return Jq.outB(this.keys(records), this.jooq::deleteBatch);
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
