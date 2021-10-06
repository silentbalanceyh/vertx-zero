package io.vertx.up.eon;

import io.vertx.up.atom.query.engine.Qr;

import java.util.HashSet;
import java.util.Set;

public interface KName {
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

    String MODEL_ID = "modelId";                 /* Model Consumer ( identifier ) field */
    String MODEL_KEY = "modelKey";               /* Model Consumer ( key ) field */
    String MODEL_CATEGORY = "modelCategory";     /* Model Consumer ( related XCategory ) field */

    String SCOPE = "scope";                      /* OAuth scope field */
    String REALM = "realm";                      /* Authorization realm field for security */
    String GRANT_TYPE = "grantType";             /* OAuth grant type field */
    String RESOURCE_ID = "resourceId";           /* Security Action related resource field */

    String HABITUS = "habitus";                  /* Authorization header to store current logged user session data, Permission Pool */
    String DYNAMIC = "dynamic";                  /* View security of field for dynamic view name */
    String VIEW = "view";                        /* View security of view name */
    String PROFILE = "profile";                  /* */
    String POSITION = "position";                /* View security of view position */
    String MODULE = "module";                    /* View sub-module picking up */

    String ROLE = "role";                        /* Security Object: role field */
    String ROLE_ID = "roleId";                   /* Security Object: role id ( X_ROLE key ) field */
    String USER = "user";                        /* Security Object: user field */
    String USER_ID = "userId";                   /* Security Object: user id ( X_USER key ) field */
    String USERNAME = "username";                /* Security Object: user name ( X_USER username) field*/
    String REAL_NAME = "realname";               /* Security Object: user real name field */
    String GROUP = "group";                      /* Security Object: group */
    String ALIAS = "alias";                      /* Security Object: another name for current */
    String PASSWORD = "password";                /* Security Object: Password belong to field of security framework, ( X_USER password ) field */
    String EMAIL = "email";                      /* Security Object: user email ( X_USER email ) field */
    String MOBILE = "mobile";                    /* Security Object: user mobile ( X_USER mobile ) field */
    String CLIENT_ID = "clientId";               /* Security Object: OAuth user `clientId` field, mapped to X_USER key */

    String ACTOR = "actor";                      /* Dynamic channel for module definition, mapped to X_MODULE */

    String ITEMS = "items";                      /* Batch operation, items -> JsonArray ( element = JsonObject ) */
    String KEYS = "keys";                        /* Batch operation, keys -> JsonArray ( element = String ) */
    String CODES = "codes";                      /* Batch operation, codes -> JsonArray ( element = String ) */

    String FILE_KEY = "fileKey";                 /* XAttachment belong-to field */
    String FILE_NAME = "filename";               /* XAttachment filename */
    String DATA_KEY = "dataKey";                 /* Security belong-to field: Authorization data stored key for session storage */

    String APP_KEY = "appKey";                   /* XHeader for X-App-Key */
    String APP_ID = "appId";                     /* XHeader for X-App-Id */
    String SIGMA = "sigma";                      /* XHeader for X-Sigma */

    String DEBUG = "debug";                      /* Development: for debugging */

    String APP = "application";                  /* Reserved: */

    String KEY = "key";                          /* Common: primary key */
    String NAME = "name";                        /* Common: name */
    String CODE = "code";                        /* Common: code */
    String VALUE = "value";                      /* Common: value */
    String TYPE = "type";                        /* Common: type for different model */
    String DEPLOY_ID = "deployID";               /* */
    String DEPLOYMENT = "deployment";            /* */
    String CATEGORY = "category";                /* Common: category */
    String SERVICE = "service";                  /* Common: service */
    String DATA = "data";                        /* Common: data */
    String DATUM = "datum";                      /* Common: metadata key */
    String MAPPING = "mapping";                  /* Common: Json mapping configuration */
    String STATUS = "status";                    /* Common: status for different workflow */
    String SERIAL = "serial";                    /* Common: serial field ( XNumber related or other meaningful serial */

    String METADATA = "metadata";                /* Shared: metadata for most table of METADATA ( JsonObject ) field */
    String ACTIVE = "active";                    /* Shared: active field for most table of ACTIVE ( Boolean ) field */
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

    String EPSILON = "epsilon";                  /* Dictionary Consumer */
    String FORMAT = "format";                    /* DataFormat json configuration of uniform */

    String METHOD = "method";                    /* Web: http method */
    String URI = "uri";                          /* Web: http path */
    String URI_IMPACT = "impactUri";             /* Web: http impact uri */
    String URI_REQUEST = "requestUri";           /* Web: http path ( normalized ) contains path such as `/api/:code/name` instead of actual */
    String RESULT = "result";                    /* Web: http response */
    String HEADER = "header";                    /* Web: http header */


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
    String TEAM_ID = "teamId";                   /* Team Id */
    String WORK_NUMBER = "workNumber";           /* Work Number */

    String CONFIG = "config";                    /* Acl Usage */
    String PHASE = "phase";                      /* Acl Phase */
    String SEEKER = "seeker";                    /* Acl Seeker */
    String SYNTAX = "syntax";                    /* Acl Syntax */
    String VIEW_ID = "viewId";                   /* View Id */
    String PERMISSION_ID = "permissionId";       /* Security: permissionId */
    String ACTIONS = "actions";                  /* Security: actions */

    String DAO = "dao";                          /* Dao field in json configuration */
    String CONNECT = "connect";                  /* Connect field in json configuration */
    String PLUGIN_IO = "plugin.io";              /* Attribute Plugin for "io" of source config */
    String TIMER = "timer";                      /* Timer in Job */

    String LIST = "list";                        /* Page Data */
    String COUNT = "count";                      /* Page Count */

    String AT = "at";                            /* at field */
    String BY = "by";                            /* by field */

    String ACCESS_TOKEN = "access_token";        /* token cv from `jwt` to `access_token` */

    interface Micro {
        String ETCD = "etcd";
    }

    /*
     * X_APP
     */
    interface App {

        String COPY_RIGHT = "copyRight";
        String ICP = "icp";
        String TITLE = "title";
        String EMAIL = "email";
        String LOGO = "logo";

        String DOMAIN = "domain";
        String APP_PORT = "appPort";
        String ROUTE = "route";

        String PATH = "path";
        String URL_ENTRY = "urlEntry";
        String URL_MAIN = "urlMain";
    }

    /*
     * I_API / I_SERVICE
     */
    interface Api {
        String CONFIG_DATABASE = "configDatabase";

        String CONFIG_INTEGRATION = "configIntegration";

        String CHANNEL_CONFIG = "channelConfig";

        String SERVICE_CONFIG = "serviceConfig";

        String DICT_CONFIG = "dictConfig";

        String MAPPING_CONFIG = "mappingConfig";

        String DICT_EPSILON = "dictEpsilon";                 /* Origin X to store definition of Epsilon */
    }

    /*
     * UI_* table definition
     */
    interface Ui {
        String CONFIG = "config";

        String CONTAINER_CONFIG = "containerConfig";

        String COMPONENT_CONFIG = "componentConfig";

        String ASSIST = "assist";

        String GRID = "grid";

        String CONTROLS = "controls";

        String CONTROL_ID = "controlId";

        String CLASS_NAME = "className";
        /*
         * Form belong-to
         */
        String HIDDEN = "hidden";
        String ROW = "row";
        String INITIAL = "initial";
        String COLUMNS = "columns";
        String WINDOW = "window";
    }

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

        Set<String> VALUE_SET = new HashSet<String>() {
            {
                this.add(VALUE_BEFORE);
                this.add(VALUE_AFTER);
            }
        };
    }

    interface Graphic {

        String GRAPHIC = "graphic";               /* Graphic Engine needed */
        String NODE = "node";                     /* Graphic node */
        String NODES = "nodes";                   /* Graphic nodes */
        String EDGE = "edge";                     /* Graphic edge */
        String EDGES = "edges";                   /* Graphic edges */
    }

    interface Rbac {
        String ROLE_ID = "roleId";
        String PERM_ID = "permId";

        // view related
        String PROJECTION = Qr.KEY_PROJECTION;
        String CRITERIA = Qr.KEY_CRITERIA;
        String ROWS = "rows";
        String POSITION = "position";
    }

}
