package io.vertx.tp.plugin.git;

import io.vertx.aeon.atom.iras.HRepo;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class GitClientImpl implements GitClient {
    private transient final Vertx vertx;
    private transient HRepo repo;

    GitClientImpl(final Vertx vertx, final HRepo repo) {
        this.vertx = vertx;
        this.repo = repo;
    }

    @Override
    public GitClient init(final JsonObject params) {
        this.repo = Ut.deserialize(params, HRepo.class);
        return this;
    }
}
