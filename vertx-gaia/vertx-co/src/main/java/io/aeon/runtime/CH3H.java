package io.aeon.runtime;

import io.aeon.atom.secure.HPermit;
import io.aeon.atom.secure.HSemi;
import io.horizon.annotations.Memory;
import io.horizon.specification.action.HCombiner;
import io.horizon.uca.cache.Cc;

/**
 * 「运行时系统级数据缓存」
 * 氚(chuān) - 3H（稀有能源元素）
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
interface CH3H extends CH2H {

    /*
     * 「环境级别处理」安全管理专用
     * - CC_PERMIT  :  HPermit      权限定义对象
     * - CC_SEMI    :  HSemi        权限执行双维对象（维度+数据）
     */
    @Memory(HPermit.class)
    Cc<String, HPermit> CC_PERMIT = Cc.open();
    @Memory(HSemi.class)
    Cc<String, HSemi> CC_SEMI = Cc.open();

    /*
     * 「界面级别处理」
     */
    @SuppressWarnings("all")
    @Memory(HCombiner.class)
    Cc<String, HCombiner> CC_COMBINER = Cc.openThread();
}
