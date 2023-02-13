/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.erp.domain.tables.records;


import cn.vertxup.erp.domain.tables.EContract;
import cn.vertxup.erp.domain.tables.interfaces.IEContract;

import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.jooq.Record1;
import org.jooq.impl.UpdatableRecordImpl;


import static io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo.*;
/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class EContractRecord extends UpdatableRecordImpl<EContractRecord> implements VertxPojo, IEContract {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>DB_HOTEL.E_CONTRACT.KEY</code>. 「key」- 合同主键
     */
    @Override
    public EContractRecord setKey(String value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>DB_HOTEL.E_CONTRACT.KEY</code>. 「key」- 合同主键
     */
    @Override
    public String getKey() {
        return (String) get(0);
    }

    /**
     * Setter for <code>DB_HOTEL.E_CONTRACT.NAME</code>. 「name」- 合同名称
     */
    @Override
    public EContractRecord setName(String value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>DB_HOTEL.E_CONTRACT.NAME</code>. 「name」- 合同名称
     */
    @Override
    public String getName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>DB_HOTEL.E_CONTRACT.CODE</code>. 「code」- 合同编号
     */
    @Override
    public EContractRecord setCode(String value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>DB_HOTEL.E_CONTRACT.CODE</code>. 「code」- 合同编号
     */
    @Override
    public String getCode() {
        return (String) get(2);
    }

    /**
     * Setter for <code>DB_HOTEL.E_CONTRACT.DEPT_ID</code>. 「deptId」- 所属部门,
     * resource.departments
     */
    @Override
    public EContractRecord setDeptId(String value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>DB_HOTEL.E_CONTRACT.DEPT_ID</code>. 「deptId」- 所属部门,
     * resource.departments
     */
    @Override
    public String getDeptId() {
        return (String) get(3);
    }

    /**
     * Setter for <code>DB_HOTEL.E_CONTRACT.TYPE</code>. 「type」-
     * 合同分类，zero.contract
     */
    @Override
    public EContractRecord setType(String value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>DB_HOTEL.E_CONTRACT.TYPE</code>. 「type」-
     * 合同分类，zero.contract
     */
    @Override
    public String getType() {
        return (String) get(4);
    }

    /**
     * Setter for <code>DB_HOTEL.E_CONTRACT.STATUS</code>. 「status」-
     * 合同状态，zero.contract.status
     */
    @Override
    public EContractRecord setStatus(String value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>DB_HOTEL.E_CONTRACT.STATUS</code>. 「status」-
     * 合同状态，zero.contract.status
     */
    @Override
    public String getStatus() {
        return (String) get(5);
    }

    /**
     * Setter for <code>DB_HOTEL.E_CONTRACT.BUDGET</code>. 「budget」-
     * 所属预算，zero.contract.budget
     */
    @Override
    public EContractRecord setBudget(String value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>DB_HOTEL.E_CONTRACT.BUDGET</code>. 「budget」-
     * 所属预算，zero.contract.budget
     */
    @Override
    public String getBudget() {
        return (String) get(6);
    }

    /**
     * Setter for <code>DB_HOTEL.E_CONTRACT.LEVEL</code>. 「level」-
     * 合同级别，zero.contract.level
     */
    @Override
    public EContractRecord setLevel(String value) {
        set(7, value);
        return this;
    }

    /**
     * Getter for <code>DB_HOTEL.E_CONTRACT.LEVEL</code>. 「level」-
     * 合同级别，zero.contract.level
     */
    @Override
    public String getLevel() {
        return (String) get(7);
    }

    /**
     * Setter for <code>DB_HOTEL.E_CONTRACT.TITLE</code>. 「title」- 合同标题
     */
    @Override
    public EContractRecord setTitle(String value) {
        set(8, value);
        return this;
    }

    /**
     * Getter for <code>DB_HOTEL.E_CONTRACT.TITLE</code>. 「title」- 合同标题
     */
    @Override
    public String getTitle() {
        return (String) get(8);
    }

    /**
     * Setter for <code>DB_HOTEL.E_CONTRACT.SUBJECT</code>. 「subject」- 合同目标
     */
    @Override
    public EContractRecord setSubject(String value) {
        set(9, value);
        return this;
    }

    /**
     * Getter for <code>DB_HOTEL.E_CONTRACT.SUBJECT</code>. 「subject」- 合同目标
     */
    @Override
    public String getSubject() {
        return (String) get(9);
    }

    /**
     * Setter for <code>DB_HOTEL.E_CONTRACT.DESCRIPTION</code>. 「description」-
     * 合同描述
     */
    @Override
    public EContractRecord setDescription(String value) {
        set(10, value);
        return this;
    }

    /**
     * Getter for <code>DB_HOTEL.E_CONTRACT.DESCRIPTION</code>. 「description」-
     * 合同描述
     */
    @Override
    public String getDescription() {
        return (String) get(10);
    }

    /**
     * Setter for <code>DB_HOTEL.E_CONTRACT.AMOUNT</code>. 「amount」- 合同金额
     */
    @Override
    public EContractRecord setAmount(BigDecimal value) {
        set(11, value);
        return this;
    }

    /**
     * Getter for <code>DB_HOTEL.E_CONTRACT.AMOUNT</code>. 「amount」- 合同金额
     */
    @Override
    public BigDecimal getAmount() {
        return (BigDecimal) get(11);
    }

    /**
     * Setter for <code>DB_HOTEL.E_CONTRACT.REMARK</code>. 「remark」- 合同备注
     */
    @Override
    public EContractRecord setRemark(String value) {
        set(12, value);
        return this;
    }

    /**
     * Getter for <code>DB_HOTEL.E_CONTRACT.REMARK</code>. 「remark」- 合同备注
     */
    @Override
    public String getRemark() {
        return (String) get(12);
    }

    /**
     * Setter for <code>DB_HOTEL.E_CONTRACT.PLAN_START_AT</code>. 「planStartAt」-
     * 开始日期
     */
    @Override
    public EContractRecord setPlanStartAt(LocalDateTime value) {
        set(13, value);
        return this;
    }

    /**
     * Getter for <code>DB_HOTEL.E_CONTRACT.PLAN_START_AT</code>. 「planStartAt」-
     * 开始日期
     */
    @Override
    public LocalDateTime getPlanStartAt() {
        return (LocalDateTime) get(13);
    }

    /**
     * Setter for <code>DB_HOTEL.E_CONTRACT.PLAN_END_AT</code>. 「planEndAt」-
     * 结束日期
     */
    @Override
    public EContractRecord setPlanEndAt(LocalDateTime value) {
        set(14, value);
        return this;
    }

    /**
     * Getter for <code>DB_HOTEL.E_CONTRACT.PLAN_END_AT</code>. 「planEndAt」-
     * 结束日期
     */
    @Override
    public LocalDateTime getPlanEndAt() {
        return (LocalDateTime) get(14);
    }

    /**
     * Setter for <code>DB_HOTEL.E_CONTRACT.START_AT</code>. 「startAt」- 实际开始日期
     */
    @Override
    public EContractRecord setStartAt(LocalDateTime value) {
        set(15, value);
        return this;
    }

    /**
     * Getter for <code>DB_HOTEL.E_CONTRACT.START_AT</code>. 「startAt」- 实际开始日期
     */
    @Override
    public LocalDateTime getStartAt() {
        return (LocalDateTime) get(15);
    }

    /**
     * Setter for <code>DB_HOTEL.E_CONTRACT.END_AT</code>. 「endAt」- 实际结束日期
     */
    @Override
    public EContractRecord setEndAt(LocalDateTime value) {
        set(16, value);
        return this;
    }

    /**
     * Getter for <code>DB_HOTEL.E_CONTRACT.END_AT</code>. 「endAt」- 实际结束日期
     */
    @Override
    public LocalDateTime getEndAt() {
        return (LocalDateTime) get(16);
    }

    /**
     * Setter for <code>DB_HOTEL.E_CONTRACT.SIGNED_BY</code>. 「signedBy」- 签订人
     */
    @Override
    public EContractRecord setSignedBy(String value) {
        set(17, value);
        return this;
    }

    /**
     * Getter for <code>DB_HOTEL.E_CONTRACT.SIGNED_BY</code>. 「signedBy」- 签订人
     */
    @Override
    public String getSignedBy() {
        return (String) get(17);
    }

    /**
     * Setter for <code>DB_HOTEL.E_CONTRACT.SIGNED_AT</code>. 「signedAt」- 签订时间
     */
    @Override
    public EContractRecord setSignedAt(LocalDateTime value) {
        set(18, value);
        return this;
    }

    /**
     * Getter for <code>DB_HOTEL.E_CONTRACT.SIGNED_AT</code>. 「signedAt」- 签订时间
     */
    @Override
    public LocalDateTime getSignedAt() {
        return (LocalDateTime) get(18);
    }

    /**
     * Setter for <code>DB_HOTEL.E_CONTRACT.PARTY_A</code>. 「partyA」-
     * 甲方（关联公司ID，E_COMPANY）
     */
    @Override
    public EContractRecord setPartyA(String value) {
        set(19, value);
        return this;
    }

    /**
     * Getter for <code>DB_HOTEL.E_CONTRACT.PARTY_A</code>. 「partyA」-
     * 甲方（关联公司ID，E_COMPANY）
     */
    @Override
    public String getPartyA() {
        return (String) get(19);
    }

    /**
     * Setter for <code>DB_HOTEL.E_CONTRACT.PARTY_B</code>. 「partyB」-
     * 乙方（关联客户ID，E_CUSTOMER）
     */
    @Override
    public EContractRecord setPartyB(String value) {
        set(20, value);
        return this;
    }

    /**
     * Getter for <code>DB_HOTEL.E_CONTRACT.PARTY_B</code>. 「partyB」-
     * 乙方（关联客户ID，E_CUSTOMER）
     */
    @Override
    public String getPartyB() {
        return (String) get(20);
    }

    /**
     * Setter for <code>DB_HOTEL.E_CONTRACT.A_NAME</code>. 「aName」-
     * 甲方名称（个人为姓名/企业为企业名）
     */
    @Override
    public EContractRecord setAName(String value) {
        set(21, value);
        return this;
    }

    /**
     * Getter for <code>DB_HOTEL.E_CONTRACT.A_NAME</code>. 「aName」-
     * 甲方名称（个人为姓名/企业为企业名）
     */
    @Override
    public String getAName() {
        return (String) get(21);
    }

    /**
     * Setter for <code>DB_HOTEL.E_CONTRACT.A_PHONE</code>. 「aPhone」- 甲方电话
     */
    @Override
    public EContractRecord setAPhone(String value) {
        set(22, value);
        return this;
    }

    /**
     * Getter for <code>DB_HOTEL.E_CONTRACT.A_PHONE</code>. 「aPhone」- 甲方电话
     */
    @Override
    public String getAPhone() {
        return (String) get(22);
    }

    /**
     * Setter for <code>DB_HOTEL.E_CONTRACT.A_LEGAL</code>. 「aLegal」-
     * 甲方法人（企业合同专用）
     */
    @Override
    public EContractRecord setALegal(String value) {
        set(23, value);
        return this;
    }

    /**
     * Getter for <code>DB_HOTEL.E_CONTRACT.A_LEGAL</code>. 「aLegal」-
     * 甲方法人（企业合同专用）
     */
    @Override
    public String getALegal() {
        return (String) get(23);
    }

    /**
     * Setter for <code>DB_HOTEL.E_CONTRACT.A_ADDRESS</code>. 「aAddress」- 甲方地址
     */
    @Override
    public EContractRecord setAAddress(String value) {
        set(24, value);
        return this;
    }

    /**
     * Getter for <code>DB_HOTEL.E_CONTRACT.A_ADDRESS</code>. 「aAddress」- 甲方地址
     */
    @Override
    public String getAAddress() {
        return (String) get(24);
    }

    /**
     * Setter for <code>DB_HOTEL.E_CONTRACT.B_NAME</code>. 「bName」-
     * 乙方名称（个人为姓名/企业为企业名）
     */
    @Override
    public EContractRecord setBName(String value) {
        set(25, value);
        return this;
    }

    /**
     * Getter for <code>DB_HOTEL.E_CONTRACT.B_NAME</code>. 「bName」-
     * 乙方名称（个人为姓名/企业为企业名）
     */
    @Override
    public String getBName() {
        return (String) get(25);
    }

    /**
     * Setter for <code>DB_HOTEL.E_CONTRACT.B_PHONE</code>. 「bPhone」- 乙方人电话
     */
    @Override
    public EContractRecord setBPhone(String value) {
        set(26, value);
        return this;
    }

    /**
     * Getter for <code>DB_HOTEL.E_CONTRACT.B_PHONE</code>. 「bPhone」- 乙方人电话
     */
    @Override
    public String getBPhone() {
        return (String) get(26);
    }

    /**
     * Setter for <code>DB_HOTEL.E_CONTRACT.B_LEGAL</code>. 「bLegal」-
     * 乙方法人（企业合同专用）
     */
    @Override
    public EContractRecord setBLegal(String value) {
        set(27, value);
        return this;
    }

    /**
     * Getter for <code>DB_HOTEL.E_CONTRACT.B_LEGAL</code>. 「bLegal」-
     * 乙方法人（企业合同专用）
     */
    @Override
    public String getBLegal() {
        return (String) get(27);
    }

    /**
     * Setter for <code>DB_HOTEL.E_CONTRACT.B_ADDRESS</code>. 「bAddress」- 乙方地址
     */
    @Override
    public EContractRecord setBAddress(String value) {
        set(28, value);
        return this;
    }

    /**
     * Getter for <code>DB_HOTEL.E_CONTRACT.B_ADDRESS</code>. 「bAddress」- 乙方地址
     */
    @Override
    public String getBAddress() {
        return (String) get(28);
    }

    /**
     * Setter for <code>DB_HOTEL.E_CONTRACT.METADATA</code>. 「metadata」- 附加配置
     */
    @Override
    public EContractRecord setMetadata(String value) {
        set(29, value);
        return this;
    }

    /**
     * Getter for <code>DB_HOTEL.E_CONTRACT.METADATA</code>. 「metadata」- 附加配置
     */
    @Override
    public String getMetadata() {
        return (String) get(29);
    }

    /**
     * Setter for <code>DB_HOTEL.E_CONTRACT.ACTIVE</code>. 「active」- 是否启用
     */
    @Override
    public EContractRecord setActive(Boolean value) {
        set(30, value);
        return this;
    }

    /**
     * Getter for <code>DB_HOTEL.E_CONTRACT.ACTIVE</code>. 「active」- 是否启用
     */
    @Override
    public Boolean getActive() {
        return (Boolean) get(30);
    }

    /**
     * Setter for <code>DB_HOTEL.E_CONTRACT.SIGMA</code>. 「sigma」- 统一标识（公司所属应用）
     */
    @Override
    public EContractRecord setSigma(String value) {
        set(31, value);
        return this;
    }

    /**
     * Getter for <code>DB_HOTEL.E_CONTRACT.SIGMA</code>. 「sigma」- 统一标识（公司所属应用）
     */
    @Override
    public String getSigma() {
        return (String) get(31);
    }

    /**
     * Setter for <code>DB_HOTEL.E_CONTRACT.LANGUAGE</code>. 「language」- 使用的语言
     */
    @Override
    public EContractRecord setLanguage(String value) {
        set(32, value);
        return this;
    }

    /**
     * Getter for <code>DB_HOTEL.E_CONTRACT.LANGUAGE</code>. 「language」- 使用的语言
     */
    @Override
    public String getLanguage() {
        return (String) get(32);
    }

    /**
     * Setter for <code>DB_HOTEL.E_CONTRACT.CREATED_AT</code>. 「createdAt」- 创建时间
     */
    @Override
    public EContractRecord setCreatedAt(LocalDateTime value) {
        set(33, value);
        return this;
    }

    /**
     * Getter for <code>DB_HOTEL.E_CONTRACT.CREATED_AT</code>. 「createdAt」- 创建时间
     */
    @Override
    public LocalDateTime getCreatedAt() {
        return (LocalDateTime) get(33);
    }

    /**
     * Setter for <code>DB_HOTEL.E_CONTRACT.CREATED_BY</code>. 「createdBy」- 创建人
     */
    @Override
    public EContractRecord setCreatedBy(String value) {
        set(34, value);
        return this;
    }

    /**
     * Getter for <code>DB_HOTEL.E_CONTRACT.CREATED_BY</code>. 「createdBy」- 创建人
     */
    @Override
    public String getCreatedBy() {
        return (String) get(34);
    }

    /**
     * Setter for <code>DB_HOTEL.E_CONTRACT.UPDATED_AT</code>. 「updatedAt」- 更新时间
     */
    @Override
    public EContractRecord setUpdatedAt(LocalDateTime value) {
        set(35, value);
        return this;
    }

    /**
     * Getter for <code>DB_HOTEL.E_CONTRACT.UPDATED_AT</code>. 「updatedAt」- 更新时间
     */
    @Override
    public LocalDateTime getUpdatedAt() {
        return (LocalDateTime) get(35);
    }

    /**
     * Setter for <code>DB_HOTEL.E_CONTRACT.UPDATED_BY</code>. 「updatedBy」- 更新人
     */
    @Override
    public EContractRecord setUpdatedBy(String value) {
        set(36, value);
        return this;
    }

    /**
     * Getter for <code>DB_HOTEL.E_CONTRACT.UPDATED_BY</code>. 「updatedBy」- 更新人
     */
    @Override
    public String getUpdatedBy() {
        return (String) get(36);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<String> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    @Override
    public void from(IEContract from) {
        setKey(from.getKey());
        setName(from.getName());
        setCode(from.getCode());
        setDeptId(from.getDeptId());
        setType(from.getType());
        setStatus(from.getStatus());
        setBudget(from.getBudget());
        setLevel(from.getLevel());
        setTitle(from.getTitle());
        setSubject(from.getSubject());
        setDescription(from.getDescription());
        setAmount(from.getAmount());
        setRemark(from.getRemark());
        setPlanStartAt(from.getPlanStartAt());
        setPlanEndAt(from.getPlanEndAt());
        setStartAt(from.getStartAt());
        setEndAt(from.getEndAt());
        setSignedBy(from.getSignedBy());
        setSignedAt(from.getSignedAt());
        setPartyA(from.getPartyA());
        setPartyB(from.getPartyB());
        setAName(from.getAName());
        setAPhone(from.getAPhone());
        setALegal(from.getALegal());
        setAAddress(from.getAAddress());
        setBName(from.getBName());
        setBPhone(from.getBPhone());
        setBLegal(from.getBLegal());
        setBAddress(from.getBAddress());
        setMetadata(from.getMetadata());
        setActive(from.getActive());
        setSigma(from.getSigma());
        setLanguage(from.getLanguage());
        setCreatedAt(from.getCreatedAt());
        setCreatedBy(from.getCreatedBy());
        setUpdatedAt(from.getUpdatedAt());
        setUpdatedBy(from.getUpdatedBy());
    }

    @Override
    public <E extends IEContract> E into(E into) {
        into.from(this);
        return into;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached EContractRecord
     */
    public EContractRecord() {
        super(EContract.E_CONTRACT);
    }

    /**
     * Create a detached, initialised EContractRecord
     */
    public EContractRecord(String key, String name, String code, String deptId, String type, String status, String budget, String level, String title, String subject, String description, BigDecimal amount, String remark, LocalDateTime planStartAt, LocalDateTime planEndAt, LocalDateTime startAt, LocalDateTime endAt, String signedBy, LocalDateTime signedAt, String partyA, String partyB, String aName, String aPhone, String aLegal, String aAddress, String bName, String bPhone, String bLegal, String bAddress, String metadata, Boolean active, String sigma, String language, LocalDateTime createdAt, String createdBy, LocalDateTime updatedAt, String updatedBy) {
        super(EContract.E_CONTRACT);

        setKey(key);
        setName(name);
        setCode(code);
        setDeptId(deptId);
        setType(type);
        setStatus(status);
        setBudget(budget);
        setLevel(level);
        setTitle(title);
        setSubject(subject);
        setDescription(description);
        setAmount(amount);
        setRemark(remark);
        setPlanStartAt(planStartAt);
        setPlanEndAt(planEndAt);
        setStartAt(startAt);
        setEndAt(endAt);
        setSignedBy(signedBy);
        setSignedAt(signedAt);
        setPartyA(partyA);
        setPartyB(partyB);
        setAName(aName);
        setAPhone(aPhone);
        setALegal(aLegal);
        setAAddress(aAddress);
        setBName(bName);
        setBPhone(bPhone);
        setBLegal(bLegal);
        setBAddress(bAddress);
        setMetadata(metadata);
        setActive(active);
        setSigma(sigma);
        setLanguage(language);
        setCreatedAt(createdAt);
        setCreatedBy(createdBy);
        setUpdatedAt(updatedAt);
        setUpdatedBy(updatedBy);
    }

    /**
     * Create a detached, initialised EContractRecord
     */
    public EContractRecord(cn.vertxup.erp.domain.tables.pojos.EContract value) {
        super(EContract.E_CONTRACT);

        if (value != null) {
            setKey(value.getKey());
            setName(value.getName());
            setCode(value.getCode());
            setDeptId(value.getDeptId());
            setType(value.getType());
            setStatus(value.getStatus());
            setBudget(value.getBudget());
            setLevel(value.getLevel());
            setTitle(value.getTitle());
            setSubject(value.getSubject());
            setDescription(value.getDescription());
            setAmount(value.getAmount());
            setRemark(value.getRemark());
            setPlanStartAt(value.getPlanStartAt());
            setPlanEndAt(value.getPlanEndAt());
            setStartAt(value.getStartAt());
            setEndAt(value.getEndAt());
            setSignedBy(value.getSignedBy());
            setSignedAt(value.getSignedAt());
            setPartyA(value.getPartyA());
            setPartyB(value.getPartyB());
            setAName(value.getAName());
            setAPhone(value.getAPhone());
            setALegal(value.getALegal());
            setAAddress(value.getAAddress());
            setBName(value.getBName());
            setBPhone(value.getBPhone());
            setBLegal(value.getBLegal());
            setBAddress(value.getBAddress());
            setMetadata(value.getMetadata());
            setActive(value.getActive());
            setSigma(value.getSigma());
            setLanguage(value.getLanguage());
            setCreatedAt(value.getCreatedAt());
            setCreatedBy(value.getCreatedBy());
            setUpdatedAt(value.getUpdatedAt());
            setUpdatedBy(value.getUpdatedBy());
        }
    }

        public EContractRecord(io.vertx.core.json.JsonObject json) {
                this();
                fromJson(json);
        }
}
