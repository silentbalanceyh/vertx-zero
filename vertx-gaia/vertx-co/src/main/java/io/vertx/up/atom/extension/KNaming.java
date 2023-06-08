package io.vertx.up.atom.extension;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * # Linker data for following
 * <pre><code>
 * 1. System Field Information:
 * - createdBy
 * - createdAt
 * - updatedBy
 * - updatedAt
 * - language
 * - sigma
 * - active
 * 2. System Secondary Information:
 * - key        : System Primary Key
 * - code       : System Code
 * - name       : System Name
 * - type       : System Type
 * - category   : System Category
 * - serial     : System Number & Serialization
 * 3. System Linking Information:
 * - modelCategory  : Model Category
 * - modelId        : Model Id
 * - modelKey       : Model Key
 * - reference      : Reference Id
 * </code></pre>
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class KNaming implements Serializable {
    // Qr Only
    private final Set<String> qrKeys = new HashSet<>();
    // System Business Field
    private String key;
    private String code;
    private String name;
    private String type;
    private String category;
    private String serial;
    // System Default Field
    private String language;
    private String sigma;
    private Boolean active;
    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;
    // Linking Field
    private String reference;
    private String identifier;
    private String modelKey;
    private String modelCategory;

    public String getKey() {
        return this.key;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(final String category) {
        this.category = category;
    }

    public String getSerial() {
        return this.serial;
    }

    public void setSerial(final String serial) {
        this.serial = serial;
    }

    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(final String language) {
        this.language = language;
    }

    public String getSigma() {
        return this.sigma;
    }

    public void setSigma(final String sigma) {
        this.sigma = sigma;
    }

    public Boolean getActive() {
        return this.active;
    }

    public void setActive(final Boolean active) {
        this.active = active;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(final String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(final LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public void setUpdatedBy(final String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(final LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getReference() {
        return this.reference;
    }

    public void setReference(final String reference) {
        this.reference = reference;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public void setIdentifier(final String identifier) {
        this.identifier = identifier;
    }

    public String getModelKey() {
        return this.modelKey;
    }

    public void setModelKey(final String modelKey) {
        this.modelKey = modelKey;
    }

    public Set<String> getQrKeys() {
        return this.qrKeys;
    }

    public void setQrKeys(final Set<String> qrKeys) {
        this.qrKeys.clear();
        this.qrKeys.addAll(qrKeys);
    }

    public boolean multiple() {
        return !this.qrKeys.isEmpty();
    }

    public String getModelCategory() {
        return this.modelCategory;
    }

    public void setModelCategory(final String modelCategory) {
        this.modelCategory = modelCategory;
    }

    @Override
    public String toString() {
        return "KSpec{" +
            "key='" + this.key + '\'' +
            ", code='" + this.code + '\'' +
            ", name='" + this.name + '\'' +
            ", type='" + this.type + '\'' +
            ", category='" + this.category + '\'' +
            ", serial='" + this.serial + '\'' +
            ", language='" + this.language + '\'' +
            ", sigma='" + this.sigma + '\'' +
            ", active=" + this.active +
            ", createdBy='" + this.createdBy + '\'' +
            ", createdAt=" + this.createdAt +
            ", updatedBy='" + this.updatedBy + '\'' +
            ", updatedAt=" + this.updatedAt +
            ", reference='" + this.reference + '\'' +
            ", identifier='" + this.identifier + '\'' +
            ", modelKey='" + this.modelKey + '\'' +
            ", modelCategory='" + this.modelCategory + '\'' +
            '}';
    }
}
