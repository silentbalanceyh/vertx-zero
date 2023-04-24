package io.aeon.eon.em;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public enum ScIn {
    NONE,       // 无数据源
    WEB,        // 静态专用
    DAO,        // 动态：静态接口
    ATOM,       // 动态：动态接口
    DEFINE,     // 自定义，组件使用模式
}
