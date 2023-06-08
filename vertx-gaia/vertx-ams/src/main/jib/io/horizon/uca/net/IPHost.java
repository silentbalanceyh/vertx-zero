package io.horizon.uca.net;

import io.horizon.eon.VString;
import io.horizon.fn.HFn;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 *
 */
public class IPHost {
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
        return HFn.failOr(VString.EMPTY, () -> {
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
            return VString.EMPTY;
        }, ipFilter);
    }
}
