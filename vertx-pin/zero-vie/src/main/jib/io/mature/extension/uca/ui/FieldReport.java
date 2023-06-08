package io.mature.extension.uca.ui;

import io.vertx.mod.ui.cv.em.ControlType;

import java.util.Objects;

/**
 * ## 「Pojo」属性报表
 *
 * ### 1. 基本介绍
 *
 * 属性报表专用数据结构。
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class FieldReport {
    /** `UI_FORM / UI_LIST`中存储的identifier模型标识符。 */
    private transient String identifier;
    /** `UI_FORM / UI_LIST`中的编码字段`code`，系统编码。 */
    private transient String control;
    /** 模型名，`M_MODEL` 中定义属性的 name 字段。 **/
    private transient String attribute;
    /** UI配置中属性名，`UI_FIELD`属性`name`，`UI_LIST`中的`dataIndex`，`UI_FORM`中的`field`。 **/
    private transient String uiField;
    /** 控件类型：`FORM / LIST / NONE` */
    private transient ControlType type;
    /** 字段匹配结果状态，{@link FieldStatus} */
    private transient FieldStatus status;

    /**
     * @return {@link FieldStatus} status
     */
    public FieldStatus getStatus() {
        return this.status;
    }

    /**
     * @param status {@link FieldStatus}
     */
    public void setStatus(final FieldStatus status) {
        this.status = status;
    }

    /**
     * @return {@link ControlType} type
     */
    public ControlType getType() {
        return this.type;
    }

    /**
     * @param type {@link ControlType}
     */
    public void setType(final ControlType type) {
        this.type = type;
    }

    /**
     * @return {@link String} identifier
     */
    public String getIdentifier() {
        return this.identifier;
    }

    /**
     * @param identifier {@link String}
     */
    public void setIdentifier(final String identifier) {
        this.identifier = identifier;
    }

    /**
     * @return {@link String} control
     */
    public String getControl() {
        return this.control;
    }

    /**
     * @param control {@link String}
     */
    public void setControl(final String control) {
        this.control = control;
    }

    /**
     * @return {@link String} attribute
     */
    public String getAttribute() {
        return this.attribute;
    }

    /**
     * @param attribute {@link String}
     */
    public void setAttribute(final String attribute) {
        this.attribute = attribute;
    }

    /**
     * @return {@link String} uiField
     */
    public String getUiField() {
        return this.uiField;
    }

    /**
     * @param uiField {@link String}
     */
    public void setUiField(final String uiField) {
        this.uiField = uiField;
    }

    /**
     * 核心计算方法，计算内部的`status`状态值。
     */
    public void calculate() {
        if (Objects.isNull(this.attribute)) {
            this.status = FieldStatus.REMAIN;
        } else {
            if (this.attribute.equals(this.uiField)) {
                this.status = FieldStatus.MATCH;
            } else {
                /*
                 * 过滤 batch, filter, view
                 */
                this.status = FieldStatus.INVALID;
            }
        }
    }
}
