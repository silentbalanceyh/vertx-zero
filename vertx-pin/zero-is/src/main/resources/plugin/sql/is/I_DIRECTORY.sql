-- liquibase formatted sql

-- changeset Lang:i-directory-1
DROP TABLE IF EXISTS `I_DIRECTORY`;
CREATE TABLE `I_DIRECTORY`
(
    `KEY`             VARCHAR(36) COMMENT '「key」- 目录主键',
    `NAME`            VARCHAR(255) NOT NULL COMMENT '「name」- 目录名称',
    `CODE`            VARCHAR(255) NOT NULL COMMENT '「code」- 目录编号',

    -- 目录全名
    -- 目录本身树结构（创建目录时必须拷贝 STORE_ID）
    /*
     * 目录的 STORE_PATH 具有二义性
     * 1. 如果是实际目录，则是真实地址，从 / 开始
     * 2. 如果是非实际目录，则是虚拟地址，使用 软链接规范
     *
     * 关于文件路径 / 目录路径的计算流程
     */
    `STORE_PATH`      VARCHAR(512) COMMENT '「storePath」- 目录相对路径',
    `LINKED_PATH`     VARCHAR(512) COMMENT '「linkedPath」- 链接路径，type = LINK 时专用',
    `PARENT_ID`       VARCHAR(36) COMMENT '「parentId」- 父目录ID',
    `CATEGORY`        VARCHAR(36) COMMENT '「category」- 目录连接的类型树',

    -- 目录视图专用
    -- 1）category, parentId 同时存在，该目录一定是 STORE（手工创建）
    -- 2）category存在，parentId 不存在，该目录是根目录 ROOT，旗下会包含所有的所属文件
    -- 3）runComponent存在时，目录包含执行组件，用于抓取目录下的文件专用
    -- 4) integrationId不存在时，目录为虚拟目录，和 runComponent 配合执行
    `TYPE`            VARCHAR(36)  NOT NULL COMMENT '「type」- 目录类型：INTEGRATION / STORE / LINK',
    `OWNER`           VARCHAR(36) COMMENT '「owner」- 目录访问人',
    `INTEGRATION_ID`  VARCHAR(36) COMMENT '「integrationId」- 该目录关联的 Integration，不关联则不转存',
    `RUN_COMPONENT`   TEXT COMMENT '「runComponent」- 目录执行组件，抓文件专用',

    -- 目录计算专用规则（以目录为核心权限）
    -- 私有目录只能通过创建的方式操作，不可由系统设置，且私有目录只能用户自己访问，且私有必定包含 owner
    `VISIT`           BIT COMMENT '「visit」- 公有 / 私有',

    /*
     * 「目录」
     *   r - 只读权限，可读取目录以及打开目录，下载目录中的文件
     *   w - 可写权限，可以在目录中创建新目录，上传文件
     *   x - 执行权限，可重命名目录、删除目录（软删除硬删除）
     * 「文件」
     *   r - 可下载（目录内）
     *   w - 可上传（目录内）
     */
    `VISIT_MODE`      VARCHAR(36) COMMENT '「visitMode」- 目录模式：只读 / 可写，以后扩展为其他',
    `VISIT_ROLE`      TEXT COMMENT '「visitRole」- 目录访问角色',
    `VISIT_GROUP`     TEXT COMMENT '「visitGroup」- 目录访问组',
    `VISIT_COMPONENT` TEXT COMMENT '「visitComponent」- 目录访问控制专用组件',

    -- 特殊字段
    `SIGMA`           VARCHAR(32) COMMENT '「sigma」- 统一标识',
    `LANGUAGE`        VARCHAR(10) COMMENT '「language」- 使用的语言',

    /*
     * 回收站中 active = false
     * 软删除，放在根目录下：/.Trash 的目录中，即：
     * active = true，目录路径 = storePath
     * active = false，目录路径 = /.Trash/storePath
     */
    `ACTIVE`          BIT COMMENT '「active」- 是否启用',
    `METADATA`        TEXT COMMENT '「metadata」- 附加配置数据',

    -- Auditor字段
    `CREATED_AT`      DATETIME COMMENT '「createdAt」- 创建时间',
    `CREATED_BY`      VARCHAR(36) COMMENT '「createdBy」- 创建人',
    `UPDATED_AT`      DATETIME COMMENT '「updatedAt」- 更新时间',
    `UPDATED_BY`      VARCHAR(36) COMMENT '「updatedBy」- 更新人',
    PRIMARY KEY (`KEY`)
);
-- changeset Lang:i-directory-2
ALTER TABLE I_DIRECTORY
    ADD UNIQUE (`CODE`, `SIGMA`);
ALTER TABLE I_DIRECTORY
    ADD UNIQUE (`NAME`, `PARENT_ID`, `SIGMA`);
ALTER TABLE I_DIRECTORY
    ADD UNIQUE (`STORE_PATH`, `SIGMA`);