/**
 * Defined special package for internal exception definition.
 * 1. This kind of exceptions won't connect any Annal Logger
 * 2. This error messages use vertx logger directly.
 */
package io.vertx.up.exception.internal;

interface Info {

    String NIL_MSG = "Empty stream exception found when {0}, caused = {1}.";

    String JSON_MSG = "The system met decoding/encoding up.god.file {0} exception, caused = {1}.";

    String ECODE_MSG = "The code = {0} of error is missing in your up.god.file: " +
        "vertx-error.yml, callee = {1}.";

    String ARG_MSG = "The method \"{0}\" of class \"{1}\" accept " +
        "({3} {2}) arguments only, the length is conflict";
    String LIME_FILE = "Lime node configured up.god.file = \"{0}\"" + " is missing, please check the missed up.god.file";

    String OP_MSG = "This operation is not supported! ( method = {0}, class = {1} )";

    String JEXL_MSG = "The expression \"{0}\" could not be parsed, details = {1}";

    String DATE_MSG = "The input `{0}` could not be parsed to valid date";

    String ATP_MSG = "The arguments could not be both null to build `io.vertx.up.atom.record.Apt`";

    String CACHE_KEY_MSG = "The input key of `Pool` is null, it''s conflict in current environment";

}
