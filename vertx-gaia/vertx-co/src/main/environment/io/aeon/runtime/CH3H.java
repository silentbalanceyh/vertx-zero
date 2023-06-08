package io.aeon.runtime;

import io.aeon.atom.secure.KPermit;
import io.aeon.atom.secure.KSemi;
import io.horizon.annotations.Memory;
import io.horizon.specification.typed.TCombiner;
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
     * - CC_PERMIT  :  KPermit      权限定义对象
     * - CC_SEMI    :  KSemi        权限执行双维对象（维度+数据）
     */
    @Memory(KPermit.class)
    Cc<String, KPermit> CC_PERMIT = Cc.open();
    @Memory(KSemi.class)
    Cc<String, KSemi> CC_SEMI = Cc.open();

    /*
     * 「界面级别处理」
     */
    @SuppressWarnings("all")
    @Memory(TCombiner.class)
    Cc<String, TCombiner> CC_COMBINER = Cc.openThread();
}
