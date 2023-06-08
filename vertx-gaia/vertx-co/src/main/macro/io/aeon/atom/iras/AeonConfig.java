package io.aeon.atom.iras;

import io.aeon.eon.HName;
import io.aeon.eon.HPath;
import io.aeon.runtime.CRunning;
import io.macrocosm.atom.boot.KPlot;
import io.macrocosm.atom.boot.KRepo;
import io.macrocosm.eon.em.EmCloud;
import io.macrocosm.specification.nc.HAeon;
import io.macrocosm.specification.nc.HWrapper;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.runtime.env.MatureOn;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static io.aeon.refine.Ho.LOG;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AeonConfig implements HAeon, Serializable {
    // 代码仓库
    private final ConcurrentMap<EmCloud.Runtime, KRepo> repos = new ConcurrentHashMap<>();
    // 三种模式核心支持
    private final EmCloud.Mode mode;
    // 工作目录
    private final String workspace;
    private final String name;

    private final KPlot plot;

    // 启动配置
    private HWrapper boot;

    /* 三种库 */
    private AeonConfig(final JsonObject configuration) {
        this.mode = Ut.toEnum(() -> Ut.valueString(configuration, KName.MODE),
            EmCloud.Mode.class, EmCloud.Mode.MIN);
        // 上层工作区
        this.name = Ut.valueString(configuration, HName.NAME);
        this.workspace = Ut.valueString(configuration, HName.WORKSPACE, HPath.WORKSPACE);
        // 云工作区 Plot
        JsonObject plotJ = Ut.valueJObject(configuration, KName.PLOT);
        plotJ = MatureOn.envPlot(plotJ);
        this.plot = Ut.deserialize(plotJ, KPlot.class);


        // 遍历读取 Repo, kinect, kidd, kzero
        this.initRepo(configuration);
    }

    public static HAeon configure(final JsonObject configJ) {
        // kidd 为出厂设置环境，所以以它为缓存键值
        final JsonObject repoJ = Ut.valueJObject(configJ, HName.REPO);
        final JsonObject kiddJ = Ut.valueJObject(repoJ, HName.KIDD);
        if (Ut.isNil(kiddJ)) {
            LOG.Aeon.warn(AeonConfig.class, "`kidd` configuration missing!!");
            return null;
        }
        // 初始化
        return CRunning.CC_AEON.pick(() -> new AeonConfig(configJ), kiddJ.hashCode());
    }

    private void initRepo(final JsonObject configuration) {
        final JsonObject repoJ = Ut.valueJObject(configuration, HName.REPO);
        Ut.<JsonObject>itJObject(repoJ, (itemJ, field) -> {
            final EmCloud.Runtime repoType = Ut.toEnum(() -> field, EmCloud.Runtime.class, null);
            if (Objects.nonNull(repoType)) {
                final KRepo repo = Ut.deserialize(itemJ, KRepo.class);
                // 绑定仓库工作区：workspace + runtime
                final String wsRepo = Ut.ioPath(this.workspace, repoType.name());
                this.repos.put(repoType, repo.assemble(wsRepo));
            }
        });
    }

    // ------------------------- 提取配置专用
    // 装配专用
    @Override
    public void boot(final HWrapper boot) {
        this.boot = boot;
    }

    @Override
    public HWrapper boot() {
        return this.boot;
    }

    @Override
    public KPlot plot() {
        return this.plot;
    }

    @Override
    public EmCloud.Mode mode() {
        return this.mode;
    }

    @Override
    public String workspace() {
        return this.workspace;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public KRepo repo(final EmCloud.Runtime runtime) {
        return this.repos.getOrDefault(runtime, null);
    }

    @Override
    public ConcurrentMap<EmCloud.Runtime, KRepo> repo() {
        return this.repos;
    }
}
