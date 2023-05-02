package io.horizon.runtime.cache;

import io.horizon.annotations.Memory;
import io.horizon.atom.app.KApp;
import io.horizon.uca.cache.Cc;
import io.macrocosm.atom.HOI;

/**
 * @author lang : 2023/5/2
 */
interface CApp {
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
    @Memory(KApp.class)
    Cc<String, KApp> CC_APP = Cc.open();
    @Memory(HOI.class)
    Cc<String, HOI> CC_OI = Cc.open();
}
