package io.vertx.mod.rbac.cv.em;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface PackType {
    enum HType {
        // H type
        IN,
        AND,
        OR,
        TREE,
        NONE,
    }

    enum VType {
        // V type
        LINEAR,
        MULTI,
        NONE,
    }

    enum QType {
        // Q type
        FRONT,
        BACK,
        NONE
    }
}
