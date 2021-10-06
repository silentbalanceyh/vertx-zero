/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.ambient.domain.tables.pojos;


import cn.vertxup.ambient.domain.tables.interfaces.IXAttachment;

import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;

import java.time.LocalDateTime;


import static io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo.*;
/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class XAttachment implements VertxPojo, IXAttachment {

    private static final long serialVersionUID = 1L;

    private String        key;
    private String        storeWay;
    private String        status;
    private String        name;
    private String        fileName;
    private String        fileKey;
    private String        fileUrl;
    private String        filePath;
    private String        extension;
    private String        module;
    private String        mime;
    private Integer       size;
    private Boolean       active;
    private String        sigma;
    private String        metadata;
    private String        language;
    private LocalDateTime createdAt;
    private String        createdBy;
    private LocalDateTime updatedAt;
    private String        updatedBy;

    public XAttachment() {}

    public XAttachment(IXAttachment value) {
        this.key = value.getKey();
        this.storeWay = value.getStoreWay();
        this.status = value.getStatus();
        this.name = value.getName();
        this.fileName = value.getFileName();
        this.fileKey = value.getFileKey();
        this.fileUrl = value.getFileUrl();
        this.filePath = value.getFilePath();
        this.extension = value.getExtension();
        this.module = value.getModule();
        this.mime = value.getMime();
        this.size = value.getSize();
        this.active = value.getActive();
        this.sigma = value.getSigma();
        this.metadata = value.getMetadata();
        this.language = value.getLanguage();
        this.createdAt = value.getCreatedAt();
        this.createdBy = value.getCreatedBy();
        this.updatedAt = value.getUpdatedAt();
        this.updatedBy = value.getUpdatedBy();
    }

    public XAttachment(
        String        key,
        String        storeWay,
        String        status,
        String        name,
        String        fileName,
        String        fileKey,
        String        fileUrl,
        String        filePath,
        String        extension,
        String        module,
        String        mime,
        Integer       size,
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
        this.storeWay = storeWay;
        this.status = status;
        this.name = name;
        this.fileName = fileName;
        this.fileKey = fileKey;
        this.fileUrl = fileUrl;
        this.filePath = filePath;
        this.extension = extension;
        this.module = module;
        this.mime = mime;
        this.size = size;
        this.active = active;
        this.sigma = sigma;
        this.metadata = metadata;
        this.language = language;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }

        public XAttachment(io.vertx.core.json.JsonObject json) {
                this();
                fromJson(json);
        }

    /**
     * Getter for <code>DB_ETERNAL.X_ATTACHMENT.KEY</code>. 「key」- 附件的ID值
     */
    @Override
    public String getKey() {
        return this.key;
    }

    /**
     * Setter for <code>DB_ETERNAL.X_ATTACHMENT.KEY</code>. 「key」- 附件的ID值
     */
    @Override
    public XAttachment setKey(String key) {
        this.key = key;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.X_ATTACHMENT.STORE_WAY</code>. 「storeWay」-
     * 存储方式，BLOB / FILE / TPL / REMOTE
     */
    @Override
    public String getStoreWay() {
        return this.storeWay;
    }

    /**
     * Setter for <code>DB_ETERNAL.X_ATTACHMENT.STORE_WAY</code>. 「storeWay」-
     * 存储方式，BLOB / FILE / TPL / REMOTE
     */
    @Override
    public XAttachment setStoreWay(String storeWay) {
        this.storeWay = storeWay;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.X_ATTACHMENT.STATUS</code>. 「status」-
     * 状态，PROGRESS / SUCCESS
     */
    @Override
    public String getStatus() {
        return this.status;
    }

    /**
     * Setter for <code>DB_ETERNAL.X_ATTACHMENT.STATUS</code>. 「status」-
     * 状态，PROGRESS / SUCCESS
     */
    @Override
    public XAttachment setStatus(String status) {
        this.status = status;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.X_ATTACHMENT.NAME</code>. 「name」- 文件名（带扩展名）
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Setter for <code>DB_ETERNAL.X_ATTACHMENT.NAME</code>. 「name」- 文件名（带扩展名）
     */
    @Override
    public XAttachment setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.X_ATTACHMENT.FILE_NAME</code>. 「fileName」-
     * 原始文件名（不带扩展名）
     */
    @Override
    public String getFileName() {
        return this.fileName;
    }

    /**
     * Setter for <code>DB_ETERNAL.X_ATTACHMENT.FILE_NAME</code>. 「fileName」-
     * 原始文件名（不带扩展名）
     */
    @Override
    public XAttachment setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.X_ATTACHMENT.FILE_KEY</code>. 「fileKey」-
     * TPL模式中的文件唯一的key（全局唯一）
     */
    @Override
    public String getFileKey() {
        return this.fileKey;
    }

    /**
     * Setter for <code>DB_ETERNAL.X_ATTACHMENT.FILE_KEY</code>. 「fileKey」-
     * TPL模式中的文件唯一的key（全局唯一）
     */
    @Override
    public XAttachment setFileKey(String fileKey) {
        this.fileKey = fileKey;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.X_ATTACHMENT.FILE_URL</code>. 「fileUrl」-
     * 该文件的下载链接（全局唯一）
     */
    @Override
    public String getFileUrl() {
        return this.fileUrl;
    }

    /**
     * Setter for <code>DB_ETERNAL.X_ATTACHMENT.FILE_URL</code>. 「fileUrl」-
     * 该文件的下载链接（全局唯一）
     */
    @Override
    public XAttachment setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.X_ATTACHMENT.FILE_PATH</code>. 「filePath」-
     * 该文件的存储地址，FILE时使用
     */
    @Override
    public String getFilePath() {
        return this.filePath;
    }

    /**
     * Setter for <code>DB_ETERNAL.X_ATTACHMENT.FILE_PATH</code>. 「filePath」-
     * 该文件的存储地址，FILE时使用
     */
    @Override
    public XAttachment setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.X_ATTACHMENT.EXTENSION</code>. 「extension」-
     * 文件扩展名
     */
    @Override
    public String getExtension() {
        return this.extension;
    }

    /**
     * Setter for <code>DB_ETERNAL.X_ATTACHMENT.EXTENSION</code>. 「extension」-
     * 文件扩展名
     */
    @Override
    public XAttachment setExtension(String extension) {
        this.extension = extension;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.X_ATTACHMENT.MODULE</code>. 「module」- 业务标识
     */
    @Override
    public String getModule() {
        return this.module;
    }

    /**
     * Setter for <code>DB_ETERNAL.X_ATTACHMENT.MODULE</code>. 「module」- 业务标识
     */
    @Override
    public XAttachment setModule(String module) {
        this.module = module;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.X_ATTACHMENT.MIME</code>. 「mime」- 该文件的MIME类型
     */
    @Override
    public String getMime() {
        return this.mime;
    }

    /**
     * Setter for <code>DB_ETERNAL.X_ATTACHMENT.MIME</code>. 「mime」- 该文件的MIME类型
     */
    @Override
    public XAttachment setMime(String mime) {
        this.mime = mime;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.X_ATTACHMENT.SIZE</code>. 「size」- 该文件的尺寸
     */
    @Override
    public Integer getSize() {
        return this.size;
    }

    /**
     * Setter for <code>DB_ETERNAL.X_ATTACHMENT.SIZE</code>. 「size」- 该文件的尺寸
     */
    @Override
    public XAttachment setSize(Integer size) {
        this.size = size;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.X_ATTACHMENT.ACTIVE</code>. 「active」- 是否启用
     */
    @Override
    public Boolean getActive() {
        return this.active;
    }

    /**
     * Setter for <code>DB_ETERNAL.X_ATTACHMENT.ACTIVE</code>. 「active」- 是否启用
     */
    @Override
    public XAttachment setActive(Boolean active) {
        this.active = active;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.X_ATTACHMENT.SIGMA</code>. 「sigma」- 统一标识
     */
    @Override
    public String getSigma() {
        return this.sigma;
    }

    /**
     * Setter for <code>DB_ETERNAL.X_ATTACHMENT.SIGMA</code>. 「sigma」- 统一标识
     */
    @Override
    public XAttachment setSigma(String sigma) {
        this.sigma = sigma;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.X_ATTACHMENT.METADATA</code>. 「metadata」-
     * 附加配置
     */
    @Override
    public String getMetadata() {
        return this.metadata;
    }

    /**
     * Setter for <code>DB_ETERNAL.X_ATTACHMENT.METADATA</code>. 「metadata」-
     * 附加配置
     */
    @Override
    public XAttachment setMetadata(String metadata) {
        this.metadata = metadata;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.X_ATTACHMENT.LANGUAGE</code>. 「language」-
     * 使用的语言
     */
    @Override
    public String getLanguage() {
        return this.language;
    }

    /**
     * Setter for <code>DB_ETERNAL.X_ATTACHMENT.LANGUAGE</code>. 「language」-
     * 使用的语言
     */
    @Override
    public XAttachment setLanguage(String language) {
        this.language = language;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.X_ATTACHMENT.CREATED_AT</code>. 「createdAt」-
     * 创建时间
     */
    @Override
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    /**
     * Setter for <code>DB_ETERNAL.X_ATTACHMENT.CREATED_AT</code>. 「createdAt」-
     * 创建时间
     */
    @Override
    public XAttachment setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.X_ATTACHMENT.CREATED_BY</code>. 「createdBy」-
     * 创建人
     */
    @Override
    public String getCreatedBy() {
        return this.createdBy;
    }

    /**
     * Setter for <code>DB_ETERNAL.X_ATTACHMENT.CREATED_BY</code>. 「createdBy」-
     * 创建人
     */
    @Override
    public XAttachment setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.X_ATTACHMENT.UPDATED_AT</code>. 「updatedAt」-
     * 更新时间
     */
    @Override
    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    /**
     * Setter for <code>DB_ETERNAL.X_ATTACHMENT.UPDATED_AT</code>. 「updatedAt」-
     * 更新时间
     */
    @Override
    public XAttachment setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.X_ATTACHMENT.UPDATED_BY</code>. 「updatedBy」-
     * 更新人
     */
    @Override
    public String getUpdatedBy() {
        return this.updatedBy;
    }

    /**
     * Setter for <code>DB_ETERNAL.X_ATTACHMENT.UPDATED_BY</code>. 「updatedBy」-
     * 更新人
     */
    @Override
    public XAttachment setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("XAttachment (");

        sb.append(key);
        sb.append(", ").append(storeWay);
        sb.append(", ").append(status);
        sb.append(", ").append(name);
        sb.append(", ").append(fileName);
        sb.append(", ").append(fileKey);
        sb.append(", ").append(fileUrl);
        sb.append(", ").append(filePath);
        sb.append(", ").append(extension);
        sb.append(", ").append(module);
        sb.append(", ").append(mime);
        sb.append(", ").append(size);
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
    public void from(IXAttachment from) {
        setKey(from.getKey());
        setStoreWay(from.getStoreWay());
        setStatus(from.getStatus());
        setName(from.getName());
        setFileName(from.getFileName());
        setFileKey(from.getFileKey());
        setFileUrl(from.getFileUrl());
        setFilePath(from.getFilePath());
        setExtension(from.getExtension());
        setModule(from.getModule());
        setMime(from.getMime());
        setSize(from.getSize());
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
    public <E extends IXAttachment> E into(E into) {
        into.from(this);
        return into;
    }
}
