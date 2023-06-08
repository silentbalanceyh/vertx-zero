package io.horizon.uca.net;

public class IPFilterFactory {
    public static IPFilter getIPAllAcceptFilter() {
        return IPAllFilter.getInstance();
    }

    public static IPFilter getIPv4AcceptFilter() {
        return IPv4Filter.getInstance();
    }

    public static IPFilter getIPv6AcceptFilter() {
        return IPv6Filter.getInstance();
    }
}
