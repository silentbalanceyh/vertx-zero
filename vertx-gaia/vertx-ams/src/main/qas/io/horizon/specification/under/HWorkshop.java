package io.horizon.specification.under;

/**
 * 「工作空间」Workshop
 * <hr/>
 * 工作空间隶属于研发中心，可标识研发中心的基础信息，有可能研发中心会存在树型的工作空间结构
 * 工作空间和低代码研发中心具有所属研发中心（HRAD）的关联关系
 *
 * @author lang : 2023-05-20
 */
public interface HWorkshop {
    /**
     * 返回所属研发中心
     *
     * @return {@link HRAD}
     */
    HRAD center();
}
