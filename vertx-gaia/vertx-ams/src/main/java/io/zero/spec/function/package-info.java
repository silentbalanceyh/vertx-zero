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
 * --- ZeroActuator             / 抛ZeroException
 * - EBiConsumer
 * --- ZeroBiConsumer           / 抛ZeroException
 * - EConsumer
 * --- ErrorConsumer            / 抛Throwable
 * --- ExceptionConsumer        / 抛Exception
 * - EFunction
 * --- ErrorFunction            / 抛Throwable
 * --- ExceptionFunction        / 抛Exception
 * - ESupplier
 * --- ErrorSupplier            / 抛Throwable
 * --- ExceptionSupplier        / 抛Exception
 * --- ZeroSupplier             / 抛ZeroException
 * 若想要补齐则参考基础规范，现阶段框架级并没有完成所有函数规范的补齐动作，从实际使用上也没有必要执行补齐的动作
 */
package io.zero.spec.function;