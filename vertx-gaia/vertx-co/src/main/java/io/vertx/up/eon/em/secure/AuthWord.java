package io.vertx.up.eon.em.secure;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public enum AuthWord {
    AND, // Perm1 + Perm2 + Perm3
    OR,  // Perm1,  Perm2,  Perm3
}
