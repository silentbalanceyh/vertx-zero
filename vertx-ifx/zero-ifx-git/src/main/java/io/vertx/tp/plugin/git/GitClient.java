package io.vertx.tp.plugin.git;

import io.aeon.atom.iras.HRepo;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.bridge.Strings;
import io.vertx.up.exception.WebException;
import io.vertx.up.exception.web._500InternalServerException;
import io.vertx.up.plugin.TpClient;
import io.vertx.up.util.Ut;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.PushResult;

import java.util.function.Supplier;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface GitClient extends TpClient<GitClient> {

    static GitClient createShared(final Vertx vertx, final JsonObject repoJ) {
        final HRepo repo = Ut.deserialize(repoJ, HRepo.class);
        return new GitClientImpl(vertx, repo);
    }

    static GitClient createShared(final Vertx vertx, final HRepo repo) {
        return new GitClientImpl(vertx, repo);
    }

    // -------------------- Git Command for Repo-------------------
    /*
     * clone part
     * 1. cloned: To avoid clone() method in java Object, here renamed to `clon` instead.
     *    = git clone {repoUrl}
     * 2. connected: Open existing repository directly ( verified if it's valid )
     */
    Git open();

    default Future<Git> openAsync() {
        return this.exec(this::open);
    }

    Git clon();

    default Future<Git> clonAsync() {
        return this.exec(this::clon);
    }

    default Git connect() {
        return this.connect(false);
    }

    Git connect(boolean pull);

    default Future<Git> connectAsync() {
        return this.exec(this::connect); // Future.succeededFuture(this.connect());
    }

    default Future<Git> connectAsync(final boolean pull) {
        return this.exec(() -> this.connect(pull)); // Future.succeededFuture(this.connect());
    }

    Git search();

    default Future<Git> searchAsync() {
        return this.exec(this::search);
    }

    // -------------------- Git Command for Action  -------------------
    PullResult pull(Git git);

    default Future<PullResult> pullAsync(final Git git) {
        return this.exec(() -> this.pull(git));
    }

    default DirCache add(final Git git) {
        return this.add(git, Strings.DOT);
    }

    DirCache add(Git git, String pattern);

    default Future<DirCache> addAsync(final Git git) {
        return this.exec(() -> this.add(git));
    }

    default Future<DirCache> addAsync(final Git git, final String pattern) {
        return this.exec(() -> this.add(git, pattern));
    }

    RevCommit commit(Git git, String message);

    default Future<RevCommit> commitAsync(final Git git, final String message) {
        return this.exec(() -> this.commit(git, message));
    }

    Iterable<PushResult> push(Git git, boolean force);

    default Future<Iterable<PushResult>> pushAsync(final Git git, final boolean force) {
        return this.exec(() -> this.push(git, force));
    }

    default Iterable<PushResult> push(final Git git) {
        return this.push(git, false);
    }

    default Future<Iterable<PushResult>> pushAsync(final Git git) {
        return this.exec(() -> this.push(git));
    }


    // -------------------- Git Command for Status/History -------------------
    Status status(final Git git);

    // -------------------- Private Method -------------------
    private <T> Future<T> exec(final Supplier<T> supplier) {
        try {
            return Future.succeededFuture(supplier.get());
        } catch (final WebException ex) {
            return Future.failedFuture(ex);
        } catch (final Throwable ex) {
            return Future.failedFuture(new _500InternalServerException(this.getClass(), ex.getMessage()));
        }
    }
}
