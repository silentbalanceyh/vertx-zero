package io.aeon.eon;

import io.vertx.up.eon.KName;

/**
 * 新的常量文件，区别于Zero中的KName
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface HName {
    /*
     * --------------------------------
     * aeon/zapp.yml
     *
     * aeon:
     *   name:
     *   workspace:
     *   repo:
     *     kidd:
     *     kinect:
     *     kzero:
     */
    String AEON = "aeon";               // aeon:                         永世核心框架
    String NAME = KName.NAME;           //   name:

    String WORKSPACE = "workspace";     //   workspace:                  工作区，默认 /var/tmp/zero-aeon
    String REPO = "repo";               //   repo:                       代码库
    String KINECT = "kinect";           //     kinect:                   低代码连接
    String KIDD = "kidd";               //     kidd:                     标准出厂环境
    String KZERO = "kzero";             //     kzero:                    远程云环境
    /*
     * aeon/zapp-axis.yml
     *
     * boot:
     *   component:
     *     on:
     *     off:
     *     run:
     *   alive:
     */
    String BOOT = "boot";                   // boot:                         启动连接器
    String COMPONENT = "component";         //   component:
    String ON = "on";                       //     on:
    String OFF = "off";                     //     off:
    String RUN = "run";                     //     run:
    String ALIVE = "alive";                 //   alive:                      源代码管理平台核心组件
    String ALIVE_NOVAE = "novae";           //     novae:
    String ALIVE_NOVA = "nova";             //     nova:
    String ALIVE_NEBULA = "nebula";         //     nebula:
}
