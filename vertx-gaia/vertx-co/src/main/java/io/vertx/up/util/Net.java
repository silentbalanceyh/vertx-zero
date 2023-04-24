package io.vertx.up.util;

import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.bridge.Values;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.net.IPHost;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Locale;

final class Net {
    private Net() {
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
        return Fn.orJvm(() -> {
            // 1.Check whether host is reachalbe
            final Boolean hostOk =
                Fn.orJvm(Boolean.FALSE, () -> InetAddress.getByName(host).isReachable(timeOut), host, timeOut);
            // 2.Check whether host/port could be connected.
            return hostOk ? (Fn.orJvm(Boolean.FALSE, () -> {
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
        return Fn.orJvm(() -> (InetAddress.getLocalHost()).getHostName(), true);
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
            final String method = splitted[Values.IDX].trim().toUpperCase(Locale.getDefault());
            final String uri = splitted[Values.ONE].trim();
            return new JsonObject().put("method", method).put("uri", uri);
        } else {
            return new JsonObject();
        }
    }
}
