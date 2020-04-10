package io.vertx.quiz.cv;

public enum QzApi {
    /* 1 tier */
    POST_ACTOR,         // POST     /api/:actor
    /* 2 tier */
    GET_ACTOR_KEY,      // GET      /api/:actor/:key
    PUT_ACTOR_KEY,      // PUT      /api/:actor/:key
    DELETE_ACTOR_KEY,   // DELETE   /api/:actor/:key
    /* JqTool */
    POST_ACTOR_SEARCH,  // POST     /api/:actor/search
    POST_ACTOR_MISSING, // POST     /api/:actor/missing
    POST_ACTOR_EXISTING,// POST     /api/:actor/existing

    /* Batch */
    POST_BATCH_UPDATE,  // POST     /api/batch/:actor/update
    POST_BATCH_DELETE,  // POST     /api/batch/:actor/delete

    /* Outer */
    DEFINED,
}
