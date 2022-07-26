package io.vertx.aeon.specification.program;

import io.vertx.aeon.atom.iras.HRepo;
import io.vertx.aeon.eon.em.TypeRepo;
import io.vertx.aeon.specification.element.HEvent;

/**
 * 「持续在线」
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface HAlive extends HEvent<HRepo, Boolean> {
    /*
     * 创建专用空间，其执行流程如
     * 1. In：检查提供数据本身是否合法
     * -- Git空间检查
     * -- 安全检查（模式AccessToken）
     * -- 新库名称是否合法
     * 2. Command：初始化
     * -- 建库
     * -- 初始化Zero Cloud规范空间
     * -- 将运行环境推到 K8
     */
    TypeRepo support();
}
