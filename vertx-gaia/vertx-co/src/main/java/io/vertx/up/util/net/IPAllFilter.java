package io.vertx.up.util.net;

public class IPAllFilter implements IPFilter {
    private static IPFilter instance = null;

    /**
     * Access Control
     */
    private IPAllFilter() {
    }

    /**
     * Ignore multiple thread sync problem in extreme case
     *
     * @return Get instance of IPAllFilter here to implement singleton design pattern
     */
    public static IPFilter getInstance() {
        if (instance == null) {
            instance = new IPAllFilter();
        }
        return instance;
    }

    /**
     * @param ipAddress The ip address literal string
     *
     * @return boolean check whether the ip address format is ok
     */
    @Override
    public boolean accept(final String ipAddress) {
        return true;
    }
}
