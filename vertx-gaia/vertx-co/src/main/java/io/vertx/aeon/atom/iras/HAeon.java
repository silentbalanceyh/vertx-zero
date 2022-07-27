package io.vertx.aeon.atom.iras;

import io.vertx.aeon.eon.HCache;
import io.vertx.aeon.eon.HName;
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
    // 启动配置
    private HBoot boot;

    /* 三种库 */
    private HAeon(final JsonObject configuration) {
        this.mode = Ut.toEnum(
            () -> Ut.valueString(configuration, KName.MODE),
            ModeAeon.class, ModeAeon.MIN
        );
        // 遍历读取 Repo, kinect, kidd, kzero
        final JsonObject repoJ = Ut.valueJObject(configuration, HName.REPO);
        Ut.<JsonObject>itJObject(repoJ, (itemJ, field) -> {
            final RTEAeon repoType = Ut.toEnum(() -> field, RTEAeon.class, null);
            if (Objects.nonNull(repoType)) {
                final HRepo repo = Ut.deserialize(itemJ, HRepo.class);
                this.repos.put(repoType, repo);
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
    public HBoot boot() {
        return this.boot;
    }

    public ModeAeon mode() {
        return this.mode;
    }

    // ------------------------- 软连接方法
    // 装配专用
    public void assemble(final HBoot boot) {
        this.boot = boot;
    }
}
