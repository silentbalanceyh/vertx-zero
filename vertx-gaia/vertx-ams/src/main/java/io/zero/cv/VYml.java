package io.zero.cv;

/**
 * 重新规划配置部分专用，针对文件部分形成梯度配置键值，完成类似 YAML 结构的配置文件
 * 集，并可以在 interface 之间形成继承模式，变量定义变得更加完善
 * 缩写含义：High Level Yaml Configuration
 * 配合 yml 文件中的信息，值采用小写，节点则采用大写
 *
 * @author lang : 2023/4/24
 */
public interface VYml {
    /*
     * zero:
     *   lime:
     */
    String zero = "zero";
}
