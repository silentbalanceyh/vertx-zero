package io.vertx.up.eon;

/**
 * Default order for manage standard request flow
 */
public interface Orders {
    /**
     * Monitor Order
     * 1,000,000
     */
    int MONITOR = 1_000_000;
    /*
     * Time Out
     * 1,050,000
     */
    int TIMEOUT = 1_050_000;
    /**
     * Cors Order
     * 1,100,000
     **/
    int CORS = 1_100_000;
    /**
     * Cookie Order
     * 1,200,000
     */
    int COOKIE = 1_200_000;
    /**
     * Body Order
     * 1,300,000
     */
    int BODY = 1_300_000;
    /**
     * Pattern
     * 1,400,000
     */
    int CONTENT = 1_400_000;
    /**
     * Secure
     * 1,600,000
     */
    int SESSION = 1_600_000;
    /**
     * Filter for request
     * 1,800,000
     */
    int FILTER = 1_800_000;
    /**
     * User Security
     * 1,900,000
     */
    int SECURE = 1_900_000;
    /**
     * User Authorization
     * 2,000,000
     */
    int SECURE_AUTHORIZATION = 2_000_000;
    /**
     * Sign for request ( Sign Request )
     * 3,000,000
     */
    int SIGN = 3_000_000;
    /**
     * ( Default order for event )
     * 5,000,000
     */
    int EVENT = 5_000_000;
    /**
     * ( Default for dynamic routing )
     * 6,000,000
     */
    int DYNAMIC = 6_000_000;
    /**
     * ( Default for Module such as CRUD )
     * 10,000,000
     */
    int MODULE = 10_000_000;
}
