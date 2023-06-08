package io.vertx.aeon.uca.alive;

import io.macrocosm.specification.program.HNovae;
import io.vertx.core.Vertx;

/**
 * 「持续在线」
 *
 * - 可选择两种代码空间：SVN 和 GIT，目前只提供 Git 作为默认版本，处理低代码底层空间
 * - 行为
 * --- 1. 连接后创建新库（初始化）
 * --- 2. 配置连接K8（初始化）
 * --- 3. 创建数据库TiDB（初始化）
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractNovae implements HNovae {
    protected Vertx vertx;

    @Override
    @SuppressWarnings("unchecked")
    public HNovae bind(final Vertx vertx) {
        this.vertx = vertx;
        return this;
    }
}
