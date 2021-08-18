package io.vertx.tp.modular.dao.internal;

import io.vertx.tp.atom.refine.Ao;
import io.vertx.tp.modular.jooq.internal.Jq;
import io.vertx.up.commune.Record;

/**
 * 工具类
 * 1. 支持 INSERT
 * 2. 支持 UPDATE
 * 3. 支持 DELETE
 */
public class Partakor extends AbstractUtil<Partakor> {

    private Partakor() {
    }

    public static Partakor create() {
        return new Partakor();
    }

    public Record insert(final Record record) {
        Ao.infoSQL(this.getLogger(), "执行方法：Partakor.insert(Record)");

        return Jq.outR(this.idUUID(record), this.jooq::insert);
    }

    public Record[] insert(final Record... records) {
        Ao.infoSQL(this.getLogger(), "执行方法：Partakor.insert(Record...)");

        return Jq.outRs(this.idUUIDs(records), this.jooq::insertBatch);
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

        return Jq.outB(this.idRecord(record), this.jooq::delete);
    }

    public Boolean delete(final Record... records) {
        Ao.infoSQL(this.getLogger(), "执行方法：Partakor.delete(Record...)");
        /* 解析参数，生成 Arguments */
        return Jq.outB(this.idRecords(records), this.jooq::deleteBatch);
    }
}
