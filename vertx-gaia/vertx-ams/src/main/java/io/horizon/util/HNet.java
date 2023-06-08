package io.horizon.util;

import io.horizon.eon.VValue;
import io.horizon.fn.HFn;
import io.horizon.uca.net.IPHost;
import io.vertx.core.json.JsonObject;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Locale;

final class HNet {
    private HNet() {
    }

    /**
     * Check whether host:port is ok to connect
     *
     * @param host checked host
     * @param port checked port
     *
     * @return whether it's reached
     */
    static boolean isReach(final String host, final int port) {
        return isReach(host, port, 3000);
    }

    static boolean isReach(final String host, final int port, final Integer timeOut) {
        return HFn.failOr(() -> {
            // 1.Check whether host is reachalbe
            final Boolean hostOk =
                HFn.failOr(Boolean.FALSE, () -> InetAddress.getByName(host).isReachable(timeOut), host, timeOut);
            // 2.Check whether host/port could be connected.
            return hostOk ? (HFn.failOr(Boolean.FALSE, () -> {
                final Socket socket = new Socket();
                socket.connect(new InetSocketAddress(host, port));
                final boolean reached = socket.isConnected();
                socket.close();
                return reached;
            })) : hostOk;
        }, host, port);
    }

    /**
     * @return ip address of ipv4 format
     */
    static String getIPv4() {
        return IPHost.getInstance().getExtranetIPv4Address();
    }

    static String getHostName() {
        return HFn.failOr(() -> (InetAddress.getLocalHost()).getHostName(), true);
    }

    /**
     * @return ip address of ipv6 format
     */
    static String getIPv6() {
        return IPHost.getInstance().getExtranetIPv6Address();
    }

    /**
     * @return ip address of common format ( detect by system about 4 or 6 )
     */
    static String getIP() {
        return IPHost.getInstance().getExtranetIPAddress();
    }

    static String netUri(final String url) {
        if (null == url) {
            return null;
        }
        if (url.contains("?")) {
            return url.split("\\?")[0];
        } else {
            return url;
        }
    }

    static JsonObject netStatus(final String line) {
        if (null == line || !line.contains(" ")) {
            return new JsonObject();
        }
        final String[] splitted = line.split(" ");
        if (2 == splitted.length) {
            final String method = splitted[VValue.IDX].trim().toUpperCase(Locale.getDefault());
            final String uri = splitted[VValue.ONE].trim();
            return new JsonObject().put("method", method).put("uri", uri);
        } else {
            return new JsonObject();
        }
    }
}
