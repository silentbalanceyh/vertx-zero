package io.vertx.mod.ambient.atom;

import com.fasterxml.jackson.databind.ClassDeserializer;
import com.fasterxml.jackson.databind.ClassSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.horizon.spi.extension.Init;
import io.horizon.spi.extension.Prerequisite;

import java.io.Serializable;

/**
 * ## 「Pojo」Configuration Object
 *
 * ### 1. Intro
 *
 * This class is for serialization/deserialization that mapped to Ambient configuration data.
 *
 * ### 2. Extension
 *
 * Here are three critical components that could be extend in zero extension modules.
 *
 * |Name|Interface|Comments|
 * |---|---:|:---|
 * |prerequisite|{@link Prerequisite}|The first phase before all, assist for Front-End app.|
 * |initializer|{@link Init}|After `X_APP, X_SOURCE` processed.|
 * |loader|{@link Init}|After configuration data, this phase will be call for data loading.|
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AtConfig implements Serializable {
    /**
     * <value>FALSE</value>, Whether enable `X_SOURCE` to stored multi data sources in current `X_APP`.
     */
    private Boolean supportSource = Boolean.FALSE;

    /**
     * Initializer: This class will be inited as Init instance and executed
     * After: X_APP, X_SOURCE, Database, also it will be inited after re-init.
     */
    @JsonSerialize(using = ClassSerializer.class)
    @JsonDeserialize(using = ClassDeserializer.class)
    private Class<?> initializer;

    /**
     * It's for /api/app/prepare interface only, do pre-requisite for app initialization
     */
    @JsonSerialize(using = ClassSerializer.class)
    @JsonDeserialize(using = ClassDeserializer.class)
    private Class<?> prerequisite;

    /**
     * Initializer: Another initializer that will be executed after data loading
     * The last step of initApp.
     */
    @JsonSerialize(using = ClassSerializer.class)
    @JsonDeserialize(using = ClassDeserializer.class)
    private Class<?> loader;

    /**
     * Attachment configuration: storeWay property, fixed in current version.
     */
    private String fileStorage;
    /**
     * Attachment configuration: language property, fixed in current version.
     */
    private String fileLanguage;

    private String fileIntegration;
    /**
     * Standard data folder for data loading, Required for excel client, Loading data into database by excel client.
     */
    private String dataFolder;

    private String storePath;

    /**
     * Return to `fileStorage` attribute.
     *
     * @return {@link java.lang.String}
     */
    public String getFileStorage() {
        return this.fileStorage;
    }

    /**
     * @param fileStorage {@link java.lang.String} `fileStorage` attribute.
     */
    public void setFileStorage(final String fileStorage) {
        this.fileStorage = fileStorage;
    }

    /**
     * Return to `fileLanguage` attribute.
     *
     * @return {@link java.lang.String}
     */
    public String getFileLanguage() {
        return this.fileLanguage;
    }

    /**
     * @param fileLanguage {@link java.lang.String} `fileLanguage` attribute.
     */
    public void setFileLanguage(final String fileLanguage) {
        this.fileLanguage = fileLanguage;
    }

    public String getFileIntegration() {
        return fileIntegration;
    }

    public void setFileIntegration(String fileIntegration) {
        this.fileIntegration = fileIntegration;
    }

    /**
     * Return to `supportSource` attribute.
     *
     * @return {@link java.lang.Boolean}
     */
    public Boolean getSupportSource() {
        return this.supportSource;
    }

    /**
     * @param supportSource {@link java.lang.Boolean} `supportSource` attribute.
     */
    public void setSupportSource(final Boolean supportSource) {
        this.supportSource = supportSource;
    }

    /**
     * Return to `initializer` attribute, deserialized to Class.
     *
     * @return {@link java.lang.Class}
     */
    public Class<?> getInitializer() {
        return this.initializer;
    }

    /**
     * @param initializer {@link java.lang.Class} `initializer` attribute.
     */
    public void setInitializer(final Class<?> initializer) {
        this.initializer = initializer;
    }

    /**
     * Return to `prerequisite` attribute, deserialized to Class.
     *
     * @return {@link java.lang.Class}
     */
    public Class<?> getPrerequisite() {
        return this.prerequisite;
    }

    /**
     * @param prerequisite {@link java.lang.Class} `prerequisite` attribute.
     */
    public void setPrerequisite(final Class<?> prerequisite) {
        this.prerequisite = prerequisite;
    }

    /**
     * Return to `loader` attribute, deserialized to Class.
     *
     * @return {@link java.lang.Class}
     */
    public Class<?> getLoader() {
        return this.loader;
    }

    /**
     * @param loader {@link java.lang.Class} `loader` attribute.
     */
    public void setLoader(final Class<?> loader) {
        this.loader = loader;
    }

    /**
     * Return to `dataFolder` attribute.
     *
     * @return {@link java.lang.String}
     */
    public String getDataFolder() {
        return this.dataFolder;
    }

    /**
     * @param dataFolder {@link java.lang.String} `dataFolder` attribute.
     */
    public void setDataFolder(final String dataFolder) {
        this.dataFolder = dataFolder;
    }

    public String getStorePath() {
        return this.storePath;
    }

    public void setStorePath(final String storePath) {
        this.storePath = storePath;
    }

    @Override
    public String toString() {
        return "AtConfig{" +
            "supportSource=" + this.supportSource +
            ", initializer=" + this.initializer +
            ", prerequisite=" + this.prerequisite +
            ", loader=" + this.loader +
            ", fileStorage='" + this.fileStorage + '\'' +
            ", fileLanguage='" + this.fileLanguage + '\'' +
            ", fileIntegration='" + this.fileIntegration + '\'' +
            ", dataFolder='" + this.dataFolder + '\'' +
            ", storePath='" + this.storePath + '\'' +
            '}';
    }
}
