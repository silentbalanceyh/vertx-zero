package io.vertx.up.eon.em;

/**
 * @author lang : 2023-05-30
 */
public enum FeatureMark {
    SHARED,     // 是否开启 SharedMap
    SESSION,    // 是否开启 Session
    ETCD,       // 是否配置 ETCD

    INIT,       // 是否开启了 Zero Extension 中的模块初始化
}
