package io.vertx.tp.plugin.git.secure;

import io.vertx.aeon.atom.iras.HRepo;
import io.vertx.up.uca.cache.Cc;
import io.vertx.up.util.Ut;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.Serializable;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class GitAccount implements Serializable {
    private final static Cc<String, CredentialsProvider> CC_PROVIDER = Cc.open();
    // 账号和口令
    private String username;
    private String password;

    private boolean initialized;

    public GitAccount(final String username, final String password) {
        this.username = username;
        this.password = password;
        this.initialized = true;
    }

    public GitAccount(final HRepo repo) {
        if (repo.isSecure()) {
            this.username = repo.getAccount();
            this.password = repo.getSecret();
            this.initialized = true;
        }
    }

    public boolean valid() {
        return this.initialized;
    }

    public CredentialsProvider provider() {
        if (this.initialized && Ut.notNil(this.username) && Ut.notNil(this.password)) {
            return CC_PROVIDER.pick(() -> new UsernamePasswordCredentialsProvider(this.username, this.password), this.username);
        } else {
            return null;
        }
    }
}
