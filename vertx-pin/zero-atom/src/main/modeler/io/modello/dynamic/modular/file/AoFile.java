package io.modello.dynamic.modular.file;

import io.vertx.mod.atom.modeling.Model;

import java.util.Set;

/**
 * 初始化数据之前读取OOB数据包的专用接口，提供应用程序名称用于针对单个应用执行
 * OOB 数据包的复制，生成最终的数据包内容。
 */
public interface AoFile {

    /**
     * 读取建模数据
     * M_MODEL,
     * -- M_ATTRIBUTE, M_JOIN
     * M_ENTITY,
     * -- M_FIELD, M_KEY, M_INDEX
     */
    Set<Model> readModels(String appName);

    /*
     * 读取建模数据：从前一步输出数据中读取
     */
    Set<Model> readModels(String appName, String outPath);

    /**
     * 读取接口数据
     */
    Set<String> readServices();

    /*
     * 读取所有数据文件
     */
    Set<String> readDataFiles();
}
