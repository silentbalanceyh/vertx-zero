package io.vertx.mod.atom.modeling;

import cn.vertxup.atom.domain.tables.pojos.MEntity;
import cn.vertxup.atom.domain.tables.pojos.MField;
import cn.vertxup.atom.domain.tables.pojos.MKey;
import io.modello.specification.action.HLinkage;
import io.modello.specification.atom.HLife;

import java.util.List;
import java.util.Set;

/**
 * 数据库专用Schema相关信息，用于整合
 * Entity, Field, PtField，Index四种类型
 * Issuer可以通过Schema创建完整表结构
 */
public interface Schema extends HLife, HLinkage {
    /* 所有字段名字集合 */
    Set<String> getFieldNames();

    /* 当前Entity实体对应的表信息 **/
    String getTable();

    /* 根据列名读取Field **/
    MField getFieldByColumn(String column);

    /* 根据名称读取Field **/
    MField getField(String field);

    /* 所有列集合 */
    Set<String> getColumnNames();

    /* 读取主键字段信息，关联表使用的是双主键 */
    List<MField> getPrimaryKeys();

    /* 读取键集合 */
    MKey[] getKeys();

    /* 读取主实体信息 */
    MEntity getEntity();

    /* 读取字段信息 */
    MField[] getFields();
}

