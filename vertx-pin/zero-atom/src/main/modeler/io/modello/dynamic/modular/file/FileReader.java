package io.modello.dynamic.modular.file;

import io.vertx.mod.atom.modeling.Model;
import io.vertx.mod.atom.modeling.Schema;
import io.vertx.mod.atom.refine.Ao;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 读取目录 runtime 下的 Model 和 Entity 相关定义文件
 * 1. Model 的信息位于 runtime/model 目录下，每一个模型一个文件
 * 2. Entity 的信息位于 runtime/schema 目录下，每一个实体一个文件
 */
public class FileReader implements AoFile {

    @Override
    public Set<Model> readModels(final String appName) {
        return this.readModels(appName, null);
    }

    @Override
    public Set<Model> readModels(final String appName, final String outPath) {
        // 实体
        final Set<String> schemaFiles = this.readFiles("schema", outPath);
        final Set<Schema> schemata = schemaFiles.stream()
            .map(file -> Ao.toSchema(appName, file))
            .collect(Collectors.toSet());
        // 模型
        final Set<String> files = this.readFiles("model", outPath);
        return files.stream()
            .map(file -> Ao.toModel(appName, file))
            .map(model -> model.bind(schemata))
            .collect(Collectors.toSet());
    }

    @Override
    public Set<String> readServices() {
        return this.readFiles("service", null);
    }

    @Override
    public Set<String> readDataFiles() {
        return null;
    }

    private Set<String> readFiles(final String folder, final String outPath) {
        final String filePath;
        if (Ut.isNil(outPath)) {
            filePath = Ao.PATH.PATH_JSON + folder;
        } else {
            filePath = outPath + folder;
        }
        final List<String> files = Ut.ioFiles(filePath);
        final Set<String> results = new HashSet<>();
        files.forEach(file -> results.add(filePath + "/" + file));
        return results;
    }
}
