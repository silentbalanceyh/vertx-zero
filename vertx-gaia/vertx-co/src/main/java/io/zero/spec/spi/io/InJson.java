package io.zero.spec.spi.io;

import io.vertx.core.json.JsonObject;

import java.io.InputStream;

/**
 * SPI专用接口，用于读取文件，转换成 JsonObject，根据读取的文件
 * 得到最终的 Json 内容，以适配不同的文件格式
 *
 * @author lang : 2023/4/24
 */
public interface InJson {
    /**
     * 读取文件，转换成 JsonObject
     *
     * @param jsonFile 文件路径
     */
    JsonObject load(String jsonFile);

    JsonObject load(InputStream jsonStream);
}
