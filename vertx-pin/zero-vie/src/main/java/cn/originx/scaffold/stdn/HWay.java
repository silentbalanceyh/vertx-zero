package cn.originx.scaffold.stdn;

import io.vertx.core.Future;
import io.vertx.tp.atom.modeling.data.DataAtom;
import io.vertx.up.atom.record.Apt;
import io.vertx.up.commune.ActIn;
import io.vertx.up.commune.ActOut;

/**
 * 「内部流程」用于描述内部处理流程
 *
 * 标准化流程函数：
 * 1. 「子类」旧数据读取：transferIn(ActIn)
 * 2. 「子类」核心主逻辑方法：transferAsync(Apt, ActIn, DataAtom)
 * 3. 「子类」输出转换方法：transferOut(T)
 *
 * 直接覆盖主逻辑：
 * -  「子类」核心函数可覆盖：transferAsync(ActIn)
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface HWay<T, ID> {
    /**
     * 构造 Apt 专用输入方法（更新专用）
     *
     *
     * - original：旧数据
     * - current：新数据
     *
     * 默认行为（不重写）：ADD添加模式
     *
     * - 旧值：null
     * - 新值：request.getJArray() / request.getJObject()
     *
     * @param request {@link ActIn} 输入业务请求
     *
     * @return {@link io.vertx.core.Future}<{@link Apt}>
     */
    Future<Apt> transferIn(ActIn request);

    /**
     * 二选一的重写方法
     *
     * 方法一：直接重写顶层主方法`Future<ActOut> transferAsync(final ActIn request)`
     * 方法二：重写当前方法`(final Apt Apt, final ActIn request, final DataAtom atom)`（带比对结果）
     *
     * T = JsonObject / JsonArray
     *
     * @param apt     {@link Apt} 核心容器
     * @param request {@link ActIn} 输入业务请求
     * @param atom    {@link DataAtom} 模型定义
     *
     * @return {@link io.vertx.core.Future}<{@link T}>
     */
    Future<T> transferAsync(Apt apt, ActIn request, DataAtom atom);

    /**
     * 重写顶层方法，为当前通道组件的主方法
     *
     * - ADD -> 添加模式
     * - DELETE -> 删除模式
     * - UPDATE -> 更新模式
     *
     * 在调用 Apt 的 data() 时，其优先级为：
     *
     * - ADD -> 右值（新数据）
     * - DELETE -> 左值（旧数据）
     * - UPDATE -> 合并值（合并数据）
     *
     * ### 主逻辑：
     *
     * 2. 先执行{@link Apt}的基础运行
     * 3. 根据输入数据{@link T}执行动态`DataAtom`切换
     * 4. 运行主流程<strong>transferAsync(HRunner)</strong>
     * 5. 执行输出操作<strong>transferOut</strong>
     *
     * @param request {@link ActIn} 默认传入请求
     *
     * @return {@link io.vertx.core.Future}<{@link ActOut}>
     */
    Future<ActOut> transferAsync(ActIn request);

    /**
     * 输出方法（默认什么都不做）
     *
     * 阶段整理：
     *
     * 1. Before插件
     * 2. 主流程Execution
     * 3. After插件
     * 4. （当前步骤）最终回调
     *
     * 专用回调方法，该方法为<strong>最终阶段</strong>，用于解决字段两次翻译的BUG，在主流程执行时不执行任何
     * 字典翻译的动作，而是在后续执行流程中引入该逻辑，防止在After插件之前就执行了最终翻译。（默认直接返回输入数据）
     *
     * @param input {@link T} 执行完成后的善后数据
     *
     * @return {@link io.vertx.core.Future}<{@link T}>
     */
    Future<T> transferOut(final T input);

    // ------------------ Fetch专用 ----------------

    /**
     * 「Step 1」根据主键执行单记录读取
     *
     * @param key {@link java.lang.String} 字符串主键
     *
     * @return {@link io.vertx.core.Future}<{@link T}>
     */
    Future<T> fetchByKey(ID key);

    /**
     * 「Step 2」根据数据记录执行动态`DataAtom`操作，读取核心数据记录
     *
     * > 内部调用`completer`构造模型读取器
     *
     * @param data {@link T} 输入记录
     *
     * @return {@link io.vertx.core.Future}<{@link T}>
     */
    Future<T> fetchByData(T data);

    /**
     * 全记录读取
     *
     * 1. 先调用`fetchAsync(String)`。
     * 2. 再调用`fetchAsync(T)`。
     *
     * 示例：
     *
     * 1. 先读取 ci.device 的记录
     * 2. 其次根据 data 切换 indent 交换 Atom
     * 3. 根据新的 Atom 读取核心数据信息
     *
     * @param key {@link java.lang.String} 字符串主键
     *
     * @return {@link io.vertx.core.Future}<{@link T}>
     */
    Future<T> fetchFull(ID key);
}
