package io.mature.extension.scaffold.stdn;

import io.horizon.spi.robin.Switcher;
import io.mature.extension.refine.Ox;
import io.mature.extension.scaffold.plugin.AspectSwitcher;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.atom.modeling.builtin.DataAtom;
import io.vertx.mod.atom.modeling.data.DataGroup;
import io.vertx.up.commune.ActIn;
import io.vertx.up.commune.ActOut;
import io.vertx.up.commune.record.Apt;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractHMore extends AbstractHub implements HWay<JsonArray, String[]> {

    // ------------------ 主逻辑 ------------------
    @Override
    public Future<Apt> transferIn(final ActIn request) {
        /*
         * 读取数据专用的前置操作
         * - 如果不重写，那么该数据就为 request.getJArray()
         * - 如果重写，那么会在主流程之前调用
         */
        final JsonArray input = request.getJArray();
        return Ux.future(Apt.create(null, input));
    }

    @Override
    public Future<JsonArray> transferAsync(final Apt apt, final ActIn request, final DataAtom atom) {
        return this.transferFailure();
    }

    @Override
    public Future<ActOut> transferAsync(final ActIn request) {
        /*
         * 主流程，调用 transferIn
         * ActIn -> Apt
         */
        return this.transferIn(request)
            /*
             * 主逻辑
             */
            .compose(apt -> this.transferAsync(apt, request))
            /*
             * 调用最终执行流程
             */
            .compose(this::transferOut)
            .compose(ActOut::future);
    }

    @Override
    public Future<JsonArray> transferAsync(final Apt apt, final ActIn request) {
        final JsonArray calculation = this.preprocessDefault(apt);
        return this.atom(calculation).compose(groupSet -> Ox.runGroup(groupSet, (input, atom) -> {
            final AspectSwitcher aspect = new AspectSwitcher(atom, this.options(), this.fabric(atom));
            return aspect.run(input, processed -> {
                final Apt created = HDiff.createApt(apt, input, this.diffKey());
                created.set(processed.copy());
                return this.transferAsync(created, request, atom);
            });
        }));
    }


    /**
     * @param apt {@link Apt} 传入比对容器
     *
     * @return {@link JsonArray} 默认数据信息，主要针对 UPDATE 模式切换
     */
    @Override
    public JsonArray preprocessDefault(final Apt apt) {
        return apt.dataI();
    }

    @Override
    public Future<JsonArray> transferOut(final JsonArray input) {
        return Ux.futureA();
    }

    @Override
    public Future<JsonArray> fetchByKey(final String[] keys) {
        return this.dao().fetchByIdAsync(keys).compose(Ux::futureA);
    }

    @Override
    public Future<JsonArray> fetchFull(final String[] keys) {
        return this.fetchByKey(keys).compose(this::fetchByData);
    }

    @Override
    public Future<JsonArray> fetchByData(final JsonArray data) {
        return this.atom(data).compose(this.setTA(group -> this.completer(group.atom()).find(group.data())));
    }

    /**
     * 动态 atom / dao 专用方法，可切换分组信息。
     *
     * 基础执行流程
     *
     * 1. Switcher = null，根据`internalAtom`构造分组。a
     * 2. Switcher not null，直接调用标识规则选择器执行分组。
     *
     * @param input {@link JsonArray} 输入数组数据
     *
     * @return {@link io.vertx.core.Future}
     */
    protected Future<Set<DataGroup>> atom(final JsonArray input) {
        final Switcher switcher = this.switcher();
        if (Objects.isNull(switcher)) {
            /*
             * 为空走默认
             */
            return Ux.future(Ox.toGroup(this.atom(), input));
        } else {
            return this.switcher().atom(input, this.atom());
        }
    }

    // ------------------ 子类共享函数 ----------------

    protected Future<JsonArray> inImport(final JsonArray data, final String uniqueKey) {
        return this.atom(data).compose(this.setTA(group -> {
            final JsonArray groupData = group.data();
            final Set<String> values = Ut.valueSetString(groupData, uniqueKey);
            /*
             * 条件构造
             */
            final JsonObject condition = new JsonObject();
            condition.put(uniqueKey + ",i", Ut.toJArray(values));
            return this.dao(group.atom()).fetchAsync(condition).compose(Ux::futureA);
        }));
    }

    /*
     *
     * Async Utility X for Function Only
     *
     * This tool is critical for function management and scheduled in different code flow here.
     * It's a little duplicated to `Ux.then` methods, but no impact here for future usage, in future versions
     * I'll remove all the function part from `Ux`, instead, the Ax is more usage.
     *
     * Here the name are as following:
     *
     * A: JsonArray
     * J: JsonObject
     * T: Generic T
     * S: Set
     * L: List
     *
     * All the returned value must be Function and the returned value should be Future type.
     *
     * i -> Future(o)
     *
     * i - input
     * o - output
     *
     * The name specification is as following:
     *
     * o - One
     * m - Many ( A, S, L )
     *
     * Recommend you to use non-Ax API ( Instead you should use Ux/Ut/Fn )
     *
     * Ax tool is used in .compose only to remove `->`.
     *
     *
     * Set<T> -> T -> Future<JsonArray>
     */
    private <T> Function<Set<T>, Future<JsonArray>> setTA(final Function<T, Future<JsonArray>> consumer) {
        return set -> {
            final List<Future<JsonArray>> futures = new ArrayList<>();
            set.forEach(item -> futures.add(consumer.apply(item)));
            return Fn.compressA(futures);
        };
    }
}
