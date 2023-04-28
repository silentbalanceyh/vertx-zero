package io.horizon.uca.net;

public interface IPFilter {
    String IPv6KeyWord = ":";

    boolean accept(String ipAddress);
}
