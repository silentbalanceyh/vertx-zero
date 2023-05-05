package cn.vertxup.ambient.service.application;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

public interface AppStub {

    /**
     * 非安全模式下的应用程序登录，登录过程访问 X_APP 表结构，该API在登录界面中使用
     * 并且不返回 appKey 的敏感数据信息。参数中的 name 信息来自于前端环境变量中的配
     * 置：.env.development 文件中的的环境变量 `Z_APP`，请确认该数据已存在于 X_APP
     * 表中，列名为 `NAME`。
     * 此处返回数据格式如：
     * <pre><code class="json">
     * {
     *      "key": "78fce5a2-17f3-4dac-a75c-7e751595015c",
     *      "name": "vie.app.zui",
     *      "code": "zui",
     *      "title": "Zero框架脚手架",
     *      "domain": "localhost",
     *      "appPort": 5100,
     *      "urlEntry": "/login/index",
     *      "urlMain": "/main/index",
     *      "path": "/zui",
     *      "route": "/zui",
     *      "active": true,
     *      "sigma": "ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq",
     *      "language": "cn"
     * }
     * </code></pre>
     * 注：appKey 已经在执行过程中被拿掉了，所以此处不返回，appKey 只有在登录之后会生效。
     * 回填部分：
     *
     * <ul>
     *     <li>1. sigma 回填传入到自定义请求头：`X_SIGMA` 中。</li>
     *     <li>2. key 回填传入到自定义请求头：`X_APP_ID` 中。</li>
     *     <li>3. appKey 回填传入到自定义请求头：`X_APP_KEY` 中（只在登录之后回填）。</li>
     * </ul>
     *
     * @param name 应用程序名称
     *
     * @return 应用配置，Json格式
     */
    Future<JsonObject> fetchByName(String name);

    /*
     * Get application by: appId = {xxx}
     */
    Future<JsonObject> fetchById(String appId);

    /*
     * Get data source by: appId = {xxx}
     * Unique for each app
     */
    Future<JsonObject> fetchSource(String appId);

    /*
     * Update
     */
    Future<JsonObject> updateBy(String appId, JsonObject data);
}
