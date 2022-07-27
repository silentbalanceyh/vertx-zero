package io.vertx.tp.plugin.git;

import io.vertx.aeon.atom.iras.HRepo;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.up.plugin.TpClient;
import io.vertx.up.util.Ut;

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

    // -------------------- Git Command -------------------
    /*
     * git clone {repoUrl}
     */
}
