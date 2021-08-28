package io.vertx.up.uca.cosmic;

import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/*
 * Empty X509 of default implementation,
 * It could be used in `https` connection of default implementation
 */
public class TrustX509 implements X509TrustManager {
    @Override
    public void checkClientTrusted(final X509Certificate[] chain, final String authType)
        throws CertificateException {
    }

    @Override
    public void checkServerTrusted(final X509Certificate[] chain, final String authType)
        throws CertificateException {
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[]{};
    }
}
