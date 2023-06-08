package io.horizon.eon.em.app;

/**
 * @author lang : 2023-05-20
 */
public enum SecurityLevel {
    // 应用级
    Application(0B0001),
    // 管理级
    Admin(0B0010),
    // 开发级，建模管理，云端部署
    Development(0B0100),
    // 超级账号
    Supervisor(0B1000);

    private final int code;

    SecurityLevel(final int code) {
        this.code = code;
    }

    public int code() {
        return this.code;
    }
}
