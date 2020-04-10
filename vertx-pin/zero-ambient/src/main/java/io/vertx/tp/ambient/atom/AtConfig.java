package io.vertx.tp.ambient.atom;

import com.fasterxml.jackson.databind.ClassDeserializer;
import com.fasterxml.jackson.databind.ClassSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

/*
 * Application configuration data
 *
 */
public class AtConfig implements Serializable {
    /*
     * Whether enable XSource to stored multi data source in current XApp.
     */
    private Boolean supportSource = Boolean.FALSE;
    /*
     * Initializer: This class will be inited as Init instance and executed
     * After: X_APP, X_SOURCE, Database, also it will be inited after re-init.
     */
    @JsonSerialize(using = ClassSerializer.class)
    @JsonDeserialize(using = ClassDeserializer.class)
    private Class<?> initializer;

    /*
     * It's for /api/app/prepare interface only, do pre-requisite for app initialization
     */
    @JsonSerialize(using = ClassSerializer.class)
    @JsonDeserialize(using = ClassDeserializer.class)
    private Class<?> prerequisite;

    /*
     * Initializer: Another initializer that will be executed after data loading
     * The last step of initApp.
     */
    @JsonSerialize(using = ClassSerializer.class)
    @JsonDeserialize(using = ClassDeserializer.class)
    private Class<?> loader;

    /*
     * Attachment configuration: storeWay / language property.
     * reserved in current version.
     */
    private String fileStorage;
    private String fileLanguage;
    /*
     * Standard data folder for data loading, Required for excel client,
     * Loading data into database by excel client.
     */
    private String dataFolder;

    public String getFileStorage() {
        return this.fileStorage;
    }

    public void setFileStorage(final String fileStorage) {
        this.fileStorage = fileStorage;
    }

    public String getFileLanguage() {
        return this.fileLanguage;
    }

    public void setFileLanguage(final String fileLanguage) {
        this.fileLanguage = fileLanguage;
    }

    public Boolean getSupportSource() {
        return this.supportSource;
    }

    public void setSupportSource(final Boolean supportSource) {
        this.supportSource = supportSource;
    }

    public Class<?> getInitializer() {
        return this.initializer;
    }

    public void setInitializer(final Class<?> initializer) {
        this.initializer = initializer;
    }

    public Class<?> getPrerequisite() {
        return this.prerequisite;
    }

    public void setPrerequisite(final Class<?> prerequisite) {
        this.prerequisite = prerequisite;
    }

    public Class<?> getLoader() {
        return this.loader;
    }

    public void setLoader(final Class<?> loader) {
        this.loader = loader;
    }

    public String getDataFolder() {
        return this.dataFolder;
    }

    public void setDataFolder(final String dataFolder) {
        this.dataFolder = dataFolder;
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
                ", dataFolder='" + this.dataFolder + '\'' +
                '}';
    }
}
