package io.vertx.up.plugin.git;

import io.macrocosm.atom.boot.KRepo;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.up.error.git._400RepoCommandException;
import io.vertx.up.error.git._404RepoMissingException;
import io.vertx.up.error.git._409RepoExistingException;
import io.vertx.up.fn.Fn;
import io.vertx.up.plugin.git.refine.Gt;
import io.vertx.up.util.Ut;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class GitClientImpl implements GitClient {
    private transient final Vertx vertx;
    private transient KRepo repo;

    private transient CredentialsProvider provider;

    GitClientImpl(final Vertx vertx, final KRepo repo) {
        this.vertx = vertx;
        this.repo = repo;
        this.initialize(repo);
    }

    @Override
    public GitClient init(final JsonObject params) {
        this.repo = Ut.deserialize(params, KRepo.class);
        this.initialize(this.repo);
        return this;
    }

    private void initialize(final KRepo repo) {
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
        Gt.LOG.REPO.info(this.getClass(), "Initialize from {0} to {1}", uri, path);

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
        Gt.LOG.REPO.info(this.getClass(), "Open the repository of {0}", path);
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
            final MergeResult merged = result.getMergeResult();
            Gt.LOG.COMMAND.info(this.getClass(), "Pull result: success = {1}, Merge Status = {0}",
                merged.getMergeStatus(),
                result.isSuccessful());
            return result;
        } catch (final Exception ex) {
            throw new _400RepoCommandException(this.getClass(), "pull", ex.getMessage());
        }
    }

    @Override
    public Git search() {
        final FileRepositoryBuilder builder = new FileRepositoryBuilder();
        try {
            final Repository repository = builder.findGitDir(new File(this.repo.getPath())).build();
            Gt.LOG.REPO.info(this.getClass(), "Search the repository of {0}", this.repo.getPath());
            return new Git(repository);
        } catch (final Exception ex) {
            throw new _404RepoMissingException(this.getClass(), this.repo.getPath(), ex.getMessage());
        }
    }

    @Override
    public Status status(final Git git) {
        return Fn.failOr(() -> git.status().call());
    }

    @Override
    public RevCommit commit(final Git git, final String message) {
        try {
            final String comment;
            if (Ut.isNil(message)) {
                comment = "[ πηγή ] No Message";
            } else {
                comment = "[ πηγή ] " + message;
            }
            return git.commit().setMessage(comment).call();
        } catch (final Exception ex) {
            throw new _400RepoCommandException(this.getClass(), "commit", ex.getMessage());
        }
    }

    @Override
    public DirCache add(final Git git, final String pattern) {
        try {
            return git.add().addFilepattern(".").call();
        } catch (final Exception ex) {
            throw new _400RepoCommandException(this.getClass(), "add", ex.getMessage());
        }
    }

    @Override
    public Iterable<PushResult> push(final Git git, final boolean force) {
        try {
            final Repository repository = git.getRepository();
            final Set<String> remotes = repository.getRemoteNames();
            if (remotes.isEmpty()) {
                // Empty and Ignore
                Gt.LOG.COMMAND.info(this.getClass(), "Ignore remote push because of Non Remote related.");
                return new ArrayList<>();
            } else {
                final PushCommand command = git.push();
                if (Objects.nonNull(this.provider)) {
                    command.setCredentialsProvider(this.provider);
                }
                command.setForce(force);
                return command.call();
            }
        } catch (final Exception ex) {
            throw new _400RepoCommandException(this.getClass(), "push", ex.getMessage());
        }
    }
}
