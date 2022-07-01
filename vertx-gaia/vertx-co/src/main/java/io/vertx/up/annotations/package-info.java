/**
 * = Zero专用注解包（Annotation）
 *
 * == 1.基础说明
 *
 * 此包中定义了Zero Framework支持的所有自定义注解，Zero中支持以下两种自定义注解：
 *
 * * io.vertx.up.annotations包中的标准自定义注解。
 * * javax.*包中的扩展自定义注解（主要扩展自JSR规范）。Zero Framework对JSR的支持规范如：
 * ** JSR311, RESTful Web Service
 * ** JSR340, Java Servlet ( Filter )
 * ** JSR299/JSR330, Injection Dependency
 * ** JSR303, Java Bean Validation
 *
 * [CAUTION]
 * ====
 * Zero Framework针对部分标准规范提供扩展，扩展的注解位于同名包下边，这种设计并不是为了混淆视听，相反是让开发人员方便在对应的包中直接引用该
 * 注解使得整体代码看起来更加协调。
 * ====
 *
 * == 2.标准Annotation列表
 *
 * [cols=2*,separator=|]
 * |===
 * | @Address | EventBus专用注解。
 * |===
 */
package io.vertx.up.annotations;