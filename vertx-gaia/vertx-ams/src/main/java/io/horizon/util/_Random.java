package io.horizon.util;

/**
 * @author lang : 2023/4/28
 */
class _Random extends _Parse {
    /**
     * 按长度生成随机整数
     *
     * @param length 长度
     *
     * @return 随机整数
     */
    public static Integer randomNumber(final int length) {
        return HRandom.randomNumber(length);
    }

    /**
     * 按长度生成随机字符串
     *
     * @param length 长度
     *
     * @return 随机字符串
     */
    public static String randomString(final int length) {
        return HRandom.randomString(length);
    }

    /**
     * 按长度生成随机验证码，验证码针对视觉层面比如大写 I 和小写 l 之类的问题做了处理
     *
     * @param length 长度
     *
     * @return 随机验证码
     */
    public static String randomCaptcha(final int length) {
        return HRandom.randomCaptcha(length);
    }

    /**
     * 生成随机字母
     *
     * @param length 长度
     *
     * @return 随机字母
     */
    public static String randomLetter(final int length) {
        return HRandom.randomLetter(length);
    }
}
