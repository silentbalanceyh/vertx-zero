package io.vertx.tp.crud.cv.em;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public enum ApiSpec {
    /*
     * The request parameter sequence for specification
     *                                       actor       body                key         module          view
     * POST     /{actor}                     0           1 ( JObject )
     * POST     /{actor}/search              0           1 ( JCriteria )                  2              3
     * POST     /{actor}/existing            0           1 ( JCriteria )                  2
     * POST     /{actor}/missing             0           1 ( JCriteria )                  2
     * POST     /{actor}/import              0           1 ( filename )                   2
     * POST     /{actor}/export              0           1 ( JCriteria )                  2              3
     * DELETE   /batch/{actor}/delete        0           1 ( JArray )
     * PUT      /batch/{actor}/update        0           1 ( JArray )                     2
     * PUT      /columns/{actor}/my          0           1 ( JObject )                    2              3
     * GET      /{actor}/{key}               0                               1
     * DELETE   /{actor}/{key}               0                               1
     * PUT      /{actor}/{key}               0           2                   1
     * GET      /{actor}/by/sigma            0                                            1
     * GET      /columns/{actor}/full        0                                            1              2
     * GET      /columns/{actor}/my          0                                            1              2
     */
    BODY_JSON,
    BODY_STRING,
    BODY_ARRAY,
    BODY_WITH_KEY,
    BODY_NONE
}
