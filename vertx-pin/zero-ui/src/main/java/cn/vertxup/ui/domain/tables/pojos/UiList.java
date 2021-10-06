/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.ui.domain.tables.pojos;


import cn.vertxup.ui.domain.tables.interfaces.IUiList;

import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;

import java.time.LocalDateTime;


import static io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo.*;
/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class UiList implements VertxPojo, IUiList {

    private static final long serialVersionUID = 1L;

    private String        key;
    private String        name;
    private String        code;
    private String        identifier;
    private String        vQuery;
    private String        vSearch;
    private String        vTable;
    private String        vSegment;
    private Boolean       dynamicColumn;
    private Boolean       dynamicSwitch;
    private String        optionsAjax;
    private String        optionsSubmit;
    private String        options;
    private String        classCombiner;
    private Boolean       active;
    private String        sigma;
    private String        metadata;
    private String        language;
    private LocalDateTime createdAt;
    private String        createdBy;
    private LocalDateTime updatedAt;
    private String        updatedBy;

    public UiList() {}

    public UiList(IUiList value) {
        this.key = value.getKey();
        this.name = value.getName();
        this.code = value.getCode();
        this.identifier = value.getIdentifier();
        this.vQuery = value.getVQuery();
        this.vSearch = value.getVSearch();
        this.vTable = value.getVTable();
        this.vSegment = value.getVSegment();
        this.dynamicColumn = value.getDynamicColumn();
        this.dynamicSwitch = value.getDynamicSwitch();
        this.optionsAjax = value.getOptionsAjax();
        this.optionsSubmit = value.getOptionsSubmit();
        this.options = value.getOptions();
        this.classCombiner = value.getClassCombiner();
        this.active = value.getActive();
        this.sigma = value.getSigma();
        this.metadata = value.getMetadata();
        this.language = value.getLanguage();
        this.createdAt = value.getCreatedAt();
        this.createdBy = value.getCreatedBy();
        this.updatedAt = value.getUpdatedAt();
        this.updatedBy = value.getUpdatedBy();
    }

    public UiList(
        String        key,
        String        name,
        String        code,
        String        identifier,
        String        vQuery,
        String        vSearch,
        String        vTable,
        String        vSegment,
        Boolean       dynamicColumn,
        Boolean       dynamicSwitch,
        String        optionsAjax,
        String        optionsSubmit,
        String        options,
        String        classCombiner,
        Boolean       active,
        String        sigma,
        String        metadata,
        String        language,
        LocalDateTime createdAt,
        String        createdBy,
        LocalDateTime updatedAt,
        String        updatedBy
    ) {
        this.key = key;
        this.name = name;
        this.code = code;
        this.identifier = identifier;
        this.vQuery = vQuery;
        this.vSearch = vSearch;
        this.vTable = vTable;
        this.vSegment = vSegment;
        this.dynamicColumn = dynamicColumn;
        this.dynamicSwitch = dynamicSwitch;
        this.optionsAjax = optionsAjax;
        this.optionsSubmit = optionsSubmit;
        this.options = options;
        this.classCombiner = classCombiner;
        this.active = active;
        this.sigma = sigma;
        this.metadata = metadata;
        this.language = language;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }

        public UiList(io.vertx.core.json.JsonObject json) {
                this();
                fromJson(json);
        }

    /**
     * Getter for <code>DB_ETERNAL.UI_LIST.KEY</code>. 「key」- 主键
     */
    @Override
    public String getKey() {
        return this.key;
    }

    /**
     * Setter for <code>DB_ETERNAL.UI_LIST.KEY</code>. 「key」- 主键
     */
    @Override
    public UiList setKey(String key) {
        this.key = key;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.UI_LIST.NAME</code>. 「name」- 名称
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Setter for <code>DB_ETERNAL.UI_LIST.NAME</code>. 「name」- 名称
     */
    @Override
    public UiList setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.UI_LIST.CODE</code>. 「code」- 系统编码
     */
    @Override
    public String getCode() {
        return this.code;
    }

    /**
     * Setter for <code>DB_ETERNAL.UI_LIST.CODE</code>. 「code」- 系统编码
     */
    @Override
    public UiList setCode(String code) {
        this.code = code;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.UI_LIST.IDENTIFIER</code>. 「identifier」-
     * 表单所属的模型ID
     */
    @Override
    public String getIdentifier() {
        return this.identifier;
    }

    /**
     * Setter for <code>DB_ETERNAL.UI_LIST.IDENTIFIER</code>. 「identifier」-
     * 表单所属的模型ID
     */
    @Override
    public UiList setIdentifier(String identifier) {
        this.identifier = identifier;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.UI_LIST.V_QUERY</code>. 「vQuery」- 连接query到
     * grid -&gt; query 节点
     */
    @Override
    public String getVQuery() {
        return this.vQuery;
    }

    /**
     * Setter for <code>DB_ETERNAL.UI_LIST.V_QUERY</code>. 「vQuery」- 连接query到
     * grid -&gt; query 节点
     */
    @Override
    public UiList setVQuery(String vQuery) {
        this.vQuery = vQuery;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.UI_LIST.V_SEARCH</code>. 「vSearch」- 连接search到
     * grid -&gt; options 节点
     */
    @Override
    public String getVSearch() {
        return this.vSearch;
    }

    /**
     * Setter for <code>DB_ETERNAL.UI_LIST.V_SEARCH</code>. 「vSearch」- 连接search到
     * grid -&gt; options 节点
     */
    @Override
    public UiList setVSearch(String vSearch) {
        this.vSearch = vSearch;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.UI_LIST.V_TABLE</code>. 「vTable」- 连接table到
     * grid -&gt; table 节点
     */
    @Override
    public String getVTable() {
        return this.vTable;
    }

    /**
     * Setter for <code>DB_ETERNAL.UI_LIST.V_TABLE</code>. 「vTable」- 连接table到
     * grid -&gt; table 节点
     */
    @Override
    public UiList setVTable(String vTable) {
        this.vTable = vTable;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.UI_LIST.V_SEGMENT</code>. 「vSegment」-
     * Json结构，对应到 grid -&gt; component 节点
     */
    @Override
    public String getVSegment() {
        return this.vSegment;
    }

    /**
     * Setter for <code>DB_ETERNAL.UI_LIST.V_SEGMENT</code>. 「vSegment」-
     * Json结构，对应到 grid -&gt; component 节点
     */
    @Override
    public UiList setVSegment(String vSegment) {
        this.vSegment = vSegment;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.UI_LIST.DYNAMIC_COLUMN</code>.
     * 「dynamicColumn」- 动态列？
     */
    @Override
    public Boolean getDynamicColumn() {
        return this.dynamicColumn;
    }

    /**
     * Setter for <code>DB_ETERNAL.UI_LIST.DYNAMIC_COLUMN</code>.
     * 「dynamicColumn」- 动态列？
     */
    @Override
    public UiList setDynamicColumn(Boolean dynamicColumn) {
        this.dynamicColumn = dynamicColumn;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.UI_LIST.DYNAMIC_SWITCH</code>.
     * 「dynamicSwitch」- 动态切换？
     */
    @Override
    public Boolean getDynamicSwitch() {
        return this.dynamicSwitch;
    }

    /**
     * Setter for <code>DB_ETERNAL.UI_LIST.DYNAMIC_SWITCH</code>.
     * 「dynamicSwitch」- 动态切换？
     */
    @Override
    public UiList setDynamicSwitch(Boolean dynamicSwitch) {
        this.dynamicSwitch = dynamicSwitch;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.UI_LIST.OPTIONS_AJAX</code>. 「optionsAjax」-
     * 所有 ajax系列的配置
     */
    @Override
    public String getOptionsAjax() {
        return this.optionsAjax;
    }

    /**
     * Setter for <code>DB_ETERNAL.UI_LIST.OPTIONS_AJAX</code>. 「optionsAjax」-
     * 所有 ajax系列的配置
     */
    @Override
    public UiList setOptionsAjax(String optionsAjax) {
        this.optionsAjax = optionsAjax;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.UI_LIST.OPTIONS_SUBMIT</code>.
     * 「optionsSubmit」- 所有提交类的配置
     */
    @Override
    public String getOptionsSubmit() {
        return this.optionsSubmit;
    }

    /**
     * Setter for <code>DB_ETERNAL.UI_LIST.OPTIONS_SUBMIT</code>.
     * 「optionsSubmit」- 所有提交类的配置
     */
    @Override
    public UiList setOptionsSubmit(String optionsSubmit) {
        this.optionsSubmit = optionsSubmit;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.UI_LIST.OPTIONS</code>. 「options」- 配置项
     */
    @Override
    public String getOptions() {
        return this.options;
    }

    /**
     * Setter for <code>DB_ETERNAL.UI_LIST.OPTIONS</code>. 「options」- 配置项
     */
    @Override
    public UiList setOptions(String options) {
        this.options = options;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.UI_LIST.CLASS_COMBINER</code>.
     * 「classCombiner」- 组装器
     */
    @Override
    public String getClassCombiner() {
        return this.classCombiner;
    }

    /**
     * Setter for <code>DB_ETERNAL.UI_LIST.CLASS_COMBINER</code>.
     * 「classCombiner」- 组装器
     */
    @Override
    public UiList setClassCombiner(String classCombiner) {
        this.classCombiner = classCombiner;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.UI_LIST.ACTIVE</code>. 「active」- 是否启用
     */
    @Override
    public Boolean getActive() {
        return this.active;
    }

    /**
     * Setter for <code>DB_ETERNAL.UI_LIST.ACTIVE</code>. 「active」- 是否启用
     */
    @Override
    public UiList setActive(Boolean active) {
        this.active = active;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.UI_LIST.SIGMA</code>. 「sigma」- 统一标识
     */
    @Override
    public String getSigma() {
        return this.sigma;
    }

    /**
     * Setter for <code>DB_ETERNAL.UI_LIST.SIGMA</code>. 「sigma」- 统一标识
     */
    @Override
    public UiList setSigma(String sigma) {
        this.sigma = sigma;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.UI_LIST.METADATA</code>. 「metadata」- 附加配置
     */
    @Override
    public String getMetadata() {
        return this.metadata;
    }

    /**
     * Setter for <code>DB_ETERNAL.UI_LIST.METADATA</code>. 「metadata」- 附加配置
     */
    @Override
    public UiList setMetadata(String metadata) {
        this.metadata = metadata;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.UI_LIST.LANGUAGE</code>. 「language」- 使用的语言
     */
    @Override
    public String getLanguage() {
        return this.language;
    }

    /**
     * Setter for <code>DB_ETERNAL.UI_LIST.LANGUAGE</code>. 「language」- 使用的语言
     */
    @Override
    public UiList setLanguage(String language) {
        this.language = language;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.UI_LIST.CREATED_AT</code>. 「createdAt」- 创建时间
     */
    @Override
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    /**
     * Setter for <code>DB_ETERNAL.UI_LIST.CREATED_AT</code>. 「createdAt」- 创建时间
     */
    @Override
    public UiList setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.UI_LIST.CREATED_BY</code>. 「createdBy」- 创建人
     */
    @Override
    public String getCreatedBy() {
        return this.createdBy;
    }

    /**
     * Setter for <code>DB_ETERNAL.UI_LIST.CREATED_BY</code>. 「createdBy」- 创建人
     */
    @Override
    public UiList setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.UI_LIST.UPDATED_AT</code>. 「updatedAt」- 更新时间
     */
    @Override
    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    /**
     * Setter for <code>DB_ETERNAL.UI_LIST.UPDATED_AT</code>. 「updatedAt」- 更新时间
     */
    @Override
    public UiList setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.UI_LIST.UPDATED_BY</code>. 「updatedBy」- 更新人
     */
    @Override
    public String getUpdatedBy() {
        return this.updatedBy;
    }

    /**
     * Setter for <code>DB_ETERNAL.UI_LIST.UPDATED_BY</code>. 「updatedBy」- 更新人
     */
    @Override
    public UiList setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("UiList (");

        sb.append(key);
        sb.append(", ").append(name);
        sb.append(", ").append(code);
        sb.append(", ").append(identifier);
        sb.append(", ").append(vQuery);
        sb.append(", ").append(vSearch);
        sb.append(", ").append(vTable);
        sb.append(", ").append(vSegment);
        sb.append(", ").append(dynamicColumn);
        sb.append(", ").append(dynamicSwitch);
        sb.append(", ").append(optionsAjax);
        sb.append(", ").append(optionsSubmit);
        sb.append(", ").append(options);
        sb.append(", ").append(classCombiner);
        sb.append(", ").append(active);
        sb.append(", ").append(sigma);
        sb.append(", ").append(metadata);
        sb.append(", ").append(language);
        sb.append(", ").append(createdAt);
        sb.append(", ").append(createdBy);
        sb.append(", ").append(updatedAt);
        sb.append(", ").append(updatedBy);

        sb.append(")");
        return sb.toString();
    }

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    @Override
    public void from(IUiList from) {
        setKey(from.getKey());
        setName(from.getName());
        setCode(from.getCode());
        setIdentifier(from.getIdentifier());
        setVQuery(from.getVQuery());
        setVSearch(from.getVSearch());
        setVTable(from.getVTable());
        setVSegment(from.getVSegment());
        setDynamicColumn(from.getDynamicColumn());
        setDynamicSwitch(from.getDynamicSwitch());
        setOptionsAjax(from.getOptionsAjax());
        setOptionsSubmit(from.getOptionsSubmit());
        setOptions(from.getOptions());
        setClassCombiner(from.getClassCombiner());
        setActive(from.getActive());
        setSigma(from.getSigma());
        setMetadata(from.getMetadata());
        setLanguage(from.getLanguage());
        setCreatedAt(from.getCreatedAt());
        setCreatedBy(from.getCreatedBy());
        setUpdatedAt(from.getUpdatedAt());
        setUpdatedBy(from.getUpdatedBy());
    }

    @Override
    public <E extends IUiList> E into(E into) {
        into.from(this);
        return into;
    }
}
