package io.vertx.tp.modular.dao.internal;

import io.vertx.tp.atom.cv.em.EventType;
import io.vertx.tp.atom.modeling.data.DataAtom;
import io.vertx.tp.atom.modeling.data.DataEvent;
import io.vertx.tp.error._417DataAtomNullException;
import io.vertx.tp.modular.jdbc.AoConnection;
import io.vertx.tp.modular.jooq.JQEngine;
import io.vertx.tp.modular.metadata.AoSentence;
import io.vertx.up.atom.query.Criteria;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;

/**
 * 抽象工具类
 */
@SuppressWarnings("unchecked")
public abstract class AbstractUtil<T extends AoBinder> implements AoBinder<T> {
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
    protected DataEvent event() {
        /* 检查 this.io.vertx.up.atom / this.sentence */
        Fn.outWeb(null == this.atom, _417DataAtomNullException.class, this.getClass());
        return DataEvent.create(this.atom, this.sentence).init(EventType.SINGLE);
    }

    DataEvent events() {
        Fn.outWeb(null == this.atom, _417DataAtomNullException.class, this.getClass());
        return DataEvent.create(this.atom, this.sentence).init(EventType.BATCH);
    }

    <ID> DataEvent irIDs(final ID... ids) {
        return this.events().keys(ids);
    }

    DataEvent irCond(final Criteria criteria) {
        return this.event().criteria(criteria);
    }

    protected Annal getLogger() {
        return Annal.get(this.getClass());
    }
}
