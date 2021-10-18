package io.vertx.tp.ke.atom;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * # Linker data for following
 *
 * 1. System Field Information:
 * - createdBy
 * - createdAt
 * - updatedBy
 * - updatedAt
 * - language
 * - sigma
 * - active
 *
 * 2. System Secondary Information:
 * - key        : System Primary Key
 * - code       : System Code
 * - name       : System Name
 * - type       : System Type
 * - category   : System Category
 * - serial     : System Number & Serialization
 *
 * 3. System Linking Information:
 * - modelCategory  : Model Category
 * - modelId        : Model Id
 * - modelKey       : Model Key
 * - reference      : Reference Id
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class KSpec implements Serializable {
    // System Business Field
    private transient String key;
    private transient String code;
    private transient String name;
    private transient String type;
    private transient String category;
    private transient String serial;
    // System Default Field
    private transient String language;
    private transient String sigma;
    private transient Boolean active;
    private transient String createdBy;
    private transient LocalDateTime createdAt;
    private transient String updatedBy;
    private transient LocalDateTime updatedAt;
    // Linking Field
    private transient String reference;
}
