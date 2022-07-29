package io.vertx.tp.plugin.git;

import io.vertx.aeon.atom.iras.HRepo;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.error._400RepoPullException;
import io.vertx.tp.error._404RepoMissingException;
import io.vertx.tp.error._409RepoExistingException;
import io.vertx.up.util.Ut;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
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
        this.initialize(repo);
    }

    @Override
    public GitClient init(final JsonObject params) {
        this.repo = Ut.deserialize(params, HRepo.class);
        this.initialize(this.repo);
        return this;
    }

    private void initialize(final HRepo repo) {
        // 安全设置
        if (repo.inSecure()) {
            this.provider = new UsernamePasswordCredentialsProvider(repo.getAccount(), repo.getSecret());
        }
        // Uri反向检索
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
    public Git connect(final boolean pull) {
        final File file = new File(this.repo.inWS());
        if (file.exists()) {
            final Git git = this.open();
            if (pull) {
                // pull repository
                this.pull(git);
            }
            return git;
        } else {
            return this.clon();
        }
    }

    @Override
    public PullResult pull(final Git git) {
        try {
            final PullCommand command = git.pull();
            if (Objects.nonNull(this.provider)) {
                command.setCredentialsProvider(this.provider);
            }
            final PullResult result = command.call();
            GLog.infoCommand(this.getClass(), "Pull result: success = {1}, info = \n{0}",
                result.toString(),
                result.isSuccessful());
            return result;
        } catch (final Exception ex) {
            throw new _400RepoPullException(this.getClass(), this.repo.getUri(), ex.getMessage());
        }
    }

    @Override
    public Git search() {
        final FileRepositoryBuilder builder = new FileRepositoryBuilder();
        try {
            final Repository repository = builder.findGitDir(new File(this.repo.getPath())).build();
            GLog.infoRepo(this.getClass(), "Search the repository of {0}", this.repo.getPath());
            return new Git(repository);
        } catch (final Exception ex) {
            throw new _404RepoMissingException(this.getClass(), this.repo.getPath(), ex.getMessage());
        }
    }
}
