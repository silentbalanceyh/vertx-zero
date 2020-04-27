package io.vertx.tp.modular.file;

import io.vertx.tp.atom.modeling.Model;
import io.vertx.tp.atom.modeling.Schema;
import io.vertx.tp.atom.refine.Ao;
import io.vertx.tp.modular.file.excel.ExAnalyzer;
import io.vertx.tp.modular.file.excel.ExModello;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/*
 * Excel类型的 Marshal，用于读取数据
 */
public class ExcelReader implements AoFile {
    private final transient String rootPath;

    public ExcelReader() {
        this(Ao.Path.PATH_EXCEL);
    }

    public ExcelReader(final String rootPath) {
        final String normalized;
        if (Objects.isNull(rootPath)) {
            /* runtime/excel */
            normalized = Ao.Path.PATH_EXCEL;
        } else {
            /* End with '/' */
            if (!rootPath.endsWith("/")) {
                normalized = rootPath + "/";
            } else {
                normalized = rootPath;
            }
        }
        this.rootPath = normalized;
    }

    @Override
    public Set<Model> readModels(final String appName) {
        final Set<String> files = this.readFiles("schema");

        Ao.infoUca(this.getClass(), "找到符合条件的文件：{0}", String.valueOf(files.size()));
        /*
         * 先构造 Schema 处理实体
         */
        final Set<Schema> schemas = ExModello.create(files)
                .on(appName).build();
        Ao.infoUca(this.getClass(), "合计构造了模型：{0}", schemas.size());
        /*
         * 将 Model 和 Schema 连接
         */
        final Set<Model> models = new HashSet<>();
        files.stream().map(ExAnalyzer::create)
                // 和应用绑定
                .map(analyzer -> analyzer.on(appName))
                //  构造最终的Model
                .map(analyzer -> analyzer.build(schemas))
                .forEach(models::addAll);
        return models;
    }

    @Override
    public Set<String> readServices() {
        return null;
    }

    @Override
    public Set<String> readDataFiles() {
        return this.readFiles("data");
    }


    private Set<String> readFiles(final String folder) {
        final String root = this.rootPath + folder;
        final List<String> files = Ut.ioFiles(root);
        return files.stream()
                .filter(file -> !file.startsWith("~"))  // 过滤Office的临时文件
                .map(item -> root + "/" + item).collect(Collectors.toSet());
    }
}
