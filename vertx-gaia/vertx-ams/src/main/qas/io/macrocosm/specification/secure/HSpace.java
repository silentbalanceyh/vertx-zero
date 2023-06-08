package io.macrocosm.specification.secure;

import io.horizon.specification.app.HBelong;

/**
 * 「空间租户域」空间 Space EmApp
 * <hr/>
 * 该接口限定了某一个应用租户的应用信息，一个完整应用租户只会包含一个应用边界。
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface HSpace extends HBelong {
    /**
     * 当前应用租户所属于的应用租户域的标识，可以读取唯一的应用租户域，管理模式中所需
     *
     * @return 应用租户域标识
     */
    String galaxy();
}
