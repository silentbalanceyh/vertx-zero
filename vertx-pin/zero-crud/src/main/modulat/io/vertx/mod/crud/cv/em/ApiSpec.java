package io.vertx.mod.crud.cv.em;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public enum ApiSpec {
    /*
     * The request parameter sequence for specification
     *                                       actor       body                key         module          view
     * POST     /{actor}                     0           1 ( JObject )
     *          BODY_JSON
     * POST     /{actor}/search              0           1 ( JCriteria )                  2              3
     *          BODY_JSON
     * POST     /{actor}/existing            0           1 ( JCriteria )                  2
     *          BODY_JSON
     * POST     /{actor}/missing             0           1 ( JCriteria )                  2
     *          BODY_JSON
     * POST     /{actor}/import              0           1 ( filename )                   2
     *          BODY_STRING
     * POST     /{actor}/export              0           1 ( JCriteria )                  2              3
     *          BODY_JSON
     * DELETE   /batch/{actor}/delete        0           1 ( JArray )
     *          BODY_ARRAY
     * PUT      /batch/{actor}/update        0           1 ( JArray )                     2
     *          BODY_ARRAY
     * PUT      /columns/{actor}/my          0           1 ( JObject )                    2              3
     *          BODY_JSON
     * GET      /{actor}/{key}               0                               1
     *          BODY_STRING
     * DELETE   /{actor}/{key}               0                               1
     *          BODY_STRING
     * PUT      /{actor}/{key}               0           2                   1
     *          BODY_WITH_KEY
     * GET      /{actor}/by/sigma            0                                            1
     *          BODY_NONE
     * GET      /columns/{actor}/full        0                                            1              2
     *          BODY_NONE
     * GET      /columns/{actor}/my          0                                            1              2
     *          BODY_NONE
     */
    BODY_JSON,
    BODY_STRING,
    BODY_ARRAY,
    BODY_WITH_KEY,
    BODY_NONE
}
