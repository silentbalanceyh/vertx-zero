package io.horizon.util;

import io.vertx.core.json.JsonObject;

/**
 * @author lang : 2023/4/28
 */
class _Net extends _Modeler {

    protected _Net() {
    }

    /**
     * 检查目标地址网络通讯是否正常
     *
     * @param host 主机
     * @param port 端口
     *
     * @return 是否正常
     */
    public static boolean netOk(final String host, final int port) {
        return HNet.isReach(host, port);
    }

    /**
     * 检查目标地址网络通讯是否正常
     *
     * @param host    主机
     * @param port    端口
     * @param timeout 超时时间
     *
     * @return 是否正常
     */
    public static boolean netOk(final String host, final int port, final int timeout) {
        return HNet.isReach(host, port, timeout);
    }

    /**
     * 读取本机网络IPV4地址
     *
     * @return IPV4地址
     */
    public static String netIPv4() {
        return HNet.getIPv4();
    }

    /**
     * 读取本机网络中的主机名
     *
     * @return 主机名
     */
    public static String netHostname() {
        return HNet.getHostName();
    }

    /**
     * 读取本机网络中的IPV6地址
     *
     * @return IPV6地址
     */
    public static String netIPv6() {
        return HNet.getIPv6();
    }

    /**
     * 读取网络IP地址，系统自动检查 V4 或  V6
     *
     * @return IP地址
     */
    public static String netIP() {
        return HNet.getIP();
    }

    /**
     * 网络状态信息检查
     *
     * @param line 网络状态信息
     *
     * @return JsonObject
     */
    public static JsonObject netStatus(final String line) {
        return HNet.netStatus(line);
    }

    /**
     * 计算网络标识
     *
     * @param url 网络地址
     *
     * @return 网络标识
     */
    public static String netUri(final String url) {
        return HNet.netUri(url);
    }
}
