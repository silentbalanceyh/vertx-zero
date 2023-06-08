package io.horizon.specification.secure;

import io.horizon.eon.em.app.SecurityLevel;

/**
 * 「安全凭证」
 * <hr/>
 * 安全凭证信息中包含了系统级别登录登录环境所需的完整接口。
 * <pre><code>
 *     1. 账号名称信息
 *     2. 账号密码、口令信息
 *     3. 账号基础等级：应用 -> 管理 -> 开发
 *     4. 账号私钥信息（特殊登录场景所需）
 *     5. 系统分发的专用 Secret
 * </code></pre>
 *
 * @author lang : 2023-05-20
 */
public interface HCredential {
    /**
     * 账号登录名称
     *
     * @return {@link String}
     */
    String username();

    /**
     * 账号对应口令，口令分几种：
     * <pre><code>
     *     1. 普通账号密码模式，口令就是加密之后的字符串
     *     2. 专用账号密码模式，口令是加密之后的字符串，但是需要使用私钥进行解密
     * </code></pre>
     *
     * @return {@link String}
     */
    String secret();

    /**
     * 账号级别，用于描述账号本身的等级关系，后期会使用此等级来进行账号的权限控制
     * 除开级别描述以外，也可以代表账号的类型，所以此处使用 kind 关键字
     *
     * @return {@link SecurityLevel}
     */
    SecurityLevel kind();
}
