package io.modello.eon;

/**
 * @author lang : 2023-05-08
 */
public interface VAtom {
    /**
     * 属性标记，跟着版本执行更新，追加标记时再执行处理
     * 1. 标记本身带有顺序
     * 2. 标记只能使用在固定场景中
     */
    interface Mark {

    }
}
