package io.macrocosm.eon.em;

/**
 * @author lang : 2023-05-31
 */
public final class EmCloud {
    private EmCloud() {
    }

    /**
     * @author <a href="http://www.origin-x.cn">Lang</a>
     */
    public enum Mode {
        MIN,        // 公共最小运行（公有化）
        MIRROR,     // 镜像独占模式（私有化）
        MIX,        // 混合活性（公有 + 私有）
    }

    /**
     * 「运行时」
     *
     * @author <a href="http://www.origin-x.cn">Lang</a>
     */
    public enum Runtime {
        kidd,       // 生产运行环境
        kinect,     // 低代码运行开发环境
        kzero;      // vert-zero-cloud远程标准核心库环境
    }

    /**
     * 「HRepo」库类型
     *
     * @author <a href="http://www.origin-x.cn">Lang</a>
     */
    public enum Repo {
        // GIT
        GIT_HUB,            // 「默认值」
        GIT_LIB,
        GIT_EE,
        GIT_PRIVATE,        // 「私库」
        // SVN
        SVN_PRIVATE,        // 「私库」
    }
}
