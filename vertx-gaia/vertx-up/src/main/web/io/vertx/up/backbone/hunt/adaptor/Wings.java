package io.vertx.up.backbone.hunt.adaptor;

import io.vertx.core.http.HttpServerResponse;
import io.vertx.up.commune.Envelop;

/**
 * 「Co」Zero Framework
 *
 * Here I added new configuration `freedom` to zero framework as critical data specification here for
 * old system integration here. This configuration is new released after `0.5.3`
 *
 * ```yaml
 * // <pre><code>
 * zero:
 *   freedom: true
 * // </code></pre>
 * ```
 *
 * * The default value of `freedom` is false, it means that you must be under zero data specification.
 * * You Also can use your own setting to set `freedom` to true, it means original raw data.
 *
 * Here are two features
 *
 * 1. Build response by `Accept` and `Content-Type`, set the media type
 * 2. Convert media type to actual response data.
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Wings {
    /**
     * Pre-Condition
     * 1) Response is not ended
     * 2) The request method is not HEAD
     *
     * @param response ServerResponse reference
     * @param envelop  The response uniform model
     */
    void output(HttpServerResponse response, Envelop envelop);
}
