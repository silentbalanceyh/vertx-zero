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
