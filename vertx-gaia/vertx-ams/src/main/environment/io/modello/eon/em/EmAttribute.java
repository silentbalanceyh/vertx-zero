package io.modello.eon.em;

/**
 * @author lang : 2023-05-31
 */
public final class EmAttribute {
    private EmAttribute() {
    }

    /**
     * <pre><code>
     *     索引值 | 对应属性 | 说明
     *       0      active    true - 启用，false - 禁用
     *       1      track     true - 跟踪，false - 不跟踪
     *                        跟踪属性才可以生成日志信息
     *       2      lock      true - 锁定，false - 不锁定
     *                        锁定属性才可以执行更改
     *       3      confirm   「集成」true - 开启待确认，false - 不开启
     *                        开启待确认的属性可支持二次确认流程
     *       4      array     true - 数组，false - 非数组
     *                        当前属性是否数组属性（新版可去掉，依赖 MetaField ）
     *       5      syncIn    「集成」true - 开启同步读，false - 不开启  pull
     *                        只有开启同步读的属性才能在集成过程中支持从外部数据源读取数据到当前系统中
     *       6      syncOut   「集成」true - 开启同步写，false - 不开启  push
     *                        只有开启同步写的属性才能在集成过程中支持从当前系统推送数据到外部数据源中
     *       7      refer     是否引用属性，true - 引用，false - 非引用
     *                        引用属性分两种出现在系统中
     *                        - ACTUAL：真实引用，当前属性出现在外键的属性集中
     *                        - VIRTUAL：虚拟引用，当前属性会被动被引用，如 linker 中的填充型属性
     * </code></pre>
     *
     * @author lang : 2023-05-09
     */
    public enum Marker {
        active,     // 0,  active
        track,      // 1,  track
        lock,       // 2,  lock
        confirm,    // 3,  confirm
        array,      // 4,  array
        syncIn,     // 5,  syncIn
        syncOut,    // 6,  syncOut
        refer;      // 7,  refer

        public static String[] NAMES = new String[]{
            active.name(),
            track.name(),
            lock.name(),
            confirm.name(),
            array.name(),
            syncIn.name(),
            syncOut.name(),
            refer.name()
        };
    }

    /**
     * 属性的三种来源
     * <pre><code>
     *     - INTERNAL：自身数据源
     *     - EXTERNAL：第三方数据源
     *     - REFERENCE：引用专用
     * </code></pre>
     *
     * @author <a href="http://www.origin-x.cn">Lang</a>
     */
    public enum Type {
        /*
         * Business Database
         * 自身数据源
         */
        INTERNAL,
        /*
         * External Data Source
         * 第三方数据源
         */
        EXTERNAL,
        /*
         * Reference Data Source
         * 引用专用
         */
        REFERENCE,
    }
}
