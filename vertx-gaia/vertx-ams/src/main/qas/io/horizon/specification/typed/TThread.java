package io.horizon.specification.typed;

/**
 * 「线程包装器」
 * <hr/>
 * 托管系统中的线程信息，将 {@link Thread#getName()} 的原始的线程类合并封装，以确认日志系统和
 * 监控系统可以直接监控带有名称的线程信息，并对线程内对象进行引用托管，在容器启动时可实现针对启动
 * 对象的 Metadata 级别的监控行为，防止日志系统、链路系统无法捕捉内存中对象发生。
 *
 * @param <T>
 */
public interface TThread<T> extends Runnable {
    /**
     * 当前线程执行完成之后，返回执行结果，提取线程内执行对象。
     *
     * @return 执行结果
     */
    T get();

    /**
     * 返回当前线程名称，用于日志系统和监控系统的线程名称识别
     *
     * @return 当前线程名称
     */
    String name();
}
