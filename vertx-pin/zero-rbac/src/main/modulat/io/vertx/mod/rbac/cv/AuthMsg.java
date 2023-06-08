package io.vertx.mod.rbac.cv;

public interface AuthMsg {

    String CODE_VERIFY = "Input data when verification: client_id = {0}, code = {1}";
    String CODE_FILTER = "Authorization Code Filters: {0}";

    String RELATION_USER_ROLE = "Fetch relations ( User - Role ) by User key: {0}";
    String RELATION_GROUP = "Fetch relations ( User - Group ) by User key: {0}";
    String RELATION_GROUP_ROLE = "Fetch relation ( Group - Role ) by Group key: {0}, Mode: {1}";

    String LOGIN_SUCCESS = "[ Ακριβώς ] User ( username = {0} ) login successfully.";
    String LOGIN_USER = "[ Ακριβώς ] username = {0} does not exist.";
    String LOGIN_LOCKED = "[ Ακριβώς ] username = {0} has been locked / disabled";
    String LOGIN_PWD = "[ Ακριβώς ] username = {0}, the password ( {1} ) you provided is wrong.";
    // String LOGIN_INIT = "[ Ακριβώς ] username = {0}, the password ( {1} ) is default and contains risk";
    String LOGIN_INPUT = "Login processing execute ( username = {0} )";

    String TOKEN_STORE = "The system will singleton user''s principle information. user key: {0}.";
    String TOKEN_INPUT = "Verify data = {1} from token = {0}";
    String TOKEN_CACHED = "Verify data = {1} from cache token = {0}";
    String TOKEN_JWT = "Jwt token data stored: {0}.";

    String TOKEN_SIZE_NULL = "Token size invalid ( null ): {0}, user: {1}.";
    String TOKEN_SIZE_EMPTY = "Token size invalid ( empty ): {0}, user: {1}.";
    String TOKEN_SIZE_MULTI = "Token size invalid ( multi ): {0}, user: {1}.";
    String TOKEN_INVALID = "Token invalid {0}";
    String TOKEN_EXPIRED = "Token you provided {0} is expired at: {1}.";

    String CREDIT_ACTION = "1. Accredit action ( uri = `{0}`, method = `{1}`, normalizedUri = `{2}` ).";
    String CREDIT_RESOURCE = "2. Accredit resource ( resource = `{0}` ).";
    String CREDIT_LEVEL = "3. Accredit level ( action = {0}, resource = {1} ).";
    String CREDIT_PERMISSION = "4. Accredit profile ( profileKey = {0} ).";
    String CREDIT_AUTHORIZED = "5. Accredit authorized ( permission = {0} ).";
    String CREDIT_BOUND = "6. Accredit bound ( bound = {0} and key = {1} )";
    String CREDIT_SUCCESS = "7. Accredit authorized cache ( key = {0} ) stored into ( session = {1} )";

    String REGION_BEFORE = "--> DataRegion Before: uri = {0}, region = {1}";
    String REGION_AFTER = "<-- DataRegion After: {0}";
    String REGION_TYPE = "DataRegion Analyzed type: {0}, data = {1}";
    String REGION_ROWS = "DataRegion Rows -> {0}";
    String REGION_PROJECTION = "DataRegion Projection -> {0}";
    String REGION_FLUSH = "DataRegion Flush -> dataKey = {1}, habitus = {0}, data finished = {2}";

    String SEEKER_RESOURCE = "Seeking Resource with uri = {0}, method = {1}, sigma = {2}";
    String POOL_RESOURCE = "Pool = {0} has been initialized to `habitus` = {1}";
    String VIEW_PROCESS = "My View Operation: {0}, filters = {1}";

    // -- visitant process for additional
    String VISITANT_PROCESS = "My Visitant Op: {0}, filters = {1}";


    String EXTENSION_BY_USER = "Fetch User Extension information by id = {0}";
    String EXTENSION_EMPTY = "User Extension empty in account workflow.";
}
