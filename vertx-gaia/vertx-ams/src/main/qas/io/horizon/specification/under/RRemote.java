package io.horizon.specification.under;

import io.horizon.annotations.reference.One2One;

/**
 * 「远程库」Remote Repository
 * 远程库中的库信息中会包含详细的版本信息，所以远程库可以发布，而且可以执行类似 HCabe 应用区域的部分
 *
 * @author lang : 2023-05-20
 */
public interface RRemote extends HRepo {
    /**
     * 返回当前工作分支
     *
     * @return {@link RBranch}
     */
    @One2One
    RBranch working();
}
