package io.vertx.core.http;


/**
 * 「Co」Zero Http Constant
 *
 * This enum class provide the whole HTTP status code here, when you use zero framework, we
 * recommend to use HTTP Status code in the whole web flow to implement the meaningful RESTful
 * Api.
 *
 * Here are some consideration points about RESTful design:
 *
 * 1. Release the protocol power of HTTP for application.
 * 2. Client / Server Preference about MIME resolution.
 * 3. Http Idempotent in Web application.
 * 4. Conditional query / searching.
 * 5. Web Security and plugin architecture for application.
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public enum HttpStatusCode {
    // ~ 1xx =================================================
    /**
     * 100 Continue
     **/
    CONTINUE(100, "Continue"),

    /**
     * 101 Switching Protocols
     **/
    SWITCHING_PROTOCOLS(101, "Switching Protocols"),

    /**
     * 「WebDAV」102 Processing
     **/
    PROCESSING(102, "Processing"),
    // ~ 2xx =================================================
    /**
     * 200 OK
     **/
    OK(200, "OK"),

    /**
     * 201 Created
     **/
    CREATED(201, "Created"),

    /**
     * 202 Accepted
     **/
    ACCEPTED(202, "Accepted"),

    /**
     * 203 Non-Authoritative Information
     **/
    NOT_AUTHORITATIVE_INFORMATION(203, "Non-Authoritative Information"),

    /**
     * 204 No Content
     **/
    NO_CONTENT(204, "No Content"),

    /**
     * 205 Reset Content
     **/
    RESET_CONTENT(205, "Reset Content"),

    /**
     * 206 Partial Content
     **/
    PARTIAL_CONTENT(206, "Partial Content"),

    /**
     * 「WebDAV」207 Multi-Status
     **/
    MULTI_STATUS(207, "Multi-Status"),

    /**
     * 「WebDAV」208 Already Reported
     */
    ALREADY_REPORTED(208, "Already Reported"),

    // ~ 3xx =================================================
    /**
     * 300 Multiple Choices
     **/
    MULTIPLE_CHOICES(300, "Multiple Choices"),

    /**
     * 301 Moved Permanently
     **/
    MOVED_PERMANENTLY(301, "Moved Permanently"),

    /**
     * 302 Found
     **/
    FOUND(302, "Found"),

    /**
     * 303 See Other
     **/
    SEE_OTHER(303, "See Other"),

    /**
     * 304 Not Modified
     **/
    NOT_MODIFIED(304, "Not Modified"),

    /**
     * 305 Use Proxy
     **/
    USE_PROXY(305, "Use Proxy"),

    /**
     * 306 Switch Proxy
     **/
    SWITCH_PROXY(306, "Switch Proxy"),

    /**
     * 307 Temporary Redirect
     **/
    TEMPORARY_REDIRECT(307, "Temporary Redirect"),

    /**
     * 308 Permanent Redirect
     **/
    PERMANENT_REDIRECT(308, "Permanent Redirect"),
    // ~ 4xx =================================================
    /**
     * 400 Bad Request
     **/
    BAD_REQUEST(400, "Bad Request"),

    /**
     * 401 Unauthorized
     **/
    UNAUTHORIZED(401, "Unauthorized"),

    /**
     * 402 Payment Required
     **/
    PAYMENT_REQUIRED(402, "Payment Required"),

    /**
     * 403 Forbidden
     **/
    FORBIDDEN(403, "Forbidden"),

    /**
     * 404 Not Found
     **/
    NOT_FOUND(404, "Not Found"),

    /**
     * 405 Method Not Allowed
     **/
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),

    /**
     * 406 Not Acceptable
     **/
    NOT_ACCEPTABLE(406, "Not Acceptable"),

    /**
     * 407 Proxy Authentication Required
     **/
    PROXY_AUTHENTICATION_REQUIRED(407, "Proxy Authentication Required"),

    /**
     * 408 Request Timeout
     **/
    REQUEST_TIMEOUT(408, "Request Timeout"),

    /**
     * 409 Conflict
     **/
    CONFLICT(409, "Conflict"),

    /**
     * 410 Gone
     **/
    GONE(410, "Gone"),

    /**
     * 411 Length Required
     **/
    LENGTH_REQUIRED(411, "Length Required"),

    /**
     * 412 Precondition Failed
     **/
    PRECONDITION_FAILED(412, "Precondition Failed"),

    /**
     * 413 Request Entity Too Large
     **/
    REQUEST_ENTITY_TOO_LARGE(413, "Request Entity Too Large"),

    /**
     * 414 Request-URI Too Long
     **/
    REQUEST_URI_TOO_LONG(414, "Request-URI Too Long"),

    /**
     * 415 Unsupported Media Type
     **/
    UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type"),

    /**
     * 416 Requested Range Not Satisfiable
     **/
    REQUESTED_RANGE_NOT_SATISFIABLE(416, "Requested Range Not Satisfiable"),

    /**
     * 417 Expectation Failed
     **/
    EXPECTATION_FAILED(417, "Expectation Failed"),

    /**
     * 418 Unused
     */
    UNUSED(418, "Unused"),

    /**
     * 421 Misdirected Request
     **/
    MISDIRECTED_REQUEST(421, "Misdirected Request"),

    /**
     * 422 Unprocessable Entity
     **/
    UNPROCESSABLE_ENTITY(422, "Unprocessable Entity"),

    /**
     * 「WebDAV」423 Locked
     **/
    LOCKED(423, "Locked"),

    /**
     * 「WebDAV」424 Failed Dependency
     **/
    FAILED_DEPENDENCY(424, "Failed Dependency"),

    /**
     * 「WebDAV」425 Unordered Collection
     **/
    UNORDERED_COLLECTION(425, "Unordered Collection"),

    /**
     * 426 Upgrade Required
     **/
    UPGRADE_REQUIRED(426, "Upgrade Required"),

    /**
     * 「WebDAV」428 Precondition Required
     */
    PRECONDITION_REQUIRED(428, "Precondition Required"),

    /**
     * 「WebDAV」429 Too Many Request
     */
    TOO_MANY_REQUEST(429, "Too Many Requests"),

    /**
     * 「WebDAV」431 Request Header Fields Too Large
     */
    REQUEST_HEADER_FIELDS_TOO_LARGE(431, "Request Header Fields Too Large"),

    /**
     * 「微软」449 Retry With
     **/
    RETRY_WITH(449, "Retry With"),

    /**
     * 「IETF」451 Unavailable For Legal Reasons
     */
    UNAVAILABLE_FOR_LEGAL_REASONS(451, "Unavailable For Legal Reasons"),

    /**
     * 「Nginx」499 Client Closed Request
     */
    CLIENT_CLOSED_REQUEST(499, "Client Closed Request"),
    // ~ 5xx =================================================

    /**
     * 500 Internal Server Error
     **/
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),

    /**
     * 501 Not Implemented
     **/
    NOT_IMPLEMENTED(501, "Not Implemented"),

    /**
     * 502 Bad Gateway
     **/
    BAD_GATEWAY(502, "Bad Gateway"),

    /**
     * 503 Service Unavailable
     **/
    SERVICE_UNAVAILABLE(503, "Service Unavailable"),

    /**
     * 504 Gateway Timeout
     **/
    GATEWAY_TIMEOUT(504, "Gateway Timeout"),

    /**
     * 505 HTTP Version Not Supported
     **/
    HTTP_VERSION_NOT_SUPPORTED(505, "HTTP Version Not Supported"),

    /**
     * 506 「扩展」Variant Also Negotiates
     **/
    VARIANT_ALSO_NEGOTIATES(506, "Variant Also Negotiates"),

    /**
     * 507 「WebDAV」Insufficient Storage
     **/
    INSUFFICIENT_STORAGE(507, "Insufficient Storage"),

    /**
     * 508 「WebDAV」Loop Detected
     **/
    LOOP_DETECTED(508, "Loop Detected"),
    /**
     * 509 「扩展」Badwidth Limit Exceeded
     **/
    BANDWIDTH_LIMIT_EXCEEDED(509, "Bandwidth Limit Exceeded"),

    /**
     * 510 「扩展」Not Extended
     **/
    NOT_EXTENDED(510, "Not Extended"),

    /**
     * 511 「扩展」Network Authentication Required
     */
    NETWORK_AUTHENTICATION_REQUIRED(511, "Network Authentication Required"),

    /**
     * 599 Network Connect Timeout Error
     */
    NETWORK_CONNECT_TIMEOUT_ERROR(599, "Network Connect Timeout Error"),
    /**
     * 600 Unparseable Response Headers
     **/
    UNPARSEABLE_RESPONSE_HEADERS(600, "Unparseable Response Headers");

    /**
     * Http Status
     **/
    private final int statusCode;
    /**
     * Http Info
     **/
    private final String message;


    /**
     * Default scope ( package ), The constructor of Http Status Code
     *
     * @param statusCode int status value
     * @param message    string status description
     */
    HttpStatusCode(final int statusCode, final String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    /**
     * int to HttpStatusCode
     *
     * @param statusCode Input int value of http status code
     *
     * @return HttpStatusCode object
     */
    public static HttpStatusCode fromCode(final int statusCode) {
        final HttpStatusCode[] values = HttpStatusCode.values();
        HttpStatusCode code = null;
        for (final HttpStatusCode value : values) {
            if (statusCode == value.statusCode) {
                code = value;
                break;
            }
        }
        return code;
    }

    /**
     * String to HttpStatusCode
     *
     * @param message String literal value of http status code
     *
     * @return HttpStatusCode object
     */
    public static HttpStatusCode fromString(final String message) {
        final HttpStatusCode[] values = HttpStatusCode.values();
        HttpStatusCode code = null;
        for (final HttpStatusCode value : values) {
            if (value.message.equals(message)) {
                code = value;
                break;
            }
        }
        return code;
    }

    /**
     * Return int value of http status code
     *
     * @return int value
     */
    public int code() {
        return this.statusCode;
    }

    /**
     * Return message of http status code
     *
     * @return string description
     **/
    public String message() {
        return this.message;
    }

    /**
     *
     **/
    @Override
    public String toString() {
        return this.message();
    }


}
