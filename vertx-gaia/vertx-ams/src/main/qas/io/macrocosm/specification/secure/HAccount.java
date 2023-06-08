package io.macrocosm.specification.secure;

import io.horizon.specification.secure.HCredential;

import java.util.Set;

/**
 * 「子账号」
 * <hr/>
 * 包含了需要使用的所有账号信息，此账号用于真正登录系统后台，HAccount 直接支持树型结构。
 * <pre><code>
 *     1. 一个 {@link HAccount} 账号之下可以包含多个子账号
 *     2. 只有主账号会包含契约，子账号契约和租约都位于账号之下
 * </code></pre>
 * 账号和上层容器由上层容器来引用，如此设计会使 {@link HAccount} 形成账号单独的个体。
 * 由于是树型结构，所以子账号的自身结构分成两类
 * <pre><code>
 *     1. 强引用：父账号
 *     2. 弱引用：子账号集
 * </code></pre>
 *
 * @author lang : 2023-05-20
 */
public interface HAccount extends HCredential {
    /**
     * 返回当前账号的父账号信息，如果当前账号是主账号，则返回 null
     *
     * @return 父账号 {@link HAccount}
     */
    default HAccount parent() {
        return null;
    }

    /**
     * 返回当前账号的所有子账号信息，默认为空集合
     *
     * @return 子账号集合 {@link Set}
     */
    default Set<String> children() {
        return Set.of();
    }
}
