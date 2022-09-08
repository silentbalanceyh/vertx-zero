package io.vertx.up.eon;

import io.vertx.up.atom.query.engine.Qr;

import java.util.HashSet;
import java.util.Set;

public interface KName {
    String TRACKABLE = "trackable";              /* trackable */
    String QBE = "QBE";                          /* QBE = ? */
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

    String HANDLER = "handler";                  /* Sock Handler Usage */

    String BRIDGE = "bridge";                    /* Sock Bridge Usage */
    String HOST = "host";                        /* Host */
    String PORT = "port";                        /* Port */

    String HABITUS = "habitus";                  /* Authorization header to store current logged user session data, Permission Pool */
    String DYNAMIC = "dynamic";                  /* View security of field for dynamic view name */
    String VIEW = "view";                        /* View security of view name */
    String PROFILE = "profile";                  /* */
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

    String APP = "application";                  /* Reserved: */

    String KEY = "key";                          /* Common: primary key */
    String KEY_P = "pKey";                       /* Common: argument key */
    String NAME = "name";                        /* Common: name */
    String CODE = "code";                        /* Common: code */
    String VALUE = "value";                      /* Common: value */

    String LABEL = "label";                      /* Common: label */
    String TYPE = "type";                        /* Common: type for different model */
    String DEPLOY_ID = "deployID";               /* */
    String ASPECT = "aspect";                    /* Aspect Component Usage */
    String DEPLOYMENT = "deployment";            /* */
    String CATEGORY = "category";                /* Common: category */
    String SERVICE = "service";                  /* Common: service */

    String SERVER = "server";                    /* Common: server */
    String DATA = "data";                        /* Common: data */

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


    String EPSILON = "epsilon";                  /* Dictionary Consumer */
    String FORMAT = "format";                    /* DataFormat json configuration of uniform */

    String MATRIX = "matrix";                    /* Attribute Matrix for 8 dim */
    String REFERENCE = "reference";              /* Attribute Reference for */

    String METHOD = "method";                    /* Web: http method */
    String SESSION = "session";                  /* Web: session */
    String URI = "uri";                          /* Web: http path */
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

    String PARENT_ID = "parentId";               /* Tree for parent id */
    String KEY_WORD = "keyword";                 /* Search Key Word */
    String SORT = "sort";                        /* Sort */

    String INCLUDE = "include";                  /* Auditor Pin */
    String EXCLUDE = "exclude";                  /* Auditor Pin */

    String COMMENT = "comment";                  /* Text Part: comment */
    String DESCRIPTION = "description";          /* Text Part: description */
    String REMARK = "remark";                    /* Text Part: remark */
    String REMARKS = "remarks";                  /* Text Part: remarks */

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

    // 「Specification Definition」
    interface __ {
        String METADATA = "__" + KName.METADATA;     /* __metadata for definition on modulat */
        String DATA = "__" + KName.DATA;             /* __data for previous data */
        String FLAG = "__" + KName.FLAG;             /* __flat for operation flag */

        String INPUT = "__" + KName.INPUT;           /* __input for input original data */
        String USER = "__" + KName.USER;             /* __user for user extraction */
        String REFERENCE = "__" + KName.REFERENCE;   /* __reference for dict/assist etc */
        String ACL = "__" + Flow.ACL;                /* __acl */
    }


    // 「Attachment Definition」
    interface Attachment {
        String STORE_WAY = "storeWay";               /* XAttachment, store way of the file */
        String FILE_NAME = "fileName";               /* XAttachment filename */
        String FILE_URL = "fileUrl";                 /* XAttachment fileUrl */
        String FILE_PATH = "filePath";               /* XAttachment filePath processing */

        String X = "x";
        String W = "w";
        String R = "r";
    }


    // 「Micro Service」
    interface Micro {
        String ETCD = "etcd";
    }


    // 「Application Definition」
    interface App {

        // Application
        String COPY_RIGHT = "copyRight";
        String ICP = "icp";
        String TITLE = "title";
        String EMAIL = "email";
        String LOGO = "logo";

        String DOMAIN = "domain";
        String APP_PORT = "appPort";
        String ROUTE = "route";

        String PATH = KName.PATH;
        String URL_ENTRY = "urlEntry";
        String URL_MAIN = "urlMain";

        // Modulat
        String BAG_ID = "bagId";
        String BAG = "bag";
        String BLOCK = "block";
    }


    // 「Dynamic Api/Job Definition」, I_API / I_SERVICE
    interface Api {
        String CONFIG_DATABASE = "configDatabase";

        String CONFIG_INTEGRATION = "configIntegration";

        String CHANNEL_CONFIG = "channelConfig";

        String SERVICE_CONFIG = "serviceConfig";

        String DICT_CONFIG = "dictConfig";

        String MAPPING_CONFIG = "mappingConfig";

        String DICT_EPSILON = "dictEpsilon";                 /* Origin X to store definition of Epsilon */
    }


    // 「Dynamic UI Definition」
    interface Ui {
        String CONFIG = "config";

        String CONTAINER_CONFIG = "containerConfig";

        String COMPONENT_CONFIG = "componentConfig";

        String ASSIST = "assist";

        String GRID = "grid";

        String CONTROLS = "controls";

        String CONTROL_ID = "controlId";

        String CLASS_NAME = "className";

        String PAGE = "page";
        /*
         * Form belong-to
         */
        String HIDDEN = "hidden";
        String ROW = "row";
        String INITIAL = "initial";
        String COLUMNS = "columns";
        String WINDOW = "window";
    }


    // 「Dynamic Modeling Definition」
    interface Modeling {

        String KEYS = "keys";
        String FIELDS = "fields";
        String INDEXES = "indexes";

        String SCHEMATA = "schema";
        String MODELS = "models";

        String RELATIONS = "relations";

        String JOINS = "joins";
        String ATTRIBUTES = "attributes";

        String VALUE_BEFORE = "BEFORE";
        String VALUE_AFTER = "AFTER";

        Set<String> VALUE_SET = new HashSet<>() {
            {
                this.add(VALUE_BEFORE);
                this.add(VALUE_AFTER);
            }
        };
    }


    // 「Graphic Definition」 Neo4j Part
    interface Graphic {

        String GRAPHIC = "graphic";               /* Graphic Engine needed */
        String NODE = "node";                     /* Graphic node */
        String NODES = "nodes";                   /* Graphic nodes */
        String EDGE = "edge";                     /* Graphic edge */
        String EDGES = "edges";                   /* Graphic edges */
    }


    // 「RBAC Module Usage」
    interface Rbac {
        String ROLE_ID = "roleId";
        String PERM_ID = "permId";

        // view related
        String PROJECTION = Qr.KEY_PROJECTION;
        String CRITERIA = Qr.KEY_CRITERIA;
        String ROWS = "rows";
        String POSITION = "position";


        String DM = "dm";                            /* Definition: DM Process */
        String UI = "ui";                            /* Definition: UI Process */
        String QR = "qr";                            /* Definition: */
        String SURFACE = "surface";                  /* Definition: UI Show */
        String IN = KName.IN;                        /* Definition: In Process */

        String PACK_V = "v";
        String PACK_H = "h";
        String PACK_Q = "q";
    }


    // 「Tenant Field Definition」, When you enable multi tenants environment, it will be used
    interface Tenant {
        String STELLAR = "stellar";                  /* Business */
        String TENANT = "tenant";
        String ID = "tenantId";
        String VENDORS = "vendors";
    }


    // 「Workflow Engine Definition」
    interface Flow {
        String DEFINITION_ID = "definitionId";
        String DEFINITION_KEY = "definitionKey";
        String INSTANCE_ID = "instanceId";
        String TASK_ID = "taskId";
        String FORM_KEY = "formKey";
        String BPMN = "bpmn";
        String WORKFLOW = "workflow";
        String ACL = "acl";
        // Todo
        String TASK = "task";
        String TODO = "todo";                        /* Todo */
        String DECISION = "decision";                /* Workflow: decision */
        String NODE = "node";                        /* Workflow node */
        String COMMENT_APPROVAL = "commentApproval";
        String COMMENT_REJECT = "commentReject";

        String CLOSE_CODE = "closeCode";
        String CLOSE_KB = "closeKb";
        String CLOSE_SOLUTION = "closeSolution";

        // Trace Information Data
        String TRACE_ID = "traceId";
        String TRACE_KEY = "traceKey";
        String TRACE_CODE = "traceCode";
        String TRACE_SERIAL = "traceSerial";
        String TASK_SERIAL = "taskSerial";
        String TASK_CODE = "taskCode";
        String TASK_NAME = "taskName";

        String TASK_KEY = "taskKey";

        // Flow Field
        String FLOW_END = "flowEnd";

        String FLOW_NAME = "flowName";
        String FLOW_DEFINITION_KEY = "flowDefinitionKey";
        String FLOW_DEFINITION_ID = "flowDefinitionId";
        String FLOW_INSTANCE_ID = "flowInstanceId";

        // Processing
        String CONFIG_START = "startConfig";
        String CONFIG_END = "endConfig";
        String CONFIG_RUN = "runConfig";
        String CONFIG_GENERATE = "generateConfig";
        String CONFIG_AUTHORIZED = "authorizedConfig";
        String UI_CONFIG = KName.UI_CONFIG;
        String UI_STYLE = KName.UI_STYLE;
        String UI_ASSIST = "uiAssist";
        String UI_LINKAGE = "uiLinkage";

        // Bpmn ( Belong to Workflow Engine )
        interface Bpmn {
            String CLASS = "class";
            String EVENT = "event";
        }
    }


    // 「Component Specification Definition」
    interface Component {
        // Run Component
        String RUN_COMPONENT = "runComponent";
        String RUN_CONFIG = "runConfig";
        // Tree Component
        String TREE_COMPONENT = "treeComponent";
        String TREE_CONFIG = "treeConfig";
        // Ui Component
        String UI_COMPONENT = "uiComponent";
        String UI_CONFIG = KName.UI_CONFIG;
        // Dm Component
        String DM_COMPONENT = "dmComponent";
        String DM_CONFIG = KName.DM_CONFIG;
        // Qr Component
        String QR_COMPONENT = "qrComponent";
        String QR_CONFIG = "qrConfig";
        // Web Component
        String WEB_COMPONENT = "webComponent";
        String WEB_CONFIG = "webConfig";
    }


    // 「Zero Internal Usage」
    interface Internal {
        String ERROR = "error";
        String INJECT = "inject";
        String SERVER = "server";
        String RESOLVER = "resolver";

        /**
         * Vertx Zero configuration
         */
        String ZERO = "zero";
        /**
         * External Zero configuration
         */
        String LIME = "lime";
    }


    // 「Auditor Specification Definition」
    interface Auditor {
        String FINISHED_BY = "finishedBy";
        String FINISHED_AT = "finishedAt";

        String OPEN_BY = "openBy";
        String CLOSE_BY = "closeBy";
        String CLOSE_AT = "closeAt";
        String CANCEL_BY = "cancelBy";
        String CANCEL_AT = "cancelAt";

        String OWNER = "owner";
        String SUPERVISOR = "supervisor";

        String TO_USER = "toUser";

        String TO_TEAM = "toTeam";
        String TO_DEPT = "toDept";
        String TO_ROLE = "toRole";
        String TO_GROUP = "toGroup";

        String ASSIGNED_BY = "assignedBy";
        String ASSIGNED_AT = "assignedAt";
        String ACCEPTED_BY = "acceptedBy";
        String ACCEPTED_AT = "acceptedAt";

        Set<String> USER_FIELDS = new HashSet<>() {
            {
                this.add(TO_USER);       // Approved / Processed By
                this.add(CREATED_BY);    // Created By
                this.add(UPDATED_BY);    // Updated By
                this.add(OWNER);         // Owner
                this.add(SUPERVISOR);    // Supervisor
                this.add(CANCEL_BY);     // Cancel By
                this.add(CLOSE_BY);      // Close By
                this.add(OPEN_BY);       // Open By
                this.add(FINISHED_BY);   // Finished By
                this.add(ASSIGNED_BY);   // Assigned By
                this.add(ACCEPTED_BY);   // Accdpted By
            }
        };
    }


    // 「AOP Specification Definition」
    interface Aop {
        String COMPONENT_TYPE = "configuration.operation";
        String COMPONENT = "plugin.component";
        String COMPONENT_BEFORE = "plugin.component.before";
        String COMPONENT_AFTER = "plugin.component.after";
        String COMPONENT_JOB = "plugin.component.job";
        String COMPONENT_CONFIG = "plugin.config";
    }


    // 「Date Specification Definition」
    interface Moment {
        String DATE = "date";
        String DATETIME = "datetime";
        String TIME = "time";
        String CALENDAR = "calendar";

        String SECOND = "second";
        String SECONDS = "seconds";

        String MINUTE = "minute";
        String MINUTES = "minutes";

        String HOUR = "hour";
        String HOURS = "hours";

        String DAY = "day";
        String DAYS = "days";

        String WEEK = "week";
        String WEEKS = "weeks";

        String MONTH = "month";
        String MONTHS = "months";

        String QUARTER = "quarter";
        String QUARTERS = "quarters";

        String YEAR = "year";
        String YEARS = "years";
    }
}
