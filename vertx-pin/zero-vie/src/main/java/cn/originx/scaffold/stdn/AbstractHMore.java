package cn.originx.scaffold.stdn;

import cn.originx.refine.Ox;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.tp.atom.modeling.data.DataAtom;
import io.vertx.tp.atom.modeling.data.DataGroup;
import io.vertx.tp.optic.robin.Switcher;
import io.vertx.up.atom.record.Apt;
import io.vertx.up.commune.ActIn;
import io.vertx.up.unity.Ux;

import java.util.Objects;
import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractHMore extends AbstractHub implements HWay<JsonArray> {

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
    public Future<JsonArray> transferAsync(final Apt Apt, final ActIn request, final DataAtom atom) {
        return this.transferFailure();
    }

    @Override
    public Future<JsonArray> transferOut(final JsonArray input) {
        return Ux.futureA();
    }
    // ------------------ 特殊逻辑 ----------------

    /**
     * 动态 atom / dao 专用方法，可切换分组信息。
     *
     * 基础执行流程
     *
     * 1. Switcher = null，根据`internalAtom`构造分组。
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
}
