package io.vertx.aeon.atom.configuration;

import io.vertx.aeon.eon.HName;
import io.vertx.aeon.eon.em.ModeAeon;
import io.vertx.aeon.eon.em.RTEAeon;
import io.vertx.aeon.uca.HLog;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.uca.cache.Cc;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class HAeon implements Serializable {
    private static final Cc<Integer, HAeon> CC_AEON = Cc.open();
    private final ConcurrentMap<RTEAeon, HRepo> repos = new ConcurrentHashMap<>();
    /* 三种模式核心支持 */
    private ModeAeon mode;

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
            final HRepo repo = Ut.deserialize(itemJ, HRepo.class);
            if (Objects.nonNull(repoType)) {
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
        return CC_AEON.pick(() -> new HAeon(configJ), kiddJ.hashCode());
    }

    public ModeAeon getMode() {
        return this.mode;
    }

    public void setMode(final ModeAeon mode) {
        this.mode = mode;
    }

    public HRepo rteKidd() {
        return this.repos.getOrDefault(RTEAeon.kidd, null);
    }

    public HRepo rteKinect() {
        return this.repos.getOrDefault(RTEAeon.kinect, null);
    }

    public HRepo rteKzero() {
        return this.repos.getOrDefault(RTEAeon.kzero, null);
    }
}
