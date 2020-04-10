package io.vertx.tp.modular.dao.internal;

import io.vertx.tp.atom.cv.em.EventType;
import io.vertx.tp.atom.modeling.data.DataAtom;
import io.vertx.tp.atom.modeling.data.DataEvent;
import io.vertx.tp.error._417DataAtomNullException;
import io.vertx.tp.modular.jdbc.AoConnection;
import io.vertx.tp.modular.jooq.JQEngine;
import io.vertx.tp.modular.metadata.AoSentence;
import io.vertx.up.atom.query.Criteria;
import io.vertx.up.atom.query.Inquiry;
import io.vertx.up.commune.Record;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;

import java.util.Arrays;

/**
 * 抽象工具类
 */
@SuppressWarnings("unchecked")
public abstract class AbstractUtil<T extends AoBinder> implements AoBinder<T> {

    // 专用的Builder

    // 子类继承
    protected transient AoConnection connection;
    protected transient AoSentence sentence;
    // 元数据
    protected transient DataAtom atom;
    protected transient JQEngine jooq;

    @Override
    public T on(final AoSentence sentence) {
        this.sentence = sentence;
        this.jooq.bind(sentence);       // 绑定 AoSentence
        return (T) this;
    }

    @Override
    public T on(final AoConnection connection) {
        this.connection = connection;
        this.jooq = JQEngine.create(connection.getDSL());
        /* 绑定DSLContext */
        return (T) this;
    }

    @Override
    public T on(final DataAtom atom) {
        /* 处理特殊的 语句处理器 */
        this.atom = atom;
        return (T) this;
    }

    // ---------------------------
    private DataEvent event() {
        /* 检查 this.io.vertx.up.atom / this.sentence */
        Fn.outWeb(null == this.atom, _417DataAtomNullException.class, this.getClass());
        return DataEvent.create(this.atom, this.sentence)
                .init(EventType.SINGLE);
    }

    DataEvent events() {
        Fn.outWeb(null == this.atom, _417DataAtomNullException.class, this.getClass());
        return DataEvent.create(this.atom, this.sentence)
                .init(EventType.BATCH);
    }

    /*
     * 更新专用，原始拷贝
     */
    DataEvent record(final Record record) {
        return this.event().records(record);
    }

    DataEvent records(final Record... records) {
        return this.events().records(records);
    }

    /*
     * 起点：为当前 DataEvent 中绑定的 Record
     */
    DataEvent idUUID(final Record records) {
        return this.event().records(records).uuid();
    }

    DataEvent idUUIDs(final Record... records) {
        return this.events().records(records).uuid();
    }

    /*
     * 起点：仅生成绑定了 ids 的 DataEvent
     */
    <ID> DataEvent idInput(final ID ids) {
        return this.event().keys(ids);
    }

    <ID> DataEvent idInputs(final ID... ids) {
        return this.events().keys(ids);
    }

    /*
     * 起点：通过传入的 Record 列表生成带有主键的 DataEvent
     */
    DataEvent idRecord(final Record record) {
        final Object[] ids = new Object[]{record.key()};
        return this.event().keys(ids);
    }

    DataEvent idRecords(final Record... records) {
        final Object[] ids = Arrays.stream(records)
                .map(Record::key).toArray();
        return this.events().keys(ids);
    }

    DataEvent irCond(final Criteria criteria) {
        return this.event().criteria(criteria);
    }

    DataEvent irInquiry(final Inquiry inquiry) {
        return this.event().inquiry(inquiry);
    }

    protected Annal getLogger() {
        return Annal.get(this.getClass());
    }
}
