package io.zero.fn;

/*
 * 不同于原始函数，核心的执行器函数接口版本
 * void -> ()
 * 不带任何参数执行且不带任何返回值，类似于发射型函数
 */
@FunctionalInterface
public interface Actuator {

    void execute();
}
