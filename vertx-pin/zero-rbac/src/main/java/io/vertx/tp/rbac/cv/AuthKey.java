package io.vertx.tp.rbac.cv;

public interface AuthKey {
    /**
     * Default state field
     */
    String STATE = "state";
    String SCOPE = "scope";
    String AUTH_CODE = "code";

    String USER_NAME = "username";
    String PASSWORD = "password";
    String PRIORITY = "priority";
    /**
     * Request Parameters
     */
    String CLIENT_ID = "client_id";
    String CLIENT_SECRET = "client_secret";
    String RESPONSE_TYPE = "response_type";

    String ACCESS_TOKEN = "access_token";
    String REFRESH_TOKEN = "refresh_token";
    String IAT = "iat";
    /**
     * Pojo Field
     */
    String F_USER_ID = "userId";
    String F_ROLE_ID = "roleId";
    String F_GROUP_ID = "groupId";
    String F_PARENT_ID = "parentId";
    String F_PERMISSION_ID = "permissionId";

    String F_CLIENT_ID = "clientId";
    String F_CLIENT_SECRET = "clientSecret";
    String F_GRANT_TYPE = "grantType";

    /**
     * Authorization Workflow
     */
    String F_URI = "uri";
    String F_URI_REQUEST = "requestUri";
    String F_METHOD = "method";
    String F_METADATA = "metadata";
    String F_HEADERS = "headers";

    /**
     * Could not configure authorization pool
     */
    String PROFILE_PERM = "PERM";
    String PROFILE_ROLE = "ROLE";

    interface Acl {
        /*
         * Five constant for checking
         */
        String BEFORE_PROJECTION = "BEFORE_PROJECTION";
        String BEFORE_CRITERIA = "BEFORE_CRITERIA";
        String AFTER_RECORD = "AFTER_RECORD";
        String AFTER_ROWS = "AFTER_ROWS";
        String AFTER_COLLECTION = "AFTER_COLLECTION";
    }
}
