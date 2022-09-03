package io.vertx.aeon.runtime;

import io.vertx.aeon.atom.secure.HPermit;
import io.vertx.aeon.atom.secure.HSemi;
import io.vertx.aeon.atom.secure.Hoi;
import io.vertx.aeon.specification.action.HCombiner;
import io.vertx.up.experiment.specification.power.KApp;
import io.vertx.up.uca.cache.Cc;

/**
 * 「运行时系统级数据缓存」
 * 氚(chuān) - 3H（稀有能源元素）
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface H3H {
    /*
     * 「环境级别处理」
     * - CC_APP : 建模专用
     * - CC_OI  : 云环境专用
     *
     * 此处 CC_APP 中为多键单引用处理缓存（针对每一个App）
     * - sigma, appId, appKey, code,
     *
     * 新对象 Hoi 作为云环境的核心兼容型接口，CC_OI 中存储了当前系统中运行的所有租户结构数据，
     * 在启用了 Aeon 之后 Hoi 作为了拥有者的核心数据结构，对应不同的环境级别
     */
    Cc<String, KApp> CC_APP = Cc.open();
    Cc<String, Hoi> CC_OI = Cc.open();

    /*
     * 「环境级别处理」安全管理专用
     * - CC_PERMIT  :  HPermit      权限定义对象
     * - CC_SEMI    :  HSemi        权限执行双维对象（维度+数据）
     */
    Cc<String, HPermit> CC_PERMIT = Cc.open();
    Cc<String, HSemi> CC_SEMI = Cc.open();

    /*
     * 「界面级别处理」
     */
    @SuppressWarnings("all")
    Cc<String, HCombiner> CC_COMBINER = Cc.openThread();
}
