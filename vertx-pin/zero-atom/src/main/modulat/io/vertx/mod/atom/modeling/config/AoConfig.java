package io.vertx.mod.atom.modeling.config;

import com.fasterxml.jackson.databind.ClassDeserializer;
import com.fasterxml.jackson.databind.ClassSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/*
 * Configuration for different implementation here.
 * 1. Schema
 * 2. Model
 * 3. Pin etc.
 */
public class AoConfig {
    /*
     * - Schema 实现类
     * - Model 实现类
     * - Record 实现类
     * - Pin 实现类
     * - Switcher 实现类
     */
    @JsonSerialize(using = ClassSerializer.class)
    @JsonDeserialize(using = ClassDeserializer.class)
    private transient Class<?> implSchema;
    @JsonSerialize(using = ClassSerializer.class)
    @JsonDeserialize(using = ClassDeserializer.class)
    private transient Class<?> implModel;
    @JsonSerialize(using = ClassSerializer.class)
    @JsonDeserialize(using = ClassDeserializer.class)
    private transient Class<?> implRecord;
    @JsonSerialize(using = ClassSerializer.class)
    @JsonDeserialize(using = ClassDeserializer.class)
    private transient Class<?> implPin;

    @JsonSerialize(using = ClassSerializer.class)
    @JsonDeserialize(using = ClassDeserializer.class)
    private transient Class<?> implSwitcher;
    /*
     * 默认名空间
     */
    private transient String namespace;
    /*
     * 默认Excel定义目录
     * runtime/excel/
     */
    private transient String defineExcel;
    /*
     * 默认Json定义目录
     * runtime/json/
     */
    private transient String defineJson;
    /*
     * 是否带有修正器，修整器路径（比对时使用）
     * runtime/adjuster/config.json
     */
    private transient String configAdjuster;
    /*
     * runtime/adjuster/modeling
     */
    private transient String configModeling;
    private transient Boolean sqlDebug;

    public String getConfigModeling() {
        return this.configModeling;
    }

    public void setConfigModeling(final String configModeling) {
        this.configModeling = configModeling;
    }

    public Class<?> getImplSchema() {
        return this.implSchema;
    }

    public void setImplSchema(final Class<?> implSchema) {
        this.implSchema = implSchema;
    }

    public Class<?> getImplModel() {
        return this.implModel;
    }

    public void setImplModel(final Class<?> implModel) {
        this.implModel = implModel;
    }

    public Class<?> getImplRecord() {
        return this.implRecord;
    }

    public void setImplRecord(final Class<?> implRecord) {
        this.implRecord = implRecord;
    }

    public String getNamespace() {
        return this.namespace;
    }

    public void setNamespace(final String namespace) {
        this.namespace = namespace;
    }

    public String getDefineExcel() {
        return this.defineExcel;
    }

    public void setDefineExcel(final String defineExcel) {
        this.defineExcel = defineExcel;
    }

    public String getDefineJson() {
        return this.defineJson;
    }

    public void setDefineJson(final String defineJson) {
        this.defineJson = defineJson;
    }

    public String getConfigAdjuster() {
        return this.configAdjuster;
    }

    public void setConfigAdjuster(final String configAdjuster) {
        this.configAdjuster = configAdjuster;
    }

    public Boolean getSqlDebug() {
        return this.sqlDebug;
    }

    public void setSqlDebug(final Boolean sqlDebug) {
        this.sqlDebug = sqlDebug;
    }

    public Class<?> getImplPin() {
        return this.implPin;
    }

    public void setImplPin(final Class<?> implPin) {
        this.implPin = implPin;
    }

    public Class<?> getImplSwitcher() {
        return this.implSwitcher;
    }

    public void setImplSwitcher(final Class<?> implSwitcher) {
        this.implSwitcher = implSwitcher;
    }
}
