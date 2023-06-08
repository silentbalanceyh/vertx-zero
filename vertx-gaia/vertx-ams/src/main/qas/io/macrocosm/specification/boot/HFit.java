package io.macrocosm.specification.boot;

/**
 * 「装配器」专用于配置读写
 * 1. 目录固定（每个目录固定一个HFit组件）
 * 2. 每个组件搭载
 * -- IO缓存（分布式）
 * -- 数据缓存（分布式）
 * -- Git区域（分布式）
 * 3. 提供基础API方法
 * -- 配置读取
 * ---- 单配置
 * ---- 多配置
 * ---- 分页/分组
 * -- 配置写入
 * ---- 单配置
 * ---- 查找型单配置
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface HFit {

}
