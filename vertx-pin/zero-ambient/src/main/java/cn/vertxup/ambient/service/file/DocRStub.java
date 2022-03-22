package cn.vertxup.ambient.service.file;

import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;

import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface DocRStub {
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
    Future<JsonArray> treeDir(String appId, String type);

    /*
     * Document + Directory
     */
    Future<JsonArray> fetchDoc(String sigma, String directoryId);

    Future<JsonArray> fetchTrash(String sigma);

    /*
     * Document Only
     * 1. keyword is owner name
     * 2. keyword is document name
     */
    Future<JsonArray> searchDoc(String sigma, String keyword);

    /*
     * Document Download
     */
    Future<Buffer> downloadDoc(String key);

    Future<Buffer> downloadDoc(Set<String> keys);
}
