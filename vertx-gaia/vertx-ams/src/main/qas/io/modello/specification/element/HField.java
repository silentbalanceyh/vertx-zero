package io.modello.specification.element;

/**
 * @author lang : 2023-05-22
 */
public interface HField extends HFeature {
    /**
     * 属性别名
     *
     * @return {@link String}
     */
    String alias();

    /**
     * 属性是否必须
     *
     * @return {@link Boolean}
     */
    boolean isRequired();
}
