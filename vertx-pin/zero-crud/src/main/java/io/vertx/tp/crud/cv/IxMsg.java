package io.vertx.tp.crud.cv;

public interface IxMsg {

    String INIT_INFO = "--- file = {0}, key = {1}";

    String CACHE_KEY_PROJECTION = " Update projection of cacheKey = {0}";

    String FILE_UPLOAD = "File information, filename = `{0}`, uploaded = `{1}`";

    String FILE_LOADED = "Successfully to finish loading ! data file = {0}";
}
