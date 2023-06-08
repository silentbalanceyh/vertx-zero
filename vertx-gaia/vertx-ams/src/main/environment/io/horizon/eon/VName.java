package io.horizon.eon;

/**
 * @author lang : 2023/4/24
 */
public interface VName {
    String QBE = "QBE";                          /* QBE = ? */
    String ENV = "ENV";                          /* ENV */
    String QR = "qr";                            /* qr */

    String PLOT = "plot";                        /* Plot Configuration */
    String CLOUD = "cloud";                      /* Plot Cloud Attribute */
    String CHILD = "child";                      /* Plot Child Attribute */

    String EVENT = "event";                      /* event */
    String VIRTUAL = "virtual";                  /* virtual resource for RBAC */
    String TRACKABLE = "trackable";              /* trackable */
    String ID = "id";                            /* Third Part integration primary key */
    String GLOBAL_ID = "globalId";               /* Third part global id of primary key */

    String FIELD = "field";                      /* Model definition field */
    String ATTRIBUTE = "attribute";              /* Attribute */
    String FIELDS = "fields";                    /* Model definition fields */
    String FORM = "form";                        /* Form */
    String MODEL = "model";                      /* Model definition field */
    String ENTITY = "entity";                    /* Model definition field */
    String JOINED_KEY = "joinedKey";             /* Model definition for connect model to schema */
    String ENTITY_ID = "entityId";               /* Model definition to stored related Entity Id of Field/Key/Index */
    String NAMESPACE = "namespace";              /* Model definition of Multi-App environment, each application contains only one namespace */
    String IDENTIFIER = "identifier";            /* Model definition, identifier field ( Uniform identifier ) */
    String RULE_UNIQUE = "ruleUnique";           /* Model definition, ruleUnique field */
    String RULE = "rule";                        /* Model definition, rule for source reference */
    String TABLE_NAME = "tableName";             /* Model definition, tableName field */

    String ENTRY = "entry";                      /* Entry checking */
    String ENTRY_ID = "entryId";                 /* Entry id for menu extracting */
    String TABLE = "table";                      /* Model Connect: table */

    String MODEL_ID = "modelId";                 /* Model Consumer ( identifier ) field */
    String MODEL_KEY = "modelKey";               /* Model Consumer ( key ) field */
    String MODEL_CATEGORY = "modelCategory";     /* Model Consumer ( related XCategory ) field */
    String MODEL_COMPONENT = "modelComponent";   /* Model Component */
    String MODEL_CHILD = "modelChild";           /* Model Component */
    String QUANTITY = "quantity";                /* Quantity -> Children Size */
    String CACHE_KEY = "cacheKey";               /* RapidKey -> Cache Key */

    String SCOPE = "scope";                      /* OAuth scope field */
    String REALM = "realm";                      /* Authorization realm field for security */
    String GRANT_TYPE = "grantType";             /* OAuth grant type field */
    String RESOURCE_ID = "resourceId";           /* Security Action related resource field */
    String RESOURCE = "resource";                /* Security Action for admit resource field */

    String HANDLER = "handler";                  /* Sock Handler Usage */

    String BRIDGE = "bridge";                    /* Sock Bridge Usage */
    String HOST = "host";                        /* Host */
    String HOSTNAME = "hostname";                /* Host Name */
    String PORT = "port";                        /* Port */

    String HABITUS = "habitus";                  /* Authorization header to store current logged user session data, Permission Pool */
    String DYNAMIC = "dynamic";                  /* View security of field for dynamic view name */
    String VIEW = "view";                        /* View security of view name */
    String PROFILE = "profile";                  /* */
    String PREFIX = "prefix";
    String POSITION = "position";                /* View security of view position */
    String MODULE = "module";                    /* View sub-module picking up */

    String ROLE = "role";                        /* Security Object: role field */
    String ROLES = "roles";                      /* Security Object: roles field */
    String ROLE_ID = "roleId";                   /* Security Object: role id ( X_ROLE key ) field */
    String USER = "user";                        /* Security Object: user field */
    String AUDITOR = "auditor";                  /* Security Object: auditor field */
    String USER_ID = "userId";                   /* Security Object: user id ( X_USER key ) field */
    String USERNAME = "username";                /* Security Object: user name ( X_USER username) field*/
    String REAL_NAME = "realname";               /* Security Object: user real name field */
    String GROUP = "group";                      /* Security Object: group */
    String GROUPS = "groups";                    /* Security Object: groups */
    String ALIAS = "alias";                      /* Security Object: another name for current */
    String PASSWORD = "password";                /* Security Object: Password belong to field of security framework, ( X_USER password ) field */
    String EMAIL = "email";                      /* Security Object: user email ( X_USER email ) field */
    String MOBILE = "mobile";                    /* Security Object: user mobile ( X_USER mobile ) field */
    String CLIENT_ID = "clientId";               /* Security Object: OAuth user `clientId` field, mapped to X_USER key */

    String ACTOR = "actor";                      /* Dynamic channel for module definition, mapped to X_MODULE */

    String ITEMS = "items";                      /* Batch operation, items -> JsonArray ( element = JsonObject ) */
    String CHILDREN = "children";                /* For React */
    String KEYS = "keys";                        /* Batch operation, keys -> JsonArray ( element = String ) */
    String CODES = "codes";                      /* Batch operation, codes -> JsonArray ( element = String ) */

    String DATA_KEY = "dataKey";                 /* Security belong-to field: Authorization data stored key for session storage */

    String APP_KEY = "appKey";                   /* XHeader for X-App-Key */
    String APP_ID = "appId";                     /* XHeader for X-App-Id */
    String SIGMA = "sigma";                      /* XHeader for X-Sigma */
    String CATALOG = "catalog";                  /* catalog */

    String DEBUG = "debug";                      /* Development: for debugging */
    String DEVELOPER = "developer";              /* Development: for developer */
    String DEVELOPMENT = "development";          /* Development: for development */

    String APP = "application";                  /* Reserved: */

    String KEY = "key";                          /* Common: primary key */
    String KEY_P = "pKey";                       /* Common: argument key */
    String NAME = "name";                        /* Common: name */
    String ORDER = "order";                      /* Common: order */
    String CODE = "code";                        /* Common: code */
    String VALUE = "value";                      /* Common: value */
    String LITERAL = "literal";                  /* Common: literal */

    String LABEL = "label";                      /* Common: label */
    String TYPE = "type";                        /* Common: type for different model */
    String ROBIN = "robin";
    String DEPLOY_ID = "deployID";               /* */
    String ASPECT = "aspect";                    /* Aspect Component Usage */
    String DEPLOYMENT = "deployment";            /* */
    String CATEGORY = "category";                /* Common: category */
    String SERVICE = "service";                  /* Common: service */

    String SERVER = "server";                    /* Common: server */
    String DATA = "data";                        /* Common: data */
    String DATABASE = "database";                /* Common: database */
    String KIND = "kind";                        /* Common: kind */

    String FORK = "fork";                        /* Fork/Join -> Fork */
    String JOIN = "join";                        /* Fork/Join -> Join */
    String MODE = "mode";                        /* Mode Selection */
    String GATEWAY = "gateway";                  /* Gateway for Workflow */
    String INPUT = "input";                      /* Input */
    String OUTPUT = "output";                    /* Output */
    String DATUM = "datum";                      /* Common: metadata key */
    String MAPPING = "mapping";                  /* Common: Json mapping configuration */
    String ATOM = "atom";                        /* Common: Atom */
    String STATUS = "status";                    /* Common: status for different workflow */
    String SERIAL = "serial";                    /* Common: serial field ( XNumber related or other meaningful serial */

    String METADATA = "metadata";                /* Shared: metadata for most table of METADATA ( JsonObject ) field */
    String ACTIVE = "active";                    /* Shared: active field for most table of ACTIVE ( Boolean ) field */
    String ACTIVITY_ID = "activityId";
    String LANGUAGE = "language";                /* Shared: language field for most table of LANGUAGE ( String ) field */

    String NUMBERS = "numbers";                  /* Definition: numbers definition here */
    String OUT = "out";                          /* Definition: input definition */
    String IN = "in";                            /* Definition: output definition */
    String OPTIONS = "options";                  /* Definition: configuration options */
    String COMPONENTS = "components";            /* Definition: components */
    String COMPONENT = "component";              /* Definition: ( Single ) component */

    String SELECTOR = "selector";                /* Definition: selector */

    String SOURCE = "source";                    /* Database ( X_SOURCE ) related field */
    String SOURCE_DATA = "sourceData";           /* sourceData */
    String SOURCE_FIELD = "sourceField";         /* sourceField */
    String SOURCE_DICT = "sourceDict";           /* sourceDict */
    String SOURCE_PARAMS = "sourceParams";       /* sourceParams */
    String SOURCE_CONSUMER = "sourceConsumer";   /* sourceConsumer */
    String SOURCE_NORM = "sourceNorm";           /* sourceNorm */
    String SOURCE_EXPR = "sourceExpression";     /* sourceExpression */
    String SOURCE_EXPR_CHAIN = "sourceExprChain";/* sourceExpressionChain */

    String TARGET = "target";
    String TARGET_KEY = "targetKey";
    String TARGET_DATA = "targetData";
    String SOURCE_KEY = "sourceKey";
    String CLASS = "class";


    String EPSILON = "epsilon";                  /* Dictionary Consumer */
    String FORMAT = "format";                    /* DataFormat json configuration of uniform */

    String MATRIX = "matrix";                    /* Attribute Matrix for 8 dim */
    String REFERENCE = "reference";              /* Attribute Reference for */

    String METHOD = "method";                    /* Web: http method */
    String SESSION = "session";                  /* Web: session */
    String URI = "uri";                          /* Web: http path */
    String URIS = "uris";                        /* Web: http path */
    String ROUTER = "router";                    /* Web: Router */
    String URI_IMPACT = "impactUri";             /* Web: http impact uri */
    String URI_REQUEST = "requestUri";           /* Web: http path ( normalized ) contains path such as `/api/:code/name` instead of actual */
    String RESULT = "result";                    /* Web: http response */
    String HEADER = "header";                    /* Web: http header */
    String MULTIPLE = "multiple";                /* If multiple */

    String CHANGES = "changes";                  /* XActivityChange items to store history operation of changes */
    String RECORD = "record";                    /* Change calculation for data record */
    String RECORD_NEW = "recordNew";             /* Change calculation to store the latest record */
    String RECORD_OLD = "recordOld";             /* Change calculation to store the previous record */
    String NEXT = "next";                        /* Whether next trigger */

    String CREATED_AT = "createdAt";             /* Auditor created At */
    String UPDATED_AT = "updatedAt";             /* Auditor updated At */
    String CREATED_BY = "createdBy";             /* Auditor created By */
    String UPDATED_BY = "updatedBy";             /* Auditor updated By */

    String COMPANY_ID = "companyId";             /* Company Id */
    String DEPT_ID = "deptId";                   /* Department Id */
    String DEPT = "dept";                        /* Dept */
    String TEAM_ID = "teamId";                   /* Team Id */
    String TENANT_ID = "tenantId";               /* Tenant Id */
    String TEAM = "team";                        /* Team */
    String WORK_NUMBER = "workNumber";           /* Work Number */

    String WEB_SOCKET = "websocket";             /* Web Socket */
    String CONFIG = "config";                    /* Acl Usage */
    String PHASE = "phase";                      /* Acl Phase */
    String SEEKER = "seeker";                    /* Acl Seeker */
    String SYNTAX = "syntax";                    /* Acl Syntax */
    String VIEW_ID = "viewId";                   /* View Id */
    String PERMISSION_ID = "permissionId";       /* Security: permissionId */
    String ACTIONS = "actions";                  /* Security: actions */

    String DAO = "dao";                          /* Dao field in json configuration */
    String WEB = "web";                          /* Web Prefix */
    String CONNECT = "connect";                  /* Connect field in json configuration */
    String PLUGIN_IO = "plugin.io";              /* Attribute Plugin for "io" of source config */
    String TIMER = "timer";                      /* Timer in Job */

    String LIST = "list";                        /* Page Data */
    String COUNT = "count";                      /* Page Count */

    String AT = "at";                            /* at field */
    String BY = "by";                            /* by field */
    String INDENT = "indent";                    /* indent spec number */

    String ACCESS_TOKEN = "access_token";        /* token cv from `jwt` to `access_token` */

    String UNIQUE = "unique";                    /* Workflow: unique record */
    String FLAG = "flag";                        /* Workflow: flag type of record */
    String INSTANCE = "instance";                /* Workflow: instance = true */
    String OWNER = "owner";                      /* Workflow: owner */
    String HISTORY = "history";                  /* Workflow: history */
    String EDITION = "edition";
    String READ_ONLY = "readOnly";

    String SIZE = "size";                        /* XAttachment, Size Attribute */
    String MIME = "mime";                        /* XAttachment, Web Flow mime processing */
    String EXTENSION = "extension";              /* XAttachment, extension field attribute */

    String FILE_KEY = "fileKey";                 /* XAttachment belong-to field */
    String FILE_NAME = "filename";               /* XAttachment filename */

    String LINKAGE = "linkage";                  /* XLinkage instance */
    String QUERY = "query";                      /* XLinkage query */

    // Store Path/Root
    String STORE = "store";                      /* File Management, Store */
    String STORE_ROOT = "storeRoot";             /* File Management, store root */
    String STORE_PATH = "storePath";             /* File Management, store path for directory / attachment */
    String STORE_PARENT = "storeParent";         /* File Management, store parent */
    String INTEGRATION_ID = "integrationId";     /* File Management, integration key */
    String DIRECTORY_ID = "directoryId";         /* File Management, directory id */
    String DIRECTORY = "directory";              /* File Management, directory field, two means */
    String INITIALIZE = "initialize";            /* File Management, initialize field */
    String INIT = "init";                        /* Engine for init */

    String PARENT_ID = "parentId";               /* Tree for parent id */
    String LEVEL = "level";                      /* Tree for level */
    String KEY_WORD = "keyword";                 /* Search Key Word */
    String SORT = "sort";                        /* Sort */

    String INCLUDE = "include";                  /* Auditor Pin */
    String EXCLUDE = "exclude";                  /* Auditor Pin */

    String COMMENT = "comment";                  /* Text Part: comment */
    String DESCRIPTION = "description";          /* Text Part: description */
    String REMARK = "remark";                    /* Text Part: remark */
    String REMARKS = "remarks";                  /* Text Part: remarks */
    String TEXT = "text";                        /* Text Part: text */

    String VISIT = "visit";
    String VISIT_MODE = "visitMode";
    String VISIT_ROLE = "visitRole";
    String VISIT_GROUP = "visitGroup";
    String VISIT_COMPONENT = "visitComponent";

    String UI_CONFIG = "uiConfig";
    String UI_CONDITION = "uiCondition";
    String UI_SURFACE = "uiSurface";
    String UI_STYLE = "uiStyle";
    String UI_SORT = "uiSort";
    String UI_ICON = "uiIcon";
    String UI_TYPE = "uiType";

    String DM_TYPE = "dmType";
    String DM_CONFIG = "dmConfig";
    String DM_CONDITION = "dmCondition";


    String OWNER_TYPE = "ownerType";
    String OWNER_ID = "ownerId";
    String RUN_TYPE = "runType";
    String PATH = "path";
    String MOVE = "move";
    String MESSAGE = "message";
    String INFO = "info";
    String ERROR = "error";

    String ACL = "acl";

    /**
     * 特殊属性，用于系统做比对专用
     * 可直接被继承
     */
    interface __ {
        String OLD = "__OLD__";
        String NEW = "__NEW__";
        String METADATA = "__" + VName.METADATA;     /* __metadata for definition on modulat */
        String DATA = "__" + VName.DATA;             /* __data for previous data */
        String FLAG = "__" + VName.FLAG;             /* __flat for operation flag */

        String INPUT = "__" + VName.INPUT;           /* __input for input original data */
        String USER = "__" + VName.USER;             /* __user for user extraction */
        String REFERENCE = "__" + VName.REFERENCE;   /* __reference for dict/assist etc */
        String MESSAGE = "__" + VName.MESSAGE;       /* __message for workflow */
        String ACL = "__" + VName.ACL;                /* __acl */
        String QR = "__" + VName.QR;                 /* __qr */
        String CLASS = "__" + VName.CLASS;           /* __class */
    }
}
