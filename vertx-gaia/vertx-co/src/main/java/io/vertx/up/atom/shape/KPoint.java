package io.vertx.up.atom.shape;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ClassDeserializer;
import com.fasterxml.jackson.databind.ClassSerializer;
import com.fasterxml.jackson.databind.JsonObjectDeserializer;
import com.fasterxml.jackson.databind.JsonObjectSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.modello.eon.em.EmModel;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.exchange.BMapping;
import io.vertx.up.exception.web._409JoinTargetException;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.Objects;

/**
 * ## 「Pojo」Source/Target
 *
 * ### 1. Intro
 *
 * Here defined the field for join point such as `source` and `target`.
 *
 * ### 2. Attribute
 *
 * |Name|Comment|
 * |---|:---|
 * |key|The default value is `key`, primary key field.|
 * |keyJoin|Required, the join key field of the record.|
 * |crud|Optional, When it has value, you can search `identifier`( targetKey ) by CRUD.|
 * |classDao|Optional, When it has value, you can join with the Dao class instead of CRUD seeking.|
 * |classDefine|Optional, When it has value, you can seek target by defined component.|
 *
 * ### 3. Data Format
 *
 * ```json
 * // <pre><code class="json">
 *     {
 *         "crud": "xxx",
 *         "classDao": "xxx",
 *         "keyJoin": "fieldx",
 *         "key": "key"
 *     }
 * // </code></pre>
 * ```
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class KPoint implements Serializable {
    /**
     * `identifier`
     */
    @JsonIgnore
    private String identifier;
    /**
     * `crud`, <strong>filename</strong> that could be parsed.
     */
    private String crud;
    /**
     * `classDao`, <strong>Dao class</strong> that could be convert to java class.
     */
    @JsonSerialize(using = ClassSerializer.class)
    @JsonDeserialize(using = ClassDeserializer.class)
    private Class<?> classDao;


    /**
     * `classDefine`, <strong>Defined class</strong> that could be convert to java class.
     */
    @JsonSerialize(using = ClassSerializer.class)
    @JsonDeserialize(using = ClassDeserializer.class)
    private Class<?> classDefine;
    /**
     * `key`, primary key field.
     */
    private String key;
    /**
     * `keyJoin`, join key that are related to join point.
     */
    private String keyJoin;

    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private JsonObject synonym;

    public String getCrud() {
        return this.crud;
    }

    public void setCrud(final String crud) {
        this.crud = crud;
    }

    public String getKey() {
        return Ut.isNil(this.key) ? "key" : this.key;
    }

    public void setKey(final String key) {
        if (Ut.isNotNil(key)) {
            this.key = key;
        }
    }

    public String getKeyJoin() {
        return this.keyJoin;
    }

    public void setKeyJoin(final String keyJoin) {
        this.keyJoin = keyJoin;
    }

    public Class<?> getClassDao() {
        return this.classDao;
    }

    public void setClassDao(final Class<?> classDao) {
        this.classDao = classDao;
    }

    public Class<?> getClassDefine() {
        return this.classDefine;
    }

    public void setClassDefine(final Class<?> classDefine) {
        this.classDefine = classDefine;
    }

    public JsonObject getSynonym() {
        return this.synonym;
    }

    public void setSynonym(final JsonObject synonym) {
        this.synonym = synonym;
    }

    public BMapping synonym() {
        return new BMapping(this.synonym);
    }

    /**
     * Get target configuration mode here for calculation.
     *
     * @return {@link EmModel.Join}
     */
    public EmModel.Join modeTarget() {
        /* P1: CRUD */
        if (Ut.isNotNil(this.crud)) {
            return EmModel.Join.CRUD;
        }
        /* P2: classDao */
        if (Objects.nonNull(this.classDao)) {
            return EmModel.Join.DAO;
        }
        /* P3: classDefine also null, throw error out. */
        Fn.out(Objects.isNull(this.classDefine), _409JoinTargetException.class, this.getClass());
        return EmModel.Join.DEFINE;
    }

    /**
     * Get source configuration mode here for calculation.
     *
     * @return {@link EmModel.Join}
     */
    public EmModel.Join modeSource() {
        /* P1: classDao */
        if (Objects.nonNull(this.classDao)) {
            return EmModel.Join.DAO;
        }
        /* P2: classDefine */
        if (Objects.nonNull(this.classDefine)) {
            return EmModel.Join.DEFINE;
        }
        /* P3: keyJoin */
        Fn.out(Ut.isNil(this.keyJoin), _409JoinTargetException.class, this.getClass());
        return EmModel.Join.CRUD;
    }

    public KPoint indent(final String identifier) {
        this.identifier = identifier;
        if (Objects.isNull(this.crud)) {
            // Default Applying
            this.crud = identifier;
        }
        return this;
    }

    public String indent() {
        return this.identifier;
    }

    @Override
    public String toString() {
        return "KPoint{" +
            "identifier='" + this.identifier + '\'' +
            ", crud='" + this.crud + '\'' +
            ", classDao=" + this.classDao +
            ", classDefine=" + this.classDefine +
            ", key='" + this.key + '\'' +
            ", keyJoin='" + this.keyJoin + '\'' +
            ", synonym=" + this.synonym +
            '}';
    }
}
