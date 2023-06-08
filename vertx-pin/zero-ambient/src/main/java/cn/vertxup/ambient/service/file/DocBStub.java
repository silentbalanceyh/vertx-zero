package cn.vertxup.ambient.service.file;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface DocBStub {
    /*
     * Start Document Engine by `appId`
     *
     * 1. Fetch `X_CATEGORY` by `appId` where type equal `zero.directory`, the default should be
     * -- document
     * -- department
     * -- workflow
     *
     * 2. Input the data into `ExIo` to initialize the whole storage, here are three parts:
     * -- configuration for initialize
     * -- data for menu ( Folder )
     * -- configuration for document fetching ( X_ATTACHMENT )
     *
     * Each record data structure
     * {
     *      "...",
     *      "criteria": {
     *          "condition"
     *      },
     *      "directory": {
     *          "directory rule stored into X_CATEGORY metadata field"
     *      }
     * }
     */
    Future<JsonArray> initialize(String appId, String type);

    Future<JsonArray> initialize(String appId, String type, String name);
}
