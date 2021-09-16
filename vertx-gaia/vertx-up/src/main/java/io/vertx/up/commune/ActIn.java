package io.vertx.up.commune;

import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.exchange.BiTree;
import io.vertx.up.eon.ID;
import io.vertx.up.fn.Fn;
import io.vertx.zero.exception.ActSpecificationException;

import java.io.File;
import java.io.Serializable;

/*
 * Business Request
 * 「ActIn」 means Action Request and offer business component parameters here.
 * 1) This DTO contains: JsonObject ( Pure Data ), ZRecord ( Record Structure )
 * 2) Bind record data structure for serialization/deserialization.
 *
 * 「Workflow」
 *  In ——
 *      Consumer -> ZApi/Envelop ->
 *                      Channel -> ActIn ->
 *                                          Component
 *  Out ——
 *      Component -> ActOut ->
 *                      Envelop ->
 *                             Consumer ->
 *                                    SendAim ( Callback )
 */
public class ActIn extends ActMapping implements Serializable {

    /* Raw data of `Envelop` object/reference */
    private final transient Envelop envelop;
    private final boolean isBatch;
    private final transient ActFile file;

    private transient ActJObject json;
    private transient ActJArray jarray;
    private transient Record definition;

    private transient BiTree mapping;

    public ActIn(final Envelop envelop) {
        /* Envelop reference here */
        this.envelop = envelop;
        /*
         * Whether
         * 1) JsonObject
         * 2) JsonArray
         * */
        final JsonObject data = envelop.data();
        if (data.containsKey(ID.PARAM_BODY)) {
            final Object value = data.getValue(ID.PARAM_BODY);
            if (value instanceof JsonArray) {
                this.jarray = new ActJArray(envelop);
                this.isBatch = true;        // Batch
            } else {
                this.json = new ActJObject(envelop);
                this.isBatch = false;       // Single
            }
        } else {
            this.json = new ActJObject(envelop);
            this.isBatch = false;           // Single
        }
        /*
         * Optional to stored file here
         */
        final JsonArray stream = data.getJsonArray(ID.PARAM_STREAM);
        this.file = new ActFile(stream);
    }

    public ActIn bind(final BiTree mapping) {
        this.mapping = mapping;
        return this;
    }

    public Envelop getEnvelop() {
        return this.envelop;
    }

    public JsonObject getJObject() {
        if (this.isBatch) {
            return new JsonObject();
        } else {
            return this.json.getJson(this.mapping);
        }
    }

    public JsonArray getJArray() {
        if (this.isBatch) {
            return this.jarray.getJson(this.mapping);
        } else {
            return new JsonArray();
        }
    }

    public JsonObject getQuery() {
        Fn.outUp(this.isBatch, ActSpecificationException.class, this.getClass(), this.isBatch);
        return this.json.getQuery();
    }

    public Record getRecord() {
        Fn.outUp(this.isBatch, ActSpecificationException.class, this.getClass(), this.isBatch);
        return this.json.getRecord(this.definition, this.mapping);
    }

    public File[] getFiles() {
        return this.file.getFiles();
    }

    public Record[] getRecords() {
        Fn.outUp(!this.isBatch, ActSpecificationException.class, this.getClass(), this.isBatch);
        return this.jarray.getRecords(this.definition, this.mapping);
    }

    public Record getDefinition() {
        return this.definition;
    }

    /*
     * Header value extracted
     */
    public String appId() {
        final MultiMap paramMap = this.envelop.headers();
        return paramMap.get(ID.Header.X_APP_ID);
    }

    public String sigma() {
        final MultiMap paramMap = this.envelop.headers();
        return paramMap.get(ID.Header.X_SIGMA);
    }

    /*
     * 1) Set input data to Record object ( reference here )
     */
    public void connect(final Record definition) {
        this.definition = definition;
    }
}
