package io.horizon.spi.ui;

import cn.vertxup.ui.domain.tables.pojos.UiVisitor;
import io.vertx.core.Future;
import io.vertx.up.atom.typed.UData;

/**
 * This interface is for UIVisitor when
 * dynamic = true
 *
 * 1) The UI_VISITOR must contain `controlId` in static mode.
 * 2) When dynamic = true, it means that the controlId has been calculated by
 * `runComponent` here, the component must be extend from UiHunter to get the
 * correct controlId instead.
 *
 * The request format is as following
 * // <pre><code class="json">
 * {
 *     alias: "直接配置，通常是：EDIT, ADD, FILTER, LIST",
 *     data: {
 *         "用于选择处理时专用的插件参数专用配置。"
 *     },
 *     identifier: "原始标识规则选择符",
 *     language: "语言信息",
 *     page: "当前页面ID（UI_PAGE专用）",
 *     type: "当前查找专用类型，如：FORM / LIST",
 *     position: "当前页面位置，对应 position",
 *     view: "当前页面专用视图，对应 view"
 * }
 * // </code></pre>
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface UiHunter {
    /*
     * Here the data means request parameters that input from front-end
     * and the `visitor` is the original UIVisitor instance stored in UI_VISITOR
     *
     * 1. The data came from front-end, Javascript Client
     * 2. The dim parameters came from original UIVisitor such as
     * -- page
     * -- type: LIST / FORM
     * -- path: VIEW / POSITION / ALIAS ( Combined by original )
     */
    Future<String> seek(UData data, UiVisitor visitor);
}
