package io.modello.specification.element;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 操作部分（后续再处理）
 *
 * @author lang : 2023-05-22
 */
public interface HOperation extends HNs {
    /**
     * 输入参数
     *
     * @return {@link List}
     */
    List<HFeature> input();

    /**
     * 输出参数
     *
     * @return {@link HFeature}
     */
    HFeature output();

    /**
     * 挂载到函数的方法中
     *
     * @return {@link Method}
     */
    Method method();
}
