package io.modello.dynamic.modular.file.excel;

import io.vertx.core.json.JsonObject;
import io.vertx.mod.atom.cv.AoTable;
import io.vertx.mod.atom.modeling.Model;
import io.vertx.mod.atom.modeling.Schema;
import io.vertx.mod.atom.refine.Ao;
import io.vertx.up.plugin.excel.ExcelClient;
import io.vertx.up.plugin.excel.ExcelInfix;
import io.vertx.up.plugin.excel.atom.ExRecord;
import io.vertx.up.plugin.excel.atom.ExTable;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Excel模型分析器，用于分析Excel模板
 * 1. 针对 Model 的模板进行处理：处理 model.ci.xlsx 文件
 * 2. 根据文件路径将一个 Excel 转换成 Set<Model> 的集合
 * 3. 创建 Model 和 Entity 的记录并且关联创建 Join
 * 4. 创建 属性、字段、键、索引
 */
public class ExAnalyzer {

    private static final ExcelClient CLIENT = ExcelInfix.getClient();
    private transient final ConcurrentMap<String, Set<ExRecord>> recordMap
        = new ConcurrentHashMap<>();
    /* 应用程序名称 */
    private transient String appName;

    private ExAnalyzer(final String file) {
        // this.tables.addAll(CLIENT.ingest(file));
        this.initMap(CLIENT.ingest(file));
    }

    public static ExAnalyzer create(final String file) {
        return new ExAnalyzer(file);
    }

    public ExAnalyzer on(final String appName) {
        /* 名空间计算 */
        this.appName = appName;
        return this;
    }

    public Set<Model> build(final Set<Schema> schemaSet) {
        /*
         * 查找所有的 M_MODEL
         */
        final Set<Model> modelSet = new HashSet<>();
        final Set<ExRecord> models = this.get(AoTable.MODEL);
        models.forEach(model -> {
            /*
             * 根据 model 查找 joins, attributes
             */
            final Set<ExRecord> joins = ExIn.join(this.get(AoTable.JOIN), model);
            final Set<ExRecord> attributes = ExIn.searchModel(this.get(AoTable.ATTRIBUTE), model);
            /*
             * 数据收集齐全，直接转换
             */
            final JsonObject modelJson = ExOut.toModel(model, joins, attributes);
            /*
             * Model 连接上 Schema
             */
            modelSet.add(Ao.toModel(this.appName, modelJson).bind(schemaSet));
        });
        return modelSet;
    }

    private Set<ExRecord> get(final String key) {
        return this.recordMap.getOrDefault(key, new HashSet<>());
    }

    private void initMap(final Set<ExTable> tables) {
        final List<String> from = new ArrayList<String>() {
            {
                this.add(AoTable.MODEL);
                this.add(AoTable.ATTRIBUTE);
                this.add(AoTable.JOIN);
            }
        };
        final List<Set<ExRecord>> to = new ArrayList<Set<ExRecord>>() {
            {
                this.add(ExIn.record(tables, AoTable.MODEL));
                this.add(ExIn.record(tables, AoTable.ATTRIBUTE));
                this.add(ExIn.record(tables, AoTable.JOIN));
            }
        };
        this.recordMap.putAll(Ut.elementZip(from, to));
    }
}
