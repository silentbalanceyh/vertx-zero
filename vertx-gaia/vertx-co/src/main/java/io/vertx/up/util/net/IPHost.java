package io.vertx.up.util.net;

import io.vertx.up.log.Annal;
import io.vertx.up.eon.Strings;
import io.vertx.up.fn.Fn;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 *
 */
public class IPHost {

    private final static Annal LOGGER = Annal.get(IPHost.class);
    /**
     * Singleton instance
     */
    private static final IPHost instance = new IPHost();

    /**
     * Access Control
     */
    private IPHost() {
    }

    /**
     * @return instance
     */
    public static IPHost getInstance() {
        return instance;
    }

    public String getExtranetIPv4Address() {
        return this.searchNetworkInterfaces(IPFilterFactory.getIPv4AcceptFilter());
    }


    public String getExtranetIPv6Address() {
        return this.searchNetworkInterfaces(IPFilterFactory.getIPv6AcceptFilter());
    }


    public String getExtranetIPAddress() {
        return this.searchNetworkInterfaces(IPFilterFactory.getIPAllAcceptFilter());
    }

    private String searchNetworkInterfaces(final IPFilter ipFilter) {
        return Fn.getJvm(Strings.EMPTY, () -> {
            final Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
            while (enumeration.hasMoreElements()) {
                final NetworkInterface networkInterface = enumeration.nextElement();
                //Ignore Loop/virtual/Non-started network interface
                if (networkInterface.isLoopback() || networkInterface.isVirtual() || !networkInterface.isUp()) {
                    continue;
                }
                final Enumeration<InetAddress> addressEnumeration = networkInterface.getInetAddresses();
                while (addressEnumeration.hasMoreElements()) {
                    final InetAddress inetAddress = addressEnumeration.nextElement();
                    final String address = inetAddress.getHostAddress();
                    if (ipFilter.accept(address)) {
                        return address;
                    }
                }
            }
            return Strings.EMPTY;
        }, ipFilter);
    }
}
