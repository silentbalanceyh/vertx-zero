package io.vertx.aeon.runtime;

import io.vertx.aeon.atom.iras.HAeon;
import io.vertx.aeon.atom.iras.HBoot;
import io.vertx.aeon.specification.action.HEvent;
import io.vertx.aeon.specification.app.HFS;
import io.vertx.up.uca.cache.Cc;

/**
 * 「运行时组件缓存」
 * 氕(piē) - 1H（普通能源元素）
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface H1H {
    /*
     * CC_AEON:  Aeon系统启动后的核心配置缓存
     * CC_BOOT:  Aeon系统启动过后的所有使用类清单（组件接口集）
     */
    Cc<Integer, HAeon> CC_AEON = Cc.open();
    Cc<Integer, HBoot> CC_BOOT = Cc.open();


    /*
     * 「线程级」
     * CCT_EVENT: Aeon中的所有Event集合
     * CCT_FS:    Aeon中的存储IO（文件系统、远程文件系统）组件访问集
     */
    @SuppressWarnings("all")
    Cc<String, HEvent> CCT_EVENT = Cc.openThread();
    Cc<String, HFS> CCT_FS = Cc.openThread();


    /*
     * 「Zero标准」
     * CC_SPI:    内置调用HService形成的通道接口（ServiceLoader规范）
     *            HService优先级
     *            - /META-INF/services/aeon/        Aeon Enabled
     *            - /META-INF/services/             Zero Extension Enabled
     */
    Cc<Class<?>, Object> CC_SPI = Cc.open();
}
