package io.zero.cv;

/**
 * V 前缀代表 Value，字面量 / 值量
 *
 * @author lang : 2023/4/24
 */
public interface VString {
    // ================== 货币区域
    /**
     * 美元符号
     */
    String DOLLAR = "$";
    /**
     * 人民币
     */
    String RMB = "￥";


    // ================== 符号区域
    /**
     * 英文逗号
     */
    String COMMA = ",";
    /**
     * 英文句号 / 点
     */
    String DOT = ".";
    /**
     * 空字符串
     */
    String EMPTY = "";
    /**
     * 空白字符串
     */
    String SPACE = " ";
    /**
     * 冒号
     */
    String COLON = ":";
    /**
     * 问号
     */
    String QUESTION = "?";
    /**
     * 等号
     */
    String EQUAL = "=";
    /**
     * 换行符
     */
    String NEW_LINE = "\n";
    /**
     * 制表符
     */
    String TAB = "\t";
    /**
     * 撇号
     */
    String ACCENT_SIGN = "`";
    /**
     * 双引号
     */
    String QUOTE_DOUBLE = "\"";
    /**
     * 单引号
     */
    String QUOTE = "'";
    /**
     * 感叹号
     */
    String EXCLAMATION = "!";


    // ================== 路径区
    /**
     * 斜杠
     */
    String SLASH = "/";
    /**
     * 反斜杠
     */
    String SLASH_BACK = "\\";


    // ================== 划线区
    /**
     * 中划线
     */
    String DASH = "-";
    /**
     * 下划线
     */
    String UNDERLINE = "_";


    // ================== 括号区
    /**
     * 左大括号
     */
    String LEFT_BRACE = "{";
    /**
     * 右大括号
     */
    String RIGHT_BRACE = "}";
    /**
     * 左小括号
     */
    String LEFT_BRACKET = "(";
    /**
     * 右小括号
     */
    String RIGHT_BRACKET = ")";
    /**
     * 左中括号
     */
    String LEFT_SQUARE = "[";
    /**
     * 右中括号
     */
    String RIGHT_SQUARE = "]";


    // ================= 特殊区域
    /**
     * 往右箭头
     */
    String ARROW_RIGHT = "->";
    /**
     * 往左箭头
     */
    String ARROW_LEFT = "<-";
}
