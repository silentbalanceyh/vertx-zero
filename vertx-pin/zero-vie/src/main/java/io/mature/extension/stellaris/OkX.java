package io.mature.extension.stellaris;

import io.macrocosm.specification.program.HArk;
import io.vertx.up.commune.config.Database;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface OkX {
    // ---------------- 基础环境 ---------------------

    /**
     * 数据库配置对象读取方法。
     *
     * @return {@link Database}
     */
    Database configDatabase();

    /**
     * 应用配置对象读取方法。
     *
     * @return {@link io.macrocosm.specification.program.HArk}
     */
    HArk configApp();
}
