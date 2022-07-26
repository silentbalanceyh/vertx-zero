package io.vertx.aeon.eon;

/**
 * 新的常量文件，区别于Zero中的KName
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface HName {
    String AEON = "aeon";      // 「aeon」多处使用（永世名称）

    // ------------- 配置部分 --------------
    String REPO = "repo";      // repo: -> kinect, kidd, kzero
    String KINECT = "kinect";  // 「kinect」- 低代码连接
    String KIDD = "kidd";      // 「kidd」  - 标准出厂环境
    String KZERO = "kzero";    // 「kzero」 - 远程云环境
}
