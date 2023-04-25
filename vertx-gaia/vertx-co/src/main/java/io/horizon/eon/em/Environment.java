package io.horizon.eon.em;

/**
 * # 「Co」Environment for different roles
 *
 * 1. Production: Prod Environment for deployment
 * 2. Development: Dev Environment for development;
 * 3. Mockito: Mockito Environment for `integration` debug to mock environment
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public enum Environment {
    Production,
    Development,
    Mockito,
}
