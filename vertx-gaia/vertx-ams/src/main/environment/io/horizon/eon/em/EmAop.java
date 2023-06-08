package io.horizon.eon.em;

/**
 * @author lang : 2023-06-03
 */
public final class EmAop {
    private EmAop() {
    }

    /**
     * 执行AOP的作用时间，全环境通用，使用单词 Effect 代表影响的意思，
     * 副作用、影响表示其组件生命周期的发生时间戳
     */
    public enum Effect {
        BEFORE,  // 之前执行
        AFTER,   // 之后执行
        AROUND,  // 前后执行
        NONE,    // 关闭不执行
    }

    public enum Robin {
        FIELD,      // 按字段分流
        COMPONENT,  // 按配置的组件进行分流
        NONE,       // 未设置分流器
    }
}
