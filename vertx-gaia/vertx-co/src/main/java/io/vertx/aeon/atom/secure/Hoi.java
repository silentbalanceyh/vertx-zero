package io.vertx.aeon.atom.secure;

import io.vertx.up.experiment.specification.request.KAppEnv;

import java.io.Serializable;

/**
 * 多租户维度专用对象规范（该对象规范直接从 XHeader 中提取）
 * - sigma：统一标识符
 * - language：语言信息
 * - appKey / appId / appName（内置包含KApp）
 * - tenant：租户ID
 * 拥有者ID，特殊维度（云环境专用，处理核心维度专用）
 * oi = Owner ID
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class Hoi implements Serializable {
    // 基础维度
    private String tenant;
    // 当前应用维度
    private KAppEnv env;
}
