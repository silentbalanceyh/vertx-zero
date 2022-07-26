package io.vertx.aeon.specification.element;

/**
 * 「指令」指令抽象到底层某个单独行为中（底层封装）
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface HCommand<I, R> {

    // 合法性检查（检查输入）
    R configure(I input);
}
