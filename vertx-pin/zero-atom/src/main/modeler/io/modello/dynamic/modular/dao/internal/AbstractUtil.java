package io.modello.dynamic.modular.dao.internal;

import io.horizon.uca.log.Annal;
import io.horizon.uca.qr.Criteria;
import io.modello.dynamic.modular.jdbc.AoConnection;
import io.modello.dynamic.modular.jooq.JQEngine;
import io.modello.dynamic.modular.metadata.AoSentence;
import io.modello.specification.atom.HAtom;
import io.vertx.mod.atom.cv.em.EventType;
import io.vertx.mod.atom.error._417DataAtomNullException;
import io.vertx.mod.atom.modeling.builtin.DataAtom;
import io.vertx.mod.atom.modeling.data.DataEvent;
import io.vertx.up.fn.Fn;

import java.util.function.Function;

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
    public T on(final HAtom atom) {
        /*
         * TODO: 此处有一个强制转换，目前版本中只使用 DataAtom，后期更改
         * */
        this.atom = (DataAtom) atom;
        return (T) this;
    }

    // ---------------------------
    protected DataEvent event() {
        /* 检查 this.io.vertx.up.atom / this.sentence */
        Fn.outWeb(null == this.atom, _417DataAtomNullException.class, this.getClass());
        return DataEvent.create(this.atom, this.sentence).init(EventType.SINGLE);
    }

    protected DataEvent events() {
        Fn.outWeb(null == this.atom, _417DataAtomNullException.class, this.getClass());
        return DataEvent.create(this.atom, this.sentence).init(EventType.BATCH);
    }

    protected <ID> DataEvent irIDs(final ID... ids) {
        return this.events().keys(ids);
    }

    protected DataEvent irCond(final Criteria criteria) {
        return this.event().criteria(criteria);
    }

    // Output Record, Record[]
    @SuppressWarnings("all")
    protected <T> T output(final DataEvent event, final Function<DataEvent, DataEvent> executor, final boolean isArray) {
        event.consoleAll();
        final DataEvent response = executor.apply(event);
        if (isArray) {
            return (T) response.dataA();
        } else {
            return (T) response.dataR();
        }
    }


    protected Annal getLogger() {
        return Annal.get(this.getClass());
    }
}
