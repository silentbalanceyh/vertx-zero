package io.vertx.up.fn;

import io.horizon.fn.Actuator;
import io.horizon.fn.ExceptionActuator;
import io.vertx.up.util.Ut;

import java.util.Arrays;
import java.util.Objects;

/**
 * 作用函数
 * - io.horizon.fn.Actuator
 * - io.horizon.fn.ErrorActuator
 * - io.horizon.fn.ExceptionActuator
 * - io.horizon.fn.ZeroActuator
 *
 * @author lang : 2023/4/25
 */
final class FnActuator {

    private FnActuator() {
    }

    /**
     * 执行函数，不带异常的版本，执行条件
     * - 1. args 不传入
     * - 2. args 传入，但是全部不为空
     *
     * @param actuator 执行器
     * @param args     参数
     */
    static void execute(final Actuator actuator, final Object... args) {
        if (0 == args.length || Arrays.stream(args).allMatch(Objects::nonNull)) {
            actuator.execute();
        }
    }

    static void executeException(final ExceptionActuator actuator, final Object... args) {
        if (Ut.isNull(args)) {
            return;
        }
        try {
            actuator.execute();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
}
