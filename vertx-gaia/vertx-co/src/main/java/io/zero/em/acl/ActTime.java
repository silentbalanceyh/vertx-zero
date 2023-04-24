package io.zero.em.acl;

/**
 * 数据域 DataRegion 的触发时间
 * - BEFORE: 在请求之前触发 DataRegion
 * - AFTER：在请求之后触发 DataRegion
 * - AROUND：在请求之前和之后都触发 DataRegion
 * - NONE：不触发，直接禁用，方便热部署时禁用
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public enum ActTime {
    BEFORE, // DataRegion before
    AFTER,  // DataRegion after
    AROUND,  // Around execution
    NONE,    // Disable dual mapping execution
}
