package io.vertx.aeon.atom.iras;

import io.vertx.aeon.eon.HCache;
import io.vertx.aeon.eon.HName;
import io.vertx.aeon.eon.HPath;
import io.vertx.aeon.eon.em.ModeAeon;
import io.vertx.aeon.eon.em.RTEAeon;
import io.vertx.aeon.refine.HLog;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class HAeon implements Serializable {
    // 代码仓库
    private final ConcurrentMap<RTEAeon, HRepo> repos = new ConcurrentHashMap<>();
    // 三种模式核心支持
    private final ModeAeon mode;
    // 工作目录
    private final String workspace;
    // 启动配置
    private HBoot boot;

    /* 三种库 */
    private HAeon(final JsonObject configuration) {
        this.mode = Ut.toEnum(
            () -> Ut.valueString(configuration, KName.MODE),
            ModeAeon.class, ModeAeon.MIN
        );
        // 上层工作区
        final String ws = Ut.valueString(configuration, HName.WORKSPACE, HPath.WORKSPACE);
        this.workspace = ws;
        // 遍历读取 Repo, kinect, kidd, kzero
        final JsonObject repoJ = Ut.valueJObject(configuration, HName.REPO);
        Ut.<JsonObject>itJObject(repoJ, (itemJ, field) -> {
            final RTEAeon repoType = Ut.toEnum(() -> field, RTEAeon.class, null);
            if (Objects.nonNull(repoType)) {
                final HRepo repo = Ut.deserialize(itemJ, HRepo.class);
                // 绑定仓库工作区：workspace + runtime
                final String wsRepo = Ut.ioPath(ws, repoType.name());
                this.repos.put(repoType, repo.assemble(wsRepo));
            }
        });
    }

    public static HAeon configure(final JsonObject configJ) {
        // kidd 为出厂设置环境，所以以它为缓存键值
        final JsonObject repoJ = Ut.valueJObject(configJ, HName.REPO);
        final JsonObject kiddJ = Ut.valueJObject(repoJ, HName.KIDD);
        if (Ut.isNil(kiddJ)) {
            HLog.warnAeon(HAeon.class, "`kidd` configuration missing!!");
            return null;
        }
        // 初始化
        return HCache.CC_AEON.pick(() -> new HAeon(configJ), kiddJ.hashCode());
    }

    // ------------------------- 提取配置专用
    public HBoot inBoot() {
        return this.boot;
    }

    public ModeAeon inMode() {
        return this.mode;
    }

    public String inWS() {
        return this.workspace;
    }

    public HRepo inRepo(final RTEAeon runtime) {
        return this.repos.getOrDefault(runtime, null);
    }

    public ConcurrentMap<RTEAeon, HRepo> inRepo() {
        return this.repos;
    }

    // ------------------------- 软连接方法
    // 装配专用
    public void assemble(final HBoot boot) {
        this.boot = boot;
    }
}
