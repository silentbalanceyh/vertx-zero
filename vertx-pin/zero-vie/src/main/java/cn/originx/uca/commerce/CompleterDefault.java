package cn.originx.uca.commerce;

import io.horizon.specification.modeler.HDao;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.builtin.DataAtom;
import io.horizon.spi.robin.Switcher;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class CompleterDefault implements Completer {

    /*
     * 两个构造函数
     * 1 - 静态读写专用
     * 2 - 动态读写专用
     * 两个变量
     * 1 - single 单记录处理
     * 2 - batch 多记录处理
     */
    protected CompleterIo<JsonObject> single;
    protected CompleterIo<JsonArray> batch;

    public CompleterDefault(final HDao dao, final DataAtom atom) {
        this.single = new CompleterIoOne(dao, atom);
        this.batch = new CompleterIoMore(dao, atom);
    }

    @Override
    public Completer bind(final Switcher switcher) {
        this.single.bind(switcher);
        this.batch.bind(switcher);
        return this;
    }

    @Override
    public Future<JsonArray> create(final JsonArray records) {
        return this.completerA().create(records);
    }

    @Override
    public Future<JsonObject> create(final JsonObject record) {
        return this.completerJ().create(record);
    }

    @Override
    public Future<JsonArray> update(final JsonArray records) {
        return this.completerA().update(records);
    }

    @Override
    public Future<JsonObject> update(final JsonObject record) {
        return this.completerJ().update(record);
    }

    @Override
    public Future<JsonArray> remove(final JsonArray records) {
        return this.completerA().remove(records);
    }

    @Override
    public Future<JsonObject> remove(final JsonObject record) {
        return this.completerJ().remove(record);
    }

    @Override
    public Future<JsonObject> find(final JsonObject record) {
        return this.completerJ().find(record);
    }

    @Override
    public Future<JsonArray> find(final JsonArray records) {
        return this.completerA().find(records);
    }

    protected CompleterIo<JsonObject> completerJ() {
        return this.single;
    }

    protected CompleterIo<JsonArray> completerA() {
        return this.batch;
    }
}
