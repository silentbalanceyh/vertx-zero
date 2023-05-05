package io.modello.specification;

import io.horizon.specification.typed.TJson;
import io.vertx.core.json.JsonObject;

import java.io.Serializable;

/*
 * Record interface for some data record using
 * 1. namespace: There are some format of `xxx.yyy.xxx`, and each `application` must contain only one namespace,
 * it must be business scope to split different application.
 * 2. identifier: In the whole environment, there must be gold rule:
 *  2.1 ) namespace + identifier should be unique ( Business Scope )
 *  2.2 ) Above format should be global id of one defined model.
 */
public interface HRecord extends Serializable, Meta, Check, Clone, TJson {
    /*
     * Provide attribute name and get related value
     * 1) field -> single field value
     * 2) fields ( field... ) -> subset of current record
     * 3) Get JsonObject of `this.data`
     */
    <T> T get(String field);

    JsonObject get(String... field);

    JsonObject get();

    /*
     * 「Replace Mode」
     * Set data here, @Fluent api
     * 1) Single field = value set
     * 2) JsonObject set into current record
     */
    <V> HRecord set(String field, V value);

    HRecord set(JsonObject data);

    /*
     * 「Append Mode」
     * Set data or missing, if existing do not set the value
     */
    <V> HRecord add(String field, V value);

    HRecord add(JsonObject data);

    /*
     * 「Add Virtual Mode」
     */
    <V> HRecord attach(String field, V value);

    /*
     * Remove by field / field
     */
    HRecord remove(String field);

    HRecord remove(String... fields);
}

