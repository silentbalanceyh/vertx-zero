package io.horizon.eon;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author lang : 2023/4/24
 */
public interface VValue {
    /** 默认值 */
    String DEFAULT = "DEFAULT";

    // ================= 数值常量
    // --- Number
    // -1
    int RANGE = -1;             /* 越界 */
    int UNSET = RANGE;          /* 未设置 */
    int CODECS = RANGE;         /* 规则器未设置 */
    // 0
    int ZERO = 0;               /* 0 */
    int IDX = ZERO;             /* 索引默认 */
    // 1
    int ONE = 1;                /* 1 */
    int IDX_1 = ONE;            /* 索引值1 */
    int SINGLE = ONE;           /* 单一记录 */
    // 2
    int TWO = 2;                /* 2 */
    int IDX_2 = TWO;            /* 索引值2 */
    // 3, 4, etc
    int THREE = 3;
    int FOUR = 4;
    // RC_
    int RC_SUCCESS = ZERO;      /* 成功 */
    int RC_FAILURE = RANGE;     /* 失败 */


    // --- Boolean
    String TRUE = "true";
    String FALSE = "false";

    // --- Array
    // Hex
    char[] ARR_HEX = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    // ================= 默认值清单

    /**
     * 默认值清单
     * 1 - 不带前缀为系统级默认值
     * 2 - V_ 为视图专用常量
     * 3 - M_ 为建模专用常量
     */
    interface DFT {
        /** 默认 byte[] 数组的构造尺寸 8192 字节 */
        int SIZE_BYTE_ARRAY = 8 * 1024;
        /** 系统默认编码方式 */
        Charset CHARSET = StandardCharsets.UTF_8;

        String ALGORITHM_RSA = "RSA";
        // ================= 视图专用常量
        /**
         * 默认视图名称 / 视图位置
         * V_ 前缀
         */
        String V_VIEW = VValue.DEFAULT;
        String V_POSITION = VValue.DEFAULT;

        String M_IDENTIFIER_NULL = "(`identifier` null)";
    }
}
