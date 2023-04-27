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
 */
package io.horizon.fn;