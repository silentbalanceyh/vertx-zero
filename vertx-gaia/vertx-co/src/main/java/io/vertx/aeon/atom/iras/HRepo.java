package io.vertx.aeon.atom.iras;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.vertx.aeon.eon.HEnv;
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

    /*
     * workspace 和 path 的区别
     * 1. workspace 为当前库的直接对接目录，用于 clone, open 专用（为工作区间）
     * 2. path 为当前库应该转移配置的目标目录，一般为另外一个库，不一定是当前库的镜像
     */
    @JsonIgnore
    private String workspace;

    public String getUri() {
        return this.uri;
    }

    public void setUri(final String uri) {
        this.uri = uri;
    }

    public String getPath() {
        // 内部逻辑
        /*
         * 1. 优先从配置的 path 提取工作目录：将 this.path 作为环境变量
         * 2. 如果 path 不存在则使用标准环境变量：ZK_APP
         * 3. 如果环境变量无法提取，则直接使用 path 作为目录
         */
        final String pathKey = Ut.isNil(this.path) ? HEnv.ZK_APP : this.path;
        return Ut.valueEnv(pathKey, this.path);
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
        return Ut.valueEnv(this.secret);
    }

    public void setSecret(final String secret) {
        this.secret = secret;
    }

    // ------------------------- 提取配置专用
    public String inWS() {
        return this.workspace;
    }

    public boolean inSecure() {
        return Objects.nonNull(this.secret);
    }

    // ------------------------- 软连接方法
    public HRepo assemble(final String workspace) {
        this.workspace = workspace;
        return this;
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
