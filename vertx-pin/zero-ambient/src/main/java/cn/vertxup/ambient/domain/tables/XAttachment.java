/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.ambient.domain.tables;


import cn.vertxup.ambient.domain.Db;
import cn.vertxup.ambient.domain.Keys;
import cn.vertxup.ambient.domain.tables.records.XAttachmentRecord;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class XAttachment extends TableImpl<XAttachmentRecord> {

    /**
     * The reference instance of <code>DB_ETERNAL.X_ATTACHMENT</code>
     */
    public static final XAttachment X_ATTACHMENT = new XAttachment();
    private static final long serialVersionUID = 1L;
    /**
     * The column <code>DB_ETERNAL.X_ATTACHMENT.KEY</code>. 「key」- 附件的ID值
     */
    public final TableField<XAttachmentRecord, String> KEY = createField(DSL.name("KEY"), SQLDataType.VARCHAR(36).nullable(false), this, "「key」- 附件的ID值");
    /**
     * The column <code>DB_ETERNAL.X_ATTACHMENT.NAME</code>. 「name」- 文件名（带扩展名）
     */
    public final TableField<XAttachmentRecord, String> NAME = createField(DSL.name("NAME"), SQLDataType.VARCHAR(255), this, "「name」- 文件名（带扩展名）");
    /**
     * The column <code>DB_ETERNAL.X_ATTACHMENT.EXTENSION</code>. 「extension」-
     * 文件扩展名
     */
    public final TableField<XAttachmentRecord, String> EXTENSION = createField(DSL.name("EXTENSION"), SQLDataType.VARCHAR(10), this, "「extension」- 文件扩展名");
    /**
     * The column <code>DB_ETERNAL.X_ATTACHMENT.TYPE</code>. 「type」-
     * 文件类型，直接关联zero.file.tree类型
     */
    public final TableField<XAttachmentRecord, String> TYPE = createField(DSL.name("TYPE"), SQLDataType.VARCHAR(128), this, "「type」- 文件类型，直接关联zero.file.tree类型");
    /**
     * The column <code>DB_ETERNAL.X_ATTACHMENT.MIME</code>. 「mime」- 该文件的MIME类型
     */
    public final TableField<XAttachmentRecord, String> MIME = createField(DSL.name("MIME"), SQLDataType.VARCHAR(128), this, "「mime」- 该文件的MIME类型");
    /**
     * The column <code>DB_ETERNAL.X_ATTACHMENT.SIZE</code>. 「size」- 该文件的尺寸
     */
    public final TableField<XAttachmentRecord, Integer> SIZE = createField(DSL.name("SIZE"), SQLDataType.INTEGER, this, "「size」- 该文件的尺寸");
    /**
     * The column <code>DB_ETERNAL.X_ATTACHMENT.STATUS</code>. 「status」-
     * 状态，PROGRESS / SUCCESS
     */
    public final TableField<XAttachmentRecord, String> STATUS = createField(DSL.name("STATUS"), SQLDataType.VARCHAR(12), this, "「status」- 状态，PROGRESS / SUCCESS");
    /**
     * The column <code>DB_ETERNAL.X_ATTACHMENT.DIRECTORY_ID</code>.
     * 「directoryId」- 文件存储所属目录
     */
    public final TableField<XAttachmentRecord, String> DIRECTORY_ID = createField(DSL.name("DIRECTORY_ID"), SQLDataType.VARCHAR(36), this, "「directoryId」- 文件存储所属目录");
    /**
     * The column <code>DB_ETERNAL.X_ATTACHMENT.STORE_WAY</code>. 「storeWay」-
     * 存储方式，BLOB / FILE / REMOTE
     */
    public final TableField<XAttachmentRecord, String> STORE_WAY = createField(DSL.name("STORE_WAY"), SQLDataType.VARCHAR(12), this, "「storeWay」- 存储方式，BLOB / FILE / REMOTE");
    /**
     * The column <code>DB_ETERNAL.X_ATTACHMENT.STORE_PATH</code>. 「storePath」-
     * 远程存储的目录信息（显示专用，去服务器和协议部分）
     */
    public final TableField<XAttachmentRecord, String> STORE_PATH = createField(DSL.name("STORE_PATH"), SQLDataType.VARCHAR(1024), this, "「storePath」- 远程存储的目录信息（显示专用，去服务器和协议部分）");
    /**
     * The column <code>DB_ETERNAL.X_ATTACHMENT.STORE_URI</code>. 「storeUri」-
     * 远程存储的目录URI部分
     */
    public final TableField<XAttachmentRecord, String> STORE_URI = createField(DSL.name("STORE_URI"), SQLDataType.VARCHAR(1024), this, "「storeUri」- 远程存储的目录URI部分");
    /**
     * The column <code>DB_ETERNAL.X_ATTACHMENT.MODEL_ID</code>. 「modelId」-
     * 关联的模型identifier，用于描述
     */
    public final TableField<XAttachmentRecord, String> MODEL_ID = createField(DSL.name("MODEL_ID"), SQLDataType.VARCHAR(255), this, "「modelId」- 关联的模型identifier，用于描述");
    /**
     * The column <code>DB_ETERNAL.X_ATTACHMENT.MODEL_KEY</code>. 「modelKey」-
     * 关联的模型记录ID，用于描述哪一个Model中的记录
     */
    public final TableField<XAttachmentRecord, String> MODEL_KEY = createField(DSL.name("MODEL_KEY"), SQLDataType.VARCHAR(36), this, "「modelKey」- 关联的模型记录ID，用于描述哪一个Model中的记录");
    /**
     * The column <code>DB_ETERNAL.X_ATTACHMENT.MODEL_CATEGORY</code>.
     * 「modelCategory」- 如果一个模型记录包含多种附件，则需要设置模型相关字段，等价于 field
     */
    public final TableField<XAttachmentRecord, String> MODEL_CATEGORY = createField(DSL.name("MODEL_CATEGORY"), SQLDataType.VARCHAR(36), this, "「modelCategory」- 如果一个模型记录包含多种附件，则需要设置模型相关字段，等价于 field");
    /**
     * The column <code>DB_ETERNAL.X_ATTACHMENT.FILE_NAME</code>. 「fileName」-
     * 原始文件名（不带扩展名）
     */
    public final TableField<XAttachmentRecord, String> FILE_NAME = createField(DSL.name("FILE_NAME"), SQLDataType.VARCHAR(255), this, "「fileName」- 原始文件名（不带扩展名）");
    /**
     * The column <code>DB_ETERNAL.X_ATTACHMENT.FILE_KEY</code>. 「fileKey」-
     * TPL模式中的文件唯一的key（全局唯一）
     */
    public final TableField<XAttachmentRecord, String> FILE_KEY = createField(DSL.name("FILE_KEY"), SQLDataType.VARCHAR(255), this, "「fileKey」- TPL模式中的文件唯一的key（全局唯一）");
    /**
     * The column <code>DB_ETERNAL.X_ATTACHMENT.FILE_URL</code>. 「fileUrl」-
     * 该文件的下载链接（全局唯一）
     */
    public final TableField<XAttachmentRecord, String> FILE_URL = createField(DSL.name("FILE_URL"), SQLDataType.VARCHAR(255), this, "「fileUrl」- 该文件的下载链接（全局唯一）");
    /**
     * The column <code>DB_ETERNAL.X_ATTACHMENT.FILE_PATH</code>. 「filePath」-
     * 该文件的存储地址，FILE时使用
     */
    public final TableField<XAttachmentRecord, String> FILE_PATH = createField(DSL.name("FILE_PATH"), SQLDataType.VARCHAR(255), this, "「filePath」- 该文件的存储地址，FILE时使用");
    /**
     * The column <code>DB_ETERNAL.X_ATTACHMENT.ACTIVE</code>. 「active」- 是否启用
     */
    public final TableField<XAttachmentRecord, Boolean> ACTIVE = createField(DSL.name("ACTIVE"), SQLDataType.BIT, this, "「active」- 是否启用");
    /**
     * The column <code>DB_ETERNAL.X_ATTACHMENT.SIGMA</code>. 「sigma」- 统一标识
     */
    public final TableField<XAttachmentRecord, String> SIGMA = createField(DSL.name("SIGMA"), SQLDataType.VARCHAR(32), this, "「sigma」- 统一标识");
    /**
     * The column <code>DB_ETERNAL.X_ATTACHMENT.METADATA</code>. 「metadata」-
     * 附加配置
     */
    public final TableField<XAttachmentRecord, String> METADATA = createField(DSL.name("METADATA"), SQLDataType.CLOB, this, "「metadata」- 附加配置");
    /**
     * The column <code>DB_ETERNAL.X_ATTACHMENT.LANGUAGE</code>. 「language」-
     * 使用的语言
     */
    public final TableField<XAttachmentRecord, String> LANGUAGE = createField(DSL.name("LANGUAGE"), SQLDataType.VARCHAR(8), this, "「language」- 使用的语言");
    /**
     * The column <code>DB_ETERNAL.X_ATTACHMENT.CREATED_AT</code>. 「createdAt」-
     * 创建时间
     */
    public final TableField<XAttachmentRecord, LocalDateTime> CREATED_AT = createField(DSL.name("CREATED_AT"), SQLDataType.LOCALDATETIME(0), this, "「createdAt」- 创建时间");
    /**
     * The column <code>DB_ETERNAL.X_ATTACHMENT.CREATED_BY</code>. 「createdBy」-
     * 创建人
     */
    public final TableField<XAttachmentRecord, String> CREATED_BY = createField(DSL.name("CREATED_BY"), SQLDataType.VARCHAR(36), this, "「createdBy」- 创建人");
    /**
     * The column <code>DB_ETERNAL.X_ATTACHMENT.UPDATED_AT</code>. 「updatedAt」-
     * 更新时间
     */
    public final TableField<XAttachmentRecord, LocalDateTime> UPDATED_AT = createField(DSL.name("UPDATED_AT"), SQLDataType.LOCALDATETIME(0), this, "「updatedAt」- 更新时间");
    /**
     * The column <code>DB_ETERNAL.X_ATTACHMENT.UPDATED_BY</code>. 「updatedBy」-
     * 更新人
     */
    public final TableField<XAttachmentRecord, String> UPDATED_BY = createField(DSL.name("UPDATED_BY"), SQLDataType.VARCHAR(36), this, "「updatedBy」- 更新人");

    private XAttachment(Name alias, Table<XAttachmentRecord> aliased) {
        this(alias, aliased, null);
    }

    private XAttachment(Name alias, Table<XAttachmentRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>DB_ETERNAL.X_ATTACHMENT</code> table reference
     */
    public XAttachment(String alias) {
        this(DSL.name(alias), X_ATTACHMENT);
    }

    /**
     * Create an aliased <code>DB_ETERNAL.X_ATTACHMENT</code> table reference
     */
    public XAttachment(Name alias) {
        this(alias, X_ATTACHMENT);
    }

    /**
     * Create a <code>DB_ETERNAL.X_ATTACHMENT</code> table reference
     */
    public XAttachment() {
        this(DSL.name("X_ATTACHMENT"), null);
    }

    public <O extends Record> XAttachment(Table<O> child, ForeignKey<O, XAttachmentRecord> key) {
        super(child, key, X_ATTACHMENT);
    }

    /**
     * The class holding records for this type
     */
    @Override
    public Class<XAttachmentRecord> getRecordType() {
        return XAttachmentRecord.class;
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Db.DB_ETERNAL;
    }

    @Override
    public UniqueKey<XAttachmentRecord> getPrimaryKey() {
        return Keys.KEY_X_ATTACHMENT_PRIMARY;
    }

    @Override
    public List<UniqueKey<XAttachmentRecord>> getUniqueKeys() {
        return Arrays.asList(Keys.KEY_X_ATTACHMENT_FILE_KEY, Keys.KEY_X_ATTACHMENT_FILE_URL, Keys.KEY_X_ATTACHMENT_FILE_PATH);
    }

    @Override
    public XAttachment as(String alias) {
        return new XAttachment(DSL.name(alias), this);
    }

    @Override
    public XAttachment as(Name alias) {
        return new XAttachment(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public XAttachment rename(String name) {
        return new XAttachment(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public XAttachment rename(Name name) {
        return new XAttachment(name, null);
    }
}
