package io.vertx.up.util.net;

public class IPv6Filter implements IPFilter {
    private static IPFilter instance = null;

    /**
     * Access Control
     */
    private IPv6Filter() {
    }

    /**
     * Ignore multiple thread sync problem in extreme case
     *
     * @return Get IPv6Filter of instance for singleton
     */
    public static IPFilter getInstance() {
        if (instance == null) {
            instance = new IPv6Filter();
        }
        return instance;
    }

    @Override
    public boolean accept(final String ipAddress) {
        return ipAddress != null && ipAddress.contains(IPv6KeyWord);
    }
}
