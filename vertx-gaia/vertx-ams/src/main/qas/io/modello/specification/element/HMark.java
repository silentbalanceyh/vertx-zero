package io.modello.specification.element;

/**
 * 「标记位」
 * <hr/>
 * 不同维度的描述信息
 * <pre><code>
 *     isAbstract：是否抽象，abstract 关键字
 *     isFinal：是否终态，final 关键字
 *     isStatic：是否静态，static 关键字
 *     isSealed：是否验证，sealed 关键字
 * </code></pre>
 *
 * @author lang : 2023-05-22
 */
public interface HMark {
    /**
     * 是否抽象
     *
     * @return boolean
     */
    boolean isAbstract();

    /**
     * 是否终态
     *
     * @return boolean
     */
    boolean isFinal();

    /**
     * 是否静态
     *
     * @return boolean
     */
    boolean isStatic();

    /**
     * 是否 sealed
     *
     * @return boolean
     */
    boolean isSealed();
}
