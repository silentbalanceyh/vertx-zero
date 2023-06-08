package io.horizon.eon.em;

/**
 * @author lang : 2023-05-31
 */
public final class EmUca {
    private EmUca() {
    }

    /**
     * @author lang : 2023-05-21
     */
    public enum Status {
        RESOLVED,   // 验证完成（系统级安装成功）
        INSTALLED,  // 已安装（配置验证成功）
        ACTIVATED,  // 已激活（连接到环境中成功）
        STARTED,    // 已启动（启动完成）

        STOPPED,    // 已停止（停止完成）

        ERROR,      // 错误（错误状态）

        UNINSTALLED // 已卸载（卸载完成）
    }
}
