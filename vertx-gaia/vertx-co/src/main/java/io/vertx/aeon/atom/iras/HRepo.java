package io.vertx.aeon.atom.iras;

import io.vertx.aeon.eon.em.TypeRepo;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class HRepo implements Serializable {
    /*
     * Github 远程地址 / 本地地址
     * 1. 本地寻址检查
     *    -- 先读取对应环境变量信息
     *    -- 路径信息读取
     *    -- 是否包含对应代码规范（.svn, .git目录）
     * 2. 最终获取本地运行的存储路径（开发代码区）
     * */
    private String uri;
    private String path;
    private TypeRepo type = TypeRepo.GIT_HUB;

    /*
     * 账号和口令
     * account
     * secret - 默认使用 AccessToken 为值
     * -- 如果配置了secret,则secure = true
     * -- 如果没有配置secret,则secure = false
     */
    private String account;
    private String secret;
    private boolean secure;

    public String getUri() {
        return this.uri;
    }

    public void setUri(final String uri) {
        this.uri = uri;
    }

    public String getPath() {
        // 内部逻辑
        final String path = System.getenv(this.path);
        return Ut.isNil(path) ? this.path : path;
    }

    public void setPath(final String path) {
        this.path = path;
    }

    public TypeRepo getType() {
        return this.type;
    }

    public void setType(final TypeRepo type) {
        this.type = type;
    }

    public String getAccount() {
        return this.account;
    }

    public void setAccount(final String account) {
        this.account = account;
    }

    public String getSecret() {
        return this.secret;
    }

    public void setSecret(final String secret) {
        this.secret = secret;
    }

    public boolean isSecure() {
        // 内部逻辑
        if (this.secure) {
            return true;
        }
        return Objects.nonNull(this.secret);
    }

    public void setSecure(final boolean secure) {
        this.secure = secure;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final HRepo hRepo = (HRepo) o;
        return this.uri.equals(hRepo.uri) && this.type == hRepo.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.uri, this.type);
    }
}
