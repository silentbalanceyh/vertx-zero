package cn.originx.scaffold.stdn;

import cn.originx.refine.Ox;
import cn.originx.scaffold.component.AbstractActor;
import cn.originx.uca.log.TrackIo;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.data.DataAtom;
import io.vertx.tp.atom.refine.Ao;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.optic.Trash;
import io.vertx.up.commune.ActIn;
import io.vertx.up.commune.ActOut;
import io.vertx.up.commune.config.Integration;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;

import java.util.Objects;

/**
 * ## Ox平台顶层通道
 *
 * ### 1. 基本介绍
 *
 * 该组件为通道组件的顶层抽象组件，从ACTOR中继承，由于是ACTOR，所以包含：
 *
 * - 任务配置：{@link io.vertx.up.atom.worker.Mission}
 * - 集成配置：{@link io.vertx.up.commune.config.Integration}
 * - 数据库配置：{@link io.vertx.up.commune.config.Database}
 *
 * 除开上述配置以外，该组件中还新增了特殊成员配置
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AbstractHub extends AbstractActor {
    // ------------- 主流程执行代码 -------------

    /**
     * 默认请求处理（以未重写异常失败告终，主流程默认实现），子类必须实现该方法完成主流程的编程功能，
     *
     * 子类组件开发方法可选择如下：
     *
     * 1. 直接重写该方法（优先级最高）
     * 2. 重写直接子类的核心方法（带比对结果，次优先级）
     *
     * @param request {@link ActIn} 默认传入请求
     *
     * @return {@link io.vertx.core.Future}<{@link ActOut}> 业务层输出
     */
    @Override
    public Future<ActOut> transferAsync(final ActIn request) {
        return this.transferFailure();
    }

    // ------------- 基本对象处理 --------------------

    /**
     * 下层子类实现该方法返回集成对象，从父类的`protected`开放成`public`模式提供给子类使用，并且在子类中
     * 开放该属性的访问域信息。
     *
     * @return {@link io.vertx.up.commune.config.Integration} 集成对象
     */
    @Override
    public Integration integration() {
        return super.integration();
    }

    // ------------- 基本对象处理 --------------------

    /**
     * 数据合并专用功能，对`T`数据执行<strong>去空</strong>处理（移除掉`null`值）。
     *
     * - 如果是{@link io.vertx.core.json.JsonObject}，则直接去掉`field = null`部分的值。
     * - 如果是{@link io.vertx.core.json.JsonArray}，针对每一个元素去掉`field = null`部分的值。
     *
     * > 内部调用`combineEdit`方法。
     *
     * @param data 输入数据信息
     * @param atom {@link DataAtom} 模型定义对象
     * @param <T>  内容对象，通常为{@link io.vertx.core.json.JsonArray}或{@link io.vertx.core.json.JsonObject}
     *
     * @return 返回执行完成后的内容对象
     */
    protected <T> T diffAdd(final T data, final DataAtom atom) {
        return HDiff.execute(data, null, atom);
    }

    /**
     * 该方法和`combineEdit`方法一前一后合并使用，为「清空」做准备，该数据可<strong>规范化</strong>输入数据
     *
     * 1. 遍历`original`中的所有属性`field`。
     * 2. 提取`current`输入中没有的属性，并设置成<strong>null</strong>值。
     * 3. 最终属性为合并属性。
     *
     * 由于该步骤是`UPDATE`更新的前置步骤，所以会根据模型中的定义执行下边三种操作：
     *
     * - 「清空」清除原来有值的属性（`field = null`），本方法的核心逻辑。
     * - 「更新」更新原来有值的属性（直接合并可实现）。
     * - 「追加」追加原来无值的属性（直接合并可实现）。
     *
     * > 该方法有副作用，会改变`current`对象。
     *
     * @param original {@link io.vertx.core.json.JsonObject} 原始数据记录
     * @param current  {@link io.vertx.core.json.JsonObject} 最新数据记录（新输入）
     */
    protected void diffNull(final JsonObject original, final JsonObject current) {
        original.fieldNames().stream().filter(field -> !current.containsKey(field)).forEach(current::putNull);
    }

    /**
     * 该方法为更新过程中的专用方法，会执行核心的更新逻辑。
     *
     * <strong>第一层逻辑</strong>
     *
     * - 遍历{@link io.vertx.core.json.JsonObject}直接提取Json对象引用。
     * - 遍历{@link io.vertx.core.json.JsonArray}中的每一个对象，提取Json对象引用。
     * - 在每一个元素中执行 `Function<JsonObject,T>`函数。
     *
     * <strong>第二层逻辑</strong>
     *
     * 判断逻辑，根据输入的第二参数执行判断：
     *
     * - 「Creation」去掉所有`null`对象过后执行更新。
     * - 「Edition」执行第三层核心逻辑。
     *
     * <strong>第三层逻辑</strong>
     *
     * 从系统中提取所有不执行清空的属性`notnullFields`，这种类型的属性表如下：
     *
     * |属性名|备注|
     * |---:|:---|
     * |createdAt|创建时间。|
     * |createdBy|创建人。|
     * |updatedAt|更新时间。|
     * |updatedBy|更新人。|
     * |globalId|第三方集成专用全局ID（唯一键）。|
     * |confirmStatus|对象确认状态。|
     * |`inSync = false`|配置的不需要从集成端输入的属性。|
     *
     * <strong>第四层逻辑（位于`notnullFields`中）</strong>
     *
     * 1. 如果输入的`field = null`，则设置`normalized`中的`field = value`（原值），并且从原始记录中移除`field`。
     * 2. 如果输入的`field = value`，则直接将该值传入normalized中。
     *
     * <strong>第四层逻辑（不包含在`notnullFields`中）</strong>
     *
     * 1. 如果输入值中包含`field = value`，则更新`normalized`中的`value`值。
     * 2. 如果输入值中不包含`field`，则直接清空`normalized`中的值。
     *
     * > 最终计算的`normalized`会覆盖原始记录对象数据。
     *
     * @param original 原始数据
     * @param updated  {@link io.vertx.core.json.JsonObject} 待更新数据对象
     * @param atom     {@link DataAtom} 模型信息
     * @param <T>      内容对象，通常为{@link io.vertx.core.json.JsonArray}或{@link io.vertx.core.json.JsonObject}
     *
     * @return 返回执行完成后的内容对象
     */
    protected <T> T diffEdit(final T original, final JsonObject updated, final DataAtom atom) {
        return HDiff.execute(original, updated, atom);
    }

    /**
     * 默认字段名`key`，如果是其他属性，则重写该方法。
     *
     * @return {@link java.lang.String}
     */
    protected String diffKey() {
        return KName.KEY;
    }
    // ------------- 历史记录处理 -------------

    /**
     * 「删除备份」历史记录执行器，用于存储执行了物理删除的历史数据记录。
     *
     * 1. 启用了「删除历史」功能的数据可执行该操作。
     * 2. 原始库`DB_IOP`和`DB_IOP_HIS`，该方法会将数据记录存储到`DB_IOP_HIS`中执行备份。
     *
     * 该库中的数据记录会被备份到历史库，后续版本会有专程的模块来访问。
     *
     * @param input 被删除的数据记录
     * @param atom  {@link DataAtom} 模型信息
     * @param <T>   内容对象，通常为{@link io.vertx.core.json.JsonArray}或{@link io.vertx.core.json.JsonObject}
     *
     * @return {@link io.vertx.core.Future}
     */
    protected <T> Future<T> backupAsync(final T input, final DataAtom atom) {
        return this.transferAsync(input,

            /* JsonObject */
            data -> Ke.channelAsync(Trash.class,
                () -> Ux.future(data),
                stub -> stub.backupAsync(atom.identifier(), data)),

            /* JsonArray */
            data -> Ke.channelAsync(Trash.class,
                () -> Ux.future(data),
                stub -> stub.backupAsync(atom.identifier(), data))
        );
    }

    /**
     * 「A」`ADD`类型的变更历史。
     *
     * @param newR 新记录
     * @param atom {@link DataAtom} 模型信息
     * @param <T>  内容对象，通常为{@link io.vertx.core.json.JsonArray}或{@link io.vertx.core.json.JsonObject}
     *
     * @return {@link io.vertx.core.Future}
     */
    protected <T> Future<T> trackAsyncC(final T newR, final DataAtom atom) {
        return this.transferAsync(newR,

            /* JsonObject */
            data -> TrackIo.create(atom, this.dao(atom)).procAsync(data, null, this.options()),

            /* JsonArray */
            data -> TrackIo.create(atom, this.dao(atom)).procAsync(data, null, this.options(), data.size())
        );
    }

    /**
     * 「D」`DELETE`类型的变更历史。
     *
     * @param oldR 旧记录
     * @param atom {@link DataAtom} 模型信息
     * @param <T>  内容对象，通常为{@link io.vertx.core.json.JsonArray}或{@link io.vertx.core.json.JsonObject}
     *
     * @return {@link io.vertx.core.Future}
     */
    protected <T> Future<T> trackAsyncD(final T oldR, final DataAtom atom) {
        return this.transferAsync(oldR,

            /* JsonObject */
            data -> TrackIo.create(atom, this.dao(atom))
                .procAsync(null, data, this.options()),

            /* JsonArray */
            data -> TrackIo.create(atom, this.dao(atom))
                .procAsync(null, data, this.options(), data.size())
        );
    }

    /**
     * 「U」`UPDATE`类型的变更历史。
     *
     * @param oldR 旧记录
     * @param newR 新记录
     * @param atom {@link DataAtom} 模型信息
     * @param <T>  内容对象，通常为{@link io.vertx.core.json.JsonArray}或{@link io.vertx.core.json.JsonObject}
     *
     * @return {@link io.vertx.core.Future}
     */
    protected <T> Future<T> trackAsyncU(final T oldR, final T newR, final DataAtom atom) {
        return this.transferAsync(oldR,
            /* JsonObject */
            data -> {
                final JsonObject converted = (JsonObject) newR;
                final JsonObject compared = Ao.diffPure(data, converted, atom, Ox.ignorePure(atom));
                if (Objects.isNull(compared)) {
                    /* 没有变更不生成变更历史 */
                    return Ux.future(converted);
                } else {
                    /* 发生变更，生成变更历史 */
                    return TrackIo.create(atom, this.dao(atom)).procAsync(converted, data, this.options());
                }
            },

            /* JsonArray */
            data -> {
                /* 数组类型，内部会执行计算 */
                final JsonArray converted = (JsonArray) newR;
                return TrackIo.create(atom, this.dao(atom))
                    .procAsync(converted, data, this.options(), converted.size());
            }
        );
    }
}
