package io.vertx.aeon.atom.config;

import java.io.Serializable;

/**
 * plot: (小块)土地
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class HPlot implements Serializable {

    private String cloud;                   // AEON_CLOUD
    private String child;                   // AEON_APP
    private String name;                    // Z_APP
    private String namespace;               // Z_NS
    private String language;                // Z_LANG
    private String sigma;                   // Z_SIGMA

    public String getCloud() {
        return this.cloud;
    }

    public void setCloud(final String cloud) {
        this.cloud = cloud;
    }

    public String getChild() {
        return this.child;
    }

    public void setChild(final String child) {
        this.child = child;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getNamespace() {
        return this.namespace;
    }

    public void setNamespace(final String namespace) {
        this.namespace = namespace;
    }

    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(final String language) {
        this.language = language;
    }

    public String getSigma() {
        return this.sigma;
    }

    public void setSigma(final String sigma) {
        this.sigma = sigma;
    }
}
