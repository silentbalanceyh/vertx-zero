package io.vertx.tp.plugin.git;

import io.vertx.aeon.atom.iras.HRepo;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.error._404RepoMissingException;
import io.vertx.tp.error._409RepoExistingException;
import io.vertx.up.util.Ut;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class GitClientImpl implements GitClient {
    private transient final Vertx vertx;
    private transient HRepo repo;

    private transient CredentialsProvider provider;

    GitClientImpl(final Vertx vertx, final HRepo repo) {
        this.vertx = vertx;
        this.repo = repo;
        if (repo.inSecure()) {
            this.provider = new UsernamePasswordCredentialsProvider(repo.getAccount(), repo.getSecret());
        }
    }

    @Override
    public GitClient init(final JsonObject params) {
        this.repo = Ut.deserialize(params, HRepo.class);
        return this;
    }

    @Override
    public Git clon() {
        final String uri = this.repo.getUri();
        final String path = this.repo.inWS();
        GLog.infoRepo(this.getClass(), "Initialize from {0} to {1}", uri, path);

        final CloneCommand command = Git.cloneRepository()
            .setURI(uri)
            .setDirectory(new File(path))
            .setCloneAllBranches(true);
        if (Objects.nonNull(this.provider)) {
            command.setCredentialsProvider(this.provider);
        }
        try {
            return command.call();
        } catch (final Exception ex) {
            throw new _409RepoExistingException(this.getClass(), path, ex.getMessage());
        }
    }

    @Override
    public Git open() {
        final String path = this.repo.inWS();
        GLog.infoRepo(this.getClass(), "Open the repository of {0}", path);
        try {
            return Git.open(new File(path));
        } catch (final Exception ex) {
            throw new _404RepoMissingException(this.getClass(), path, ex.getMessage());
        }
    }

    @Override
    public Git connect() {
        final File file = new File(this.repo.inWS());
        if (file.exists()) {
            return this.open();
        } else {
            return this.clon();
        }
    }
}
