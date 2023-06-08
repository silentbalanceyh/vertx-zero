package cn.vertxup.ambient.service.application;

import io.horizon.spi.extension.Init;
import io.horizon.spi.extension.Prerequisite;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

/**
 * ## EmApp initializer
 *
 * ### 1. Intro
 *
 * This interface provide different mode to initialize application data that stored in `X_APP` & `X_SOURCE` table.
 * Here provide three ways to initialize application with configuration.
 *
 * This service implementation called `At.initX` apis for the whole initialization workflow.
 *
 * ### 2. Workflow
 *
 * Please refer following table to check the workflow details:
 *
 * |Phase|Related|Comments|
 * |:---|---|:---|
 * |1. EmApp|`X_APP`|Combine or Fetch application basic data.|
 * |2. Database|`X_SOURCE`|Re-calculate the database source configuration and convert to Database.|
 * |3. Extension|None|Call `AtPin.getInit()` to get extension `Init` ( initializer ) and then call it.|
 * |4. Data Loading|None|Trigger data loading workflow to process OOB data.|
 *
 * ### 3. API
 *
 * For more details please refer each API document to check details.
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface InitStub {
    // ----------------- Creation / Edition for EmApp ----------------------

    /**
     * 「Async」( Creation ) This api is for application initialization at first time.
     *
     * Related Interface: {@link Init}
     *
     * @param appId {@link java.lang.String} The application primary key that stored in `KEY` field of `X_APP`.
     * @param data  {@link io.vertx.core.json.JsonObject} The data that will create application instance.
     *
     * @return {@link io.vertx.core.Future}<{@link io.vertx.core.json.JsonObject}>
     */
    Future<JsonObject> initCreation(String appId, JsonObject data);


    /**
     * 「Async」( Edition ) This api is for application initialization at any time after 1st.
     *
     * Related Interface: {@link Init}
     *
     * @param appName {@link java.lang.String} The application name that stored in `NAME` field of `X_APP`.
     *
     * @return {@link io.vertx.core.Future}<{@link io.vertx.core.json.JsonObject}>
     */
    Future<JsonObject> initEdition(String appName);

    /**
     * 「Async」( Modeling Only ) This api is new for modeling initialization.
     *
     * Related Interface: {@link Init}
     *
     * @param appName {@link java.lang.String} The application name that stored in `NAME` field of `X_APP`.
     *
     * @return {@link io.vertx.core.Future}<{@link io.vertx.core.json.JsonObject}>
     */
    Future<JsonObject> initModeling(String appName);

    Future<JsonObject> initModeling(String appName, String outPath);

    /**
     * 「Async」Pre-Workflow before initialization when call this method.
     *
     * Related Interface: {@link Prerequisite}
     *
     * @param appName {@link java.lang.String} The application name that stored in `NAME` field of `X_APP`.
     *
     * @return {@link io.vertx.core.Future}<{@link io.vertx.core.json.JsonObject}>
     */
    Future<JsonObject> prerequisite(String appName);
}
