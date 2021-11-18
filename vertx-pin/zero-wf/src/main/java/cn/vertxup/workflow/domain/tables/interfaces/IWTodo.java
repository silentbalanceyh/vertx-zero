/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.workflow.domain.tables.interfaces;


import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;

import java.io.Serializable;
import java.time.LocalDateTime;


import static io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo.*;
/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public interface IWTodo extends VertxPojo, Serializable {

    /**
     * Setter for <code>DB_ETERNAL.W_TODO.KEY</code>. 「key」- 待办主键
     */
    public IWTodo setKey(String value);

    /**
     * Getter for <code>DB_ETERNAL.W_TODO.KEY</code>. 「key」- 待办主键
     */
    public String getKey();

    /**
     * Setter for <code>DB_ETERNAL.W_TODO.SERIAL</code>. 「serial」- 待办编号，使用
     * X_NUMBER 生成
     */
    public IWTodo setSerial(String value);

    /**
     * Getter for <code>DB_ETERNAL.W_TODO.SERIAL</code>. 「serial」- 待办编号，使用
     * X_NUMBER 生成
     */
    public String getSerial();

    /**
     * Setter for <code>DB_ETERNAL.W_TODO.NAME</code>. 「name」- 待办名称（标题）
     */
    public IWTodo setName(String value);

    /**
     * Getter for <code>DB_ETERNAL.W_TODO.NAME</code>. 「name」- 待办名称（标题）
     */
    public String getName();

    /**
     * Setter for <code>DB_ETERNAL.W_TODO.CODE</code>. 「code」-
     * 待办系统码，使用流程时候关联流程的任务ID
     */
    public IWTodo setCode(String value);

    /**
     * Getter for <code>DB_ETERNAL.W_TODO.CODE</code>. 「code」-
     * 待办系统码，使用流程时候关联流程的任务ID
     */
    public String getCode();

    /**
     * Setter for <code>DB_ETERNAL.W_TODO.ICON</code>. 「icon」- 待办显示的图标
     */
    public IWTodo setIcon(String value);

    /**
     * Getter for <code>DB_ETERNAL.W_TODO.ICON</code>. 「icon」- 待办显示的图标
     */
    public String getIcon();

    /**
     * Setter for <code>DB_ETERNAL.W_TODO.STATUS</code>. 「status」- 待办状态
     */
    public IWTodo setStatus(String value);

    /**
     * Getter for <code>DB_ETERNAL.W_TODO.STATUS</code>. 「status」- 待办状态
     */
    public String getStatus();

    /**
     * Setter for <code>DB_ETERNAL.W_TODO.TODO_URL</code>. 「todoUrl」- 待办路径
     */
    public IWTodo setTodoUrl(String value);

    /**
     * Getter for <code>DB_ETERNAL.W_TODO.TODO_URL</code>. 「todoUrl」- 待办路径
     */
    public String getTodoUrl();

    /**
     * Setter for <code>DB_ETERNAL.W_TODO.TYPE</code>. 「type」- 待办类型
     */
    public IWTodo setType(String value);

    /**
     * Getter for <code>DB_ETERNAL.W_TODO.TYPE</code>. 「type」- 待办类型
     */
    public String getType();

    /**
     * Setter for <code>DB_ETERNAL.W_TODO.MODEL_ID</code>. 「modelId」-
     * 关联的模型identifier，用于描述
     */
    public IWTodo setModelId(String value);

    /**
     * Getter for <code>DB_ETERNAL.W_TODO.MODEL_ID</code>. 「modelId」-
     * 关联的模型identifier，用于描述
     */
    public String getModelId();

    /**
     * Setter for <code>DB_ETERNAL.W_TODO.MODEL_KEY</code>. 「modelKey」-
     * 关联的模型记录ID，用于描述哪一个Model中的记录
     */
    public IWTodo setModelKey(String value);

    /**
     * Getter for <code>DB_ETERNAL.W_TODO.MODEL_KEY</code>. 「modelKey」-
     * 关联的模型记录ID，用于描述哪一个Model中的记录
     */
    public String getModelKey();

    /**
     * Setter for <code>DB_ETERNAL.W_TODO.MODEL_CATEGORY</code>.
     * 「modelCategory」- 关联的category记录，只包含叶节点
     */
    public IWTodo setModelCategory(String value);

    /**
     * Getter for <code>DB_ETERNAL.W_TODO.MODEL_CATEGORY</code>.
     * 「modelCategory」- 关联的category记录，只包含叶节点
     */
    public String getModelCategory();

    /**
     * Setter for <code>DB_ETERNAL.W_TODO.MODEL_FORM</code>. 「modelForm」-
     * 待办专用的表单关联
     */
    public IWTodo setModelForm(String value);

    /**
     * Getter for <code>DB_ETERNAL.W_TODO.MODEL_FORM</code>. 「modelForm」-
     * 待办专用的表单关联
     */
    public String getModelForm();

    /**
     * Setter for <code>DB_ETERNAL.W_TODO.MODEL_COMPONENT</code>.
     * 「modelComponent」- 关联的待办组件记录
     */
    public IWTodo setModelComponent(String value);

    /**
     * Getter for <code>DB_ETERNAL.W_TODO.MODEL_COMPONENT</code>.
     * 「modelComponent」- 关联的待办组件记录
     */
    public String getModelComponent();

    /**
     * Setter for <code>DB_ETERNAL.W_TODO.INSTANCE</code>. 「instance」- 是否启用工作流？
     */
    public IWTodo setInstance(Boolean value);

    /**
     * Getter for <code>DB_ETERNAL.W_TODO.INSTANCE</code>. 「instance」- 是否启用工作流？
     */
    public Boolean getInstance();

    /**
     * Setter for <code>DB_ETERNAL.W_TODO.TRACE_ID</code>. 「traceId」-
     * 同一个流程的待办执行分组
     */
    public IWTodo setTraceId(String value);

    /**
     * Getter for <code>DB_ETERNAL.W_TODO.TRACE_ID</code>. 「traceId」-
     * 同一个流程的待办执行分组
     */
    public String getTraceId();

    /**
     * Setter for <code>DB_ETERNAL.W_TODO.TRACE_TASK_ID</code>. 「traceTaskId」-
     * 和待办绑定的taskId
     */
    public IWTodo setTraceTaskId(String value);

    /**
     * Getter for <code>DB_ETERNAL.W_TODO.TRACE_TASK_ID</code>. 「traceTaskId」-
     * 和待办绑定的taskId
     */
    public String getTraceTaskId();

    /**
     * Setter for <code>DB_ETERNAL.W_TODO.TRACE_ORDER</code>. 「traceOrder」-
     * 待办的处理顺序
     */
    public IWTodo setTraceOrder(Integer value);

    /**
     * Getter for <code>DB_ETERNAL.W_TODO.TRACE_ORDER</code>. 「traceOrder」-
     * 待办的处理顺序
     */
    public Integer getTraceOrder();

    /**
     * Setter for <code>DB_ETERNAL.W_TODO.TRACE_END</code>. 「traceEnd」- 主单执行完成
     */
    public IWTodo setTraceEnd(Boolean value);

    /**
     * Getter for <code>DB_ETERNAL.W_TODO.TRACE_END</code>. 「traceEnd」- 主单执行完成
     */
    public Boolean getTraceEnd();

    /**
     * Setter for <code>DB_ETERNAL.W_TODO.PARENT_ID</code>. 「parentId」-
     * 待办支持父子集结构，父待办执行时候子待办同样执行
     */
    public IWTodo setParentId(String value);

    /**
     * Getter for <code>DB_ETERNAL.W_TODO.PARENT_ID</code>. 「parentId」-
     * 待办支持父子集结构，父待办执行时候子待办同样执行
     */
    public String getParentId();

    /**
     * Setter for <code>DB_ETERNAL.W_TODO.COMMENT</code>. 「comment」- 待办描述
     */
    public IWTodo setComment(String value);

    /**
     * Getter for <code>DB_ETERNAL.W_TODO.COMMENT</code>. 「comment」- 待办描述
     */
    public String getComment();

    /**
     * Setter for <code>DB_ETERNAL.W_TODO.COMMENT_APPROVAL</code>.
     * 「commentApproval」- 审批描述
     */
    public IWTodo setCommentApproval(String value);

    /**
     * Getter for <code>DB_ETERNAL.W_TODO.COMMENT_APPROVAL</code>.
     * 「commentApproval」- 审批描述
     */
    public String getCommentApproval();

    /**
     * Setter for <code>DB_ETERNAL.W_TODO.COMMENT_REJECT</code>.
     * 「commentReject」- 拒绝理由
     */
    public IWTodo setCommentReject(String value);

    /**
     * Getter for <code>DB_ETERNAL.W_TODO.COMMENT_REJECT</code>.
     * 「commentReject」- 拒绝理由
     */
    public String getCommentReject();

    /**
     * Setter for <code>DB_ETERNAL.W_TODO.TO_GROUP_MODE</code>. 「toGroupMode」-
     * 部门、业务组、组、角色、地点等
     */
    public IWTodo setToGroupMode(String value);

    /**
     * Getter for <code>DB_ETERNAL.W_TODO.TO_GROUP_MODE</code>. 「toGroupMode」-
     * 部门、业务组、组、角色、地点等
     */
    public String getToGroupMode();

    /**
     * Setter for <code>DB_ETERNAL.W_TODO.TO_GROUP</code>. 「toGroup」- 待办指定组
     */
    public IWTodo setToGroup(String value);

    /**
     * Getter for <code>DB_ETERNAL.W_TODO.TO_GROUP</code>. 「toGroup」- 待办指定组
     */
    public String getToGroup();

    /**
     * Setter for <code>DB_ETERNAL.W_TODO.TO_USER</code>. 「toUser」- 待办指定人
     */
    public IWTodo setToUser(String value);

    /**
     * Getter for <code>DB_ETERNAL.W_TODO.TO_USER</code>. 「toUser」- 待办指定人
     */
    public String getToUser();

    /**
     * Setter for <code>DB_ETERNAL.W_TODO.TO_ROLE</code>. 「toRole」- 待办角色（集体）
     */
    public IWTodo setToRole(String value);

    /**
     * Getter for <code>DB_ETERNAL.W_TODO.TO_ROLE</code>. 「toRole」- 待办角色（集体）
     */
    public String getToRole();

    /**
     * Setter for <code>DB_ETERNAL.W_TODO.ACTIVE</code>. 「active」- 是否启用
     */
    public IWTodo setActive(Boolean value);

    /**
     * Getter for <code>DB_ETERNAL.W_TODO.ACTIVE</code>. 「active」- 是否启用
     */
    public Boolean getActive();

    /**
     * Setter for <code>DB_ETERNAL.W_TODO.SIGMA</code>. 「sigma」- 统一标识
     */
    public IWTodo setSigma(String value);

    /**
     * Getter for <code>DB_ETERNAL.W_TODO.SIGMA</code>. 「sigma」- 统一标识
     */
    public String getSigma();

    /**
     * Setter for <code>DB_ETERNAL.W_TODO.METADATA</code>. 「metadata」- 附加配置
     */
    public IWTodo setMetadata(String value);

    /**
     * Getter for <code>DB_ETERNAL.W_TODO.METADATA</code>. 「metadata」- 附加配置
     */
    public String getMetadata();

    /**
     * Setter for <code>DB_ETERNAL.W_TODO.LANGUAGE</code>. 「language」- 使用的语言
     */
    public IWTodo setLanguage(String value);

    /**
     * Getter for <code>DB_ETERNAL.W_TODO.LANGUAGE</code>. 「language」- 使用的语言
     */
    public String getLanguage();

    /**
     * Setter for <code>DB_ETERNAL.W_TODO.OWNER</code>. 「owner」- 拥有者
     */
    public IWTodo setOwner(String value);

    /**
     * Getter for <code>DB_ETERNAL.W_TODO.OWNER</code>. 「owner」- 拥有者
     */
    public String getOwner();

    /**
     * Setter for <code>DB_ETERNAL.W_TODO.SUPERVISOR</code>. 「supervisor」- 监督人
     */
    public IWTodo setSupervisor(String value);

    /**
     * Getter for <code>DB_ETERNAL.W_TODO.SUPERVISOR</code>. 「supervisor」- 监督人
     */
    public String getSupervisor();

    /**
     * Setter for <code>DB_ETERNAL.W_TODO.ASSIGNED_BY</code>. 「assignedBy」-
     * 待办指派人
     */
    public IWTodo setAssignedBy(String value);

    /**
     * Getter for <code>DB_ETERNAL.W_TODO.ASSIGNED_BY</code>. 「assignedBy」-
     * 待办指派人
     */
    public String getAssignedBy();

    /**
     * Setter for <code>DB_ETERNAL.W_TODO.ASSIGNED_AT</code>. 「assignedAt」- 指派时间
     */
    public IWTodo setAssignedAt(LocalDateTime value);

    /**
     * Getter for <code>DB_ETERNAL.W_TODO.ASSIGNED_AT</code>. 「assignedAt」- 指派时间
     */
    public LocalDateTime getAssignedAt();

    /**
     * Setter for <code>DB_ETERNAL.W_TODO.ACCEPTED_BY</code>. 「acceptedBy」-
     * 待办接收人
     */
    public IWTodo setAcceptedBy(String value);

    /**
     * Getter for <code>DB_ETERNAL.W_TODO.ACCEPTED_BY</code>. 「acceptedBy」-
     * 待办接收人
     */
    public String getAcceptedBy();

    /**
     * Setter for <code>DB_ETERNAL.W_TODO.ACCEPTED_AT</code>. 「acceptedAt」- 接收时间
     */
    public IWTodo setAcceptedAt(LocalDateTime value);

    /**
     * Getter for <code>DB_ETERNAL.W_TODO.ACCEPTED_AT</code>. 「acceptedAt」- 接收时间
     */
    public LocalDateTime getAcceptedAt();

    /**
     * Setter for <code>DB_ETERNAL.W_TODO.FINISHED_BY</code>. 「finishedBy」-
     * 待办完成人
     */
    public IWTodo setFinishedBy(String value);

    /**
     * Getter for <code>DB_ETERNAL.W_TODO.FINISHED_BY</code>. 「finishedBy」-
     * 待办完成人
     */
    public String getFinishedBy();

    /**
     * Setter for <code>DB_ETERNAL.W_TODO.FINISHED_AT</code>. 「finishedAt」- 完成时间
     */
    public IWTodo setFinishedAt(LocalDateTime value);

    /**
     * Getter for <code>DB_ETERNAL.W_TODO.FINISHED_AT</code>. 「finishedAt」- 完成时间
     */
    public LocalDateTime getFinishedAt();

    /**
     * Setter for <code>DB_ETERNAL.W_TODO.EXPIRED_AT</code>. 「expiredAt」- 超时时间
     */
    public IWTodo setExpiredAt(LocalDateTime value);

    /**
     * Getter for <code>DB_ETERNAL.W_TODO.EXPIRED_AT</code>. 「expiredAt」- 超时时间
     */
    public LocalDateTime getExpiredAt();

    /**
     * Setter for <code>DB_ETERNAL.W_TODO.CREATED_AT</code>. 「createdAt」- 创建时间
     */
    public IWTodo setCreatedAt(LocalDateTime value);

    /**
     * Getter for <code>DB_ETERNAL.W_TODO.CREATED_AT</code>. 「createdAt」- 创建时间
     */
    public LocalDateTime getCreatedAt();

    /**
     * Setter for <code>DB_ETERNAL.W_TODO.CREATED_BY</code>. 「createdBy」- 创建人
     */
    public IWTodo setCreatedBy(String value);

    /**
     * Getter for <code>DB_ETERNAL.W_TODO.CREATED_BY</code>. 「createdBy」- 创建人
     */
    public String getCreatedBy();

    /**
     * Setter for <code>DB_ETERNAL.W_TODO.UPDATED_AT</code>. 「updatedAt」- 更新时间
     */
    public IWTodo setUpdatedAt(LocalDateTime value);

    /**
     * Getter for <code>DB_ETERNAL.W_TODO.UPDATED_AT</code>. 「updatedAt」- 更新时间
     */
    public LocalDateTime getUpdatedAt();

    /**
     * Setter for <code>DB_ETERNAL.W_TODO.UPDATED_BY</code>. 「updatedBy」- 更新人
     */
    public IWTodo setUpdatedBy(String value);

    /**
     * Getter for <code>DB_ETERNAL.W_TODO.UPDATED_BY</code>. 「updatedBy」- 更新人
     */
    public String getUpdatedBy();

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    /**
     * Load data from another generated Record/POJO implementing the common
     * interface IWTodo
     */
    public void from(IWTodo from);

    /**
     * Copy data into another generated Record/POJO implementing the common
     * interface IWTodo
     */
    public <E extends IWTodo> E into(E into);

        @Override
        public default IWTodo fromJson(io.vertx.core.json.JsonObject json) {
                setOrThrow(this::setKey,json::getString,"KEY","java.lang.String");
                setOrThrow(this::setSerial,json::getString,"SERIAL","java.lang.String");
                setOrThrow(this::setName,json::getString,"NAME","java.lang.String");
                setOrThrow(this::setCode,json::getString,"CODE","java.lang.String");
                setOrThrow(this::setIcon,json::getString,"ICON","java.lang.String");
                setOrThrow(this::setStatus,json::getString,"STATUS","java.lang.String");
                setOrThrow(this::setTodoUrl,json::getString,"TODO_URL","java.lang.String");
                setOrThrow(this::setType,json::getString,"TYPE","java.lang.String");
                setOrThrow(this::setModelId,json::getString,"MODEL_ID","java.lang.String");
                setOrThrow(this::setModelKey,json::getString,"MODEL_KEY","java.lang.String");
                setOrThrow(this::setModelCategory,json::getString,"MODEL_CATEGORY","java.lang.String");
                setOrThrow(this::setModelForm,json::getString,"MODEL_FORM","java.lang.String");
                setOrThrow(this::setModelComponent,json::getString,"MODEL_COMPONENT","java.lang.String");
                setOrThrow(this::setInstance,json::getBoolean,"INSTANCE","java.lang.Boolean");
                setOrThrow(this::setTraceId,json::getString,"TRACE_ID","java.lang.String");
                setOrThrow(this::setTraceTaskId,json::getString,"TRACE_TASK_ID","java.lang.String");
                setOrThrow(this::setTraceOrder,json::getInteger,"TRACE_ORDER","java.lang.Integer");
                setOrThrow(this::setTraceEnd,json::getBoolean,"TRACE_END","java.lang.Boolean");
                setOrThrow(this::setParentId,json::getString,"PARENT_ID","java.lang.String");
                setOrThrow(this::setComment,json::getString,"COMMENT","java.lang.String");
                setOrThrow(this::setCommentApproval,json::getString,"COMMENT_APPROVAL","java.lang.String");
                setOrThrow(this::setCommentReject,json::getString,"COMMENT_REJECT","java.lang.String");
                setOrThrow(this::setToGroupMode,json::getString,"TO_GROUP_MODE","java.lang.String");
                setOrThrow(this::setToGroup,json::getString,"TO_GROUP","java.lang.String");
                setOrThrow(this::setToUser,json::getString,"TO_USER","java.lang.String");
                setOrThrow(this::setToRole,json::getString,"TO_ROLE","java.lang.String");
                setOrThrow(this::setActive,json::getBoolean,"ACTIVE","java.lang.Boolean");
                setOrThrow(this::setSigma,json::getString,"SIGMA","java.lang.String");
                setOrThrow(this::setMetadata,json::getString,"METADATA","java.lang.String");
                setOrThrow(this::setLanguage,json::getString,"LANGUAGE","java.lang.String");
                setOrThrow(this::setOwner,json::getString,"OWNER","java.lang.String");
                setOrThrow(this::setSupervisor,json::getString,"SUPERVISOR","java.lang.String");
                setOrThrow(this::setAssignedBy,json::getString,"ASSIGNED_BY","java.lang.String");
                setOrThrow(this::setAssignedAt,key -> {String s = json.getString(key); return s==null?null:java.time.LocalDateTime.parse(s);},"ASSIGNED_AT","java.time.LocalDateTime");
                setOrThrow(this::setAcceptedBy,json::getString,"ACCEPTED_BY","java.lang.String");
                setOrThrow(this::setAcceptedAt,key -> {String s = json.getString(key); return s==null?null:java.time.LocalDateTime.parse(s);},"ACCEPTED_AT","java.time.LocalDateTime");
                setOrThrow(this::setFinishedBy,json::getString,"FINISHED_BY","java.lang.String");
                setOrThrow(this::setFinishedAt,key -> {String s = json.getString(key); return s==null?null:java.time.LocalDateTime.parse(s);},"FINISHED_AT","java.time.LocalDateTime");
                setOrThrow(this::setExpiredAt,key -> {String s = json.getString(key); return s==null?null:java.time.LocalDateTime.parse(s);},"EXPIRED_AT","java.time.LocalDateTime");
                setOrThrow(this::setCreatedAt,key -> {String s = json.getString(key); return s==null?null:java.time.LocalDateTime.parse(s);},"CREATED_AT","java.time.LocalDateTime");
                setOrThrow(this::setCreatedBy,json::getString,"CREATED_BY","java.lang.String");
                setOrThrow(this::setUpdatedAt,key -> {String s = json.getString(key); return s==null?null:java.time.LocalDateTime.parse(s);},"UPDATED_AT","java.time.LocalDateTime");
                setOrThrow(this::setUpdatedBy,json::getString,"UPDATED_BY","java.lang.String");
                return this;
        }


        @Override
        public default io.vertx.core.json.JsonObject toJson() {
                io.vertx.core.json.JsonObject json = new io.vertx.core.json.JsonObject();
                json.put("KEY",getKey());
                json.put("SERIAL",getSerial());
                json.put("NAME",getName());
                json.put("CODE",getCode());
                json.put("ICON",getIcon());
                json.put("STATUS",getStatus());
                json.put("TODO_URL",getTodoUrl());
                json.put("TYPE",getType());
                json.put("MODEL_ID",getModelId());
                json.put("MODEL_KEY",getModelKey());
                json.put("MODEL_CATEGORY",getModelCategory());
                json.put("MODEL_FORM",getModelForm());
                json.put("MODEL_COMPONENT",getModelComponent());
                json.put("INSTANCE",getInstance());
                json.put("TRACE_ID",getTraceId());
                json.put("TRACE_TASK_ID",getTraceTaskId());
                json.put("TRACE_ORDER",getTraceOrder());
                json.put("TRACE_END",getTraceEnd());
                json.put("PARENT_ID",getParentId());
                json.put("COMMENT",getComment());
                json.put("COMMENT_APPROVAL",getCommentApproval());
                json.put("COMMENT_REJECT",getCommentReject());
                json.put("TO_GROUP_MODE",getToGroupMode());
                json.put("TO_GROUP",getToGroup());
                json.put("TO_USER",getToUser());
                json.put("TO_ROLE",getToRole());
                json.put("ACTIVE",getActive());
                json.put("SIGMA",getSigma());
                json.put("METADATA",getMetadata());
                json.put("LANGUAGE",getLanguage());
                json.put("OWNER",getOwner());
                json.put("SUPERVISOR",getSupervisor());
                json.put("ASSIGNED_BY",getAssignedBy());
                json.put("ASSIGNED_AT",getAssignedAt()==null?null:getAssignedAt().toString());
                json.put("ACCEPTED_BY",getAcceptedBy());
                json.put("ACCEPTED_AT",getAcceptedAt()==null?null:getAcceptedAt().toString());
                json.put("FINISHED_BY",getFinishedBy());
                json.put("FINISHED_AT",getFinishedAt()==null?null:getFinishedAt().toString());
                json.put("EXPIRED_AT",getExpiredAt()==null?null:getExpiredAt().toString());
                json.put("CREATED_AT",getCreatedAt()==null?null:getCreatedAt().toString());
                json.put("CREATED_BY",getCreatedBy());
                json.put("UPDATED_AT",getUpdatedAt()==null?null:getUpdatedAt().toString());
                json.put("UPDATED_BY",getUpdatedBy());
                return json;
        }

}
