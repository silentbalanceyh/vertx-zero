package cn.originx.scaffold.stdn;

import cn.originx.scaffold.plugin.AspectSwitcher;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.builtin.DataAtom;
import io.vertx.tp.error._404RecordMissingException;
import io.vertx.tp.optic.robin.Switcher;
import io.vertx.up.atom.record.Apt;
import io.vertx.up.commune.ActIn;
import io.vertx.up.commune.ActOut;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.experiment.mixture.HDao;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractHOne extends AbstractHub implements HWay<JsonObject, String> {

    // ------------------ 主逻辑 ------------------
    @Override
    public Future<Apt> transferIn(final ActIn request) {
        /*
         * 读取数据专用的前置操作
         * - 如果不重写，那么该数据就为 request.getJObject()
         * - 如果重写，那么会在主流程之前调用
         */
        final JsonObject input = request.getJObject();
        return Ux.future(Apt.create(null, input));
    }

    @Override
    public Future<JsonObject> transferAsync(final Apt apt, final ActIn request, final DataAtom atom) {
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
    public Future<JsonObject> transferAsync(final Apt apt, final ActIn request) {
        final JsonObject calculation = this.preprocessDefault(apt);
        return this.atom(calculation).compose(atom -> {
            final AspectSwitcher aspect = new AspectSwitcher(atom, this.options(), this.fabric(atom));
            return aspect.run((JsonObject) apt.dataI(), processed -> {
                final JsonObject normalized;
                if (ChangeFlag.UPDATE == apt.type()) {
                    /* 4. 特殊更新 */
                    final JsonObject original = apt.dataO();
                    normalized = this.diffEdit(original, processed, atom);
                } else {
                    normalized = processed;
                }
                apt.set(normalized);
                return this.transferAsync(apt, request, atom);
            });
        });
    }

    @Override
    public JsonObject preprocessDefault(final Apt apt) {
        return apt.dataI();
    }

    @Override
    public Future<JsonObject> transferOut(final JsonObject input) {
        /*
         * Avoid there is no data in response
         * This operation is critical
         */
        return Ux.future(input);
    }

    @Override
    public Future<JsonObject> fetchByKey(final String key) {
        return this.dao().fetchByIdAsync(key).compose(Ux::futureJ);
    }

    @Override
    public Future<JsonObject> fetchByData(final JsonObject data) {
        return this.atom(data).compose(atom -> this.completer(atom).find(data));
    }

    @Override
    public Future<JsonObject> fetchFull(final String key) {
        return this.fetchByKey(key).compose(queried -> {
            if (Objects.isNull(this.switcher())) {
                /* Switcher Null, return the queried data directly */
                return Ux.future(queried);
            } else {
                /* Switcher Enabled */
                return this.fetchByData(queried);
            }
        });
    }

    // ------------------ 特殊逻辑 ----------------

    /**
     * 「Async」动态`DataAtom`选择，调用顶层的标识规则选择器
     *
     * 1. 根据输入数据记录{@link io.vertx.core.json.JsonObject}执行标识规则选择。
     * 2. 如果没有配置标识规则选择器，那么该方法会返回当前环境中的默认`internalAtom`。
     *
     * @param input {@link io.vertx.core.json.JsonObject} 数据记录
     *
     * @return {@link io.vertx.core.Future}<{@link DataAtom}>
     */
    protected Future<DataAtom> atom(final JsonObject input) {
        final Switcher switcher = this.switcher();
        if (Objects.isNull(switcher)) {
            /* 默认 DataAtom */
            return Ux.future(this.atom());
        } else {
            /* 执行标识规则选择器 */
            return this.switcher().atom(input, this.atom());
        }
    }

    /**
     * 「Async」动态`DataAtom`选择，根据输入的identifier创建合法模型
     *
     * 1. 创造的新的`DataAtom`和当前配置的`DataAtom`同属一个应用。
     * 2. 如果无法创建和`identifier`绑定的模型定义对象，直接返回null。
     *
     * @param identifier {@link java.lang.String} 模型统一标识符
     *
     * @return {@link io.vertx.core.Future}<{@link DataAtom}>
     */
    protected Future<DataAtom> atom(final String identifier) {
        final DataAtom atom = this.atom();
        final DataAtom actualAtom;
        if (Objects.nonNull(atom)) {
            actualAtom = atom.atom(identifier);
        } else {
            actualAtom = null;
        }
        return Ux.future(actualAtom);
    }

    /**
     * 「Async」动态`AoDao`数据访问器
     *
     * 内部调用`atom(JsonObject)`方法切换动态模型，然后使用动态模型构造`AoDao`的动态数据访问器。
     *
     * @param input {@link io.vertx.core.json.JsonObject} 数据记录
     *
     * @return {@link io.vertx.core.Future}<{@link HDao}>
     */
    protected Future<HDao> dao(final JsonObject input) {
        return this.atom(input).compose(atom -> Ux.future(this.dao(atom)));
    }

    // ------------------ 子类共享函数 ----------------

    protected Future<Apt> inUpdate(final ActIn request) {
        final String key = this.activeKey(request);
        return this.fetchFull(key).compose(original -> {
            if (Ut.isNil(original)) {
                /*
                 * 找不到记录的异常信息
                 */
                return Fn.error(_404RecordMissingException.class, this.getClass(), key);
            } else {
                /*
                 * 构造 Apt作为主流程中的参数
                 */
                final JsonObject current = request.getJObject();
                this.diffNull(original, current);
                return Ux.future(Apt.create(original, current));
            }
        });
    }
}
