package io.vertx.aeon.eon.em;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public enum ModeAeon {
    MIN,        // 公共最小运行（公有化）
    MIRROR,     // 镜像独占模式（私有化）
    MIX,        // 混合活性（公有 + 私有）
}
