package io.mature.extension.uca.ui;

/**
 * ## 字段状态
 *
 * ### 1. 基本介绍
 *
 * 枚举常量，用于判断字段比对的最终结果。
 *
 * - MATCH：界面配置和模型定义匹配（都包含并且合法）。
 * - INVALID：模型中有定义，但属性定义有错。
 * - REMAIN：`LIST/FORM`中有定义，但模型中丢失。
 *
 * ### 2. 比对矩阵
 *
 * |比对结果|UI配置|模型定义|
 * |:---|---|---|
 * |MATCH|Ok|Ok|
 * |INVALID|有定义|x|
 * |REMAIN|有定义|--|
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public enum FieldStatus {
    MATCH,      // 匹配
    INVALID,    // 模型中有定义，但出现了错误
    REMAIN,     // Form / List 中定义，但模型中没有
}
