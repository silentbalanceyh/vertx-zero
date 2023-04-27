package io.vertx.tp.plugin.git;

import io.aeon.atom.iras.HRepo;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.horizon.uca.cache.Cc;

/**
 * GitInfix 不开放 Infix 结构，源于整个系统中不允许直接和某个 Git 仓库绑定，仓库的应用只局限于
 * 某些特定场景中
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class GitInfix {

    private static final Cc<Integer, GitClient> CC_CLIENT = Cc.open();

    public static GitClient createClient(final Vertx vertx, final HRepo repo) {
        // key = repo hashCode
        return CC_CLIENT.pick(() -> GitClient.createShared(vertx, repo), repo.hashCode());
    }

    public static GitClient createClient(final Vertx vertx, final JsonObject repoJ) {
        // key = repo hashCode
        return CC_CLIENT.pick(() -> GitClient.createShared(vertx, repoJ), repoJ.hashCode());
    }
}
