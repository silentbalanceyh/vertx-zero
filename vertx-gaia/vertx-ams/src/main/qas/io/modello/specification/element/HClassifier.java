package io.modello.specification.element;

import java.util.List;

/**
 * 「类定义」
 *
 * @author lang : 2023-05-22
 */
public interface HClassifier extends HModifier, HType {
    /**
     * 当前类定义所属的包
     *
     * @return {@link HPackage}
     */
    HPackage pkg();

    /**
     * 当前模型的成员集，开启遍历专用
     *
     * @return {@link List}
     */
    default List<HField> field() {
        return List.of();
    }

    /**
     * 当前模型的操作集，开启遍历专用
     *
     * @return {@link List}
     */
    default List<HOperation> operation() {
        return List.of();
    }

    /**
     * 父类
     *
     * @return {@link HClassifier}
     */
    HClassifier extend();

    /**
     * 接口
     *
     * @return {@link HClassifier}
     */
    HClassifier[] implement();
}
