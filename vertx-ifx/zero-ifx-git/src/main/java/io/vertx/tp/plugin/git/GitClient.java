package io.vertx.tp.plugin.git;

import io.vertx.aeon.atom.iras.HRepo;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.up.exception.WebException;
import io.vertx.up.exception.web._500InternalServerException;
import io.vertx.up.plugin.TpClient;
import io.vertx.up.util.Ut;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullResult;

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

    // -------------------- Git Command for History -------------------

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
