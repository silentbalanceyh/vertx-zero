package io.horizon.specification.secure;

/**
 * 「超级账号」
 * <hr/>
 * 工作空间，针对租户的核心接口，可以针对租户的核心维度，租户最核心的是工作空间和应用，分两种：
 * <pre><code>
 *   1. 开发空间
 *   2. 运行空间（又分云、平台、应用三级）
 * </code></pre>
 * 超级账号是云原生中显示生活面向的账号发放模式，每个租户都会保留一个超级账号，而整体账号的结构如：
 * <pre><code>
 *     - HOwner
 *       - HTenant      云端标准租户
 *         - HAccount
 *       - HPlatform    云端标准租户中的平台级配置以及账号
 *         - HAccount
 *       - HDomain      但应用专用账号
 *         - HAccount
 * </code></pre>
 * 其中 HAccount 是抽象账号接口，按此维度，整个账号系统会包含两部分
 * <pre><code>
 *     1. HOwner：超级账号（超级账号本身是业务实体，所以其内容中不包含账号和口令的原始信息）
 *     2. HAccount：子账号
 * </code></pre>
 *
 * @author lang : 2023-05-19
 */
public interface HOwner extends HCredential {
}
