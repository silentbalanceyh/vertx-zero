package io.horizon.specification.typed;

import io.vertx.core.Future;

/**
 * 「组合器」
 * <hr/>
 * 组合器现阶段版本主要用于 Ui 界面的配置组合，会根据不同的配置生成不同的组合结果
 * <pre><code>
 *     1. 针对List类型的组件：
 *        - UI_LIST, V_QUERY, V_SEARCH, V_TABLE, V_FRAGMENT
 *     2. 针对Form类型的组件：
 *        - UI_FORM, UI_FIELD
 *     3. 操作独立组合：UI_OP
 * </code></pre>
 *
 * @param <T> 输入参数类型
 */
public interface TCombiner<T> {
    /**
     * 组合器核心方法，根据传入的参数执行组合
     *
     * @param input 输入参数，参数中包含了所有待组合的数据，使用不同 key 加以区分
     *
     * @return 返回异步结果
     */
    Future<T> executeAsync(T input);
}
