package cn.originx.uca.concrete;

import io.modello.specification.HRecord;
import io.modello.specification.action.HDao;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.builtin.DataAtom;
import io.vertx.tp.atom.refine.Ao;
import io.vertx.up.annotations.Contract;

import java.util.Objects;

/**
 * 顶层抽象层，不带关系的动态建模部分专用抽象层处理
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractAgile implements Arrow {
    @Contract
    private transient DataAtom atom;

    @Contract
    private transient HDao dao;

    protected DataAtom atom() {
        return this.atom;
    }

    protected HDao dao() {
        /*
         * 这里必须执行 Mount 操作切换 DataAtom 的信息
         * 否则会出现两个写一个的混用情况
         */
        return this.dao.mount(this.atom);
    }

    protected String sigma() {
        return Objects.isNull(this.atom) ? null : this.atom.sigma();
    }

    protected String identifier() {
        return Objects.isNull(this.atom) ? null : this.atom.identifier();
    }

    /*
     * 记录构造专用（对子类隐藏 DataAtom）
     */
    protected HRecord record(final JsonObject recordJson) {
        return Ao.record(recordJson, this.atom);
    }

    protected HRecord[] records(final JsonArray recordJson) {
        return Ao.records(recordJson, this.atom);
    }
}
