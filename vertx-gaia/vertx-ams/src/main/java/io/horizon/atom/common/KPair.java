package io.horizon.atom.common;

import io.horizon.util.HUt;

import java.io.Serializable;
import java.util.Objects;

/**
 * 公私密钥对
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class KPair implements Serializable {
    private String publicKey;
    private String privateKey;

    public KPair(final String publicKey, final String privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return this.publicKey;
    }

    public void setPublicKey(final String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return this.privateKey;
    }

    public void setPrivateKey(final String privateKey) {
        this.privateKey = privateKey;
    }

    public boolean valid() {
        return HUt.isNotNil(this.privateKey) && HUt.isNotNil(this.publicKey);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final KPair kPair = (KPair) o;
        return this.publicKey.equals(kPair.publicKey) && this.privateKey.equals(kPair.privateKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.publicKey, this.privateKey);
    }

    @Override
    public String toString() {
        return "publicKey =\n" + this.publicKey + '\n' +
            "privateKey =\n" + this.privateKey;
    }
}
