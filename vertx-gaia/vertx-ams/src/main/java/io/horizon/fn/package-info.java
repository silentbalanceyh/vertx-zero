/**
 * 函数基础规范接口，追加
 * 原始：
 * - Consumer:
 * - Function:
 * - Predicate:
 * - Supplier:
 * 普通函数：
 * - Actuator    / 执行函数接口
 * - TiConsumer  / 三元 Consumer
 * 抽象类：
 * - EActuator
 * --- ErrorActuator            / 抛Throwable
 * --- ExceptionActuator        / 抛Exception
 * --- ProgramActuator          / 抛ProgramException
 * - EBiConsumer
 * --- ProgramBiConsumer        / 抛ProgramException
 * - EConsumer
 * --- ErrorConsumer            / 抛Throwable
 * --- ExceptionConsumer        / 抛Exception
 * - EFunction
 * --- ErrorFunction            / 抛Throwable
 * --- ExceptionFunction        / 抛Exception
 * - ESupplier
 * --- ErrorSupplier            / 抛Throwable
 * --- ExceptionSupplier        / 抛Exception
 * --- ProgramSupplier          / 抛ProgramException
 * 若想要补齐则参考基础规范，现阶段框架级并没有完成所有函数规范的补齐动作，从实际使用上也没有必要执行补齐的动作
 * 注意，扩展定义中，如果是 RuntimeException 没有必要定义函数式接口，JVM 语言规范本身就支持，并且 runtime 异常
 * 由于不需要再代码中使用 throws 做定义，所以该异常本身是不需要定义函数式接口的，但是如果是 Exception 则需要
 * 函数执行矩阵
 * 异常种类：Throwable / ProgramException / Exception
 * 函数种类：Actuator / Predicate / Consumer / Function / Supplier
 * 函数名基础规范：
 * - Actuator / Consumer 可重载，由于没有返回值，统一使用 At 后缀
 * - Supplier / Function 可重载，由于有返回值，统一使用 In 后缀
 * 三种异常的前缀：
 * - jvm: 来自于JVM级别的异常，异常顶层类 Throwable（范围最大）
 * - fail: 来自于JVM级别的检查异常，异常顶层类 Exception（范围中等）
 * - bug: 来自于程序级别的异常，异常顶层类 ProgramException（范围最小）
 */
package io.horizon.fn;