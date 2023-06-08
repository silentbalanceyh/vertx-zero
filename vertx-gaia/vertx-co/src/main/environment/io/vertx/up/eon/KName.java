package io.vertx.up.eon;

import io.horizon.eon.VName;
import io.horizon.uca.qr.syntax.Ir;

import java.util.HashSet;
import java.util.Set;

public interface KName extends VName {

    // 「Specification Definition」
    interface __ extends VName.__ {

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

    // 「EmUca」Life Cycle
    interface LifeCycle {
        String CONFIGURE = "configure";              /* Setup for configuration */
        String COMPILE = "compile";                  /* After `configure`, processing the compile with data */

        String SYNCHRO = "synchro";                  /* Update when usage */
    }


    // 「EmApp Definition」
    interface App {

        // EmApp
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

        String BAGS = "bags";
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
        String CONTROL = "control";

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
        String PROJECTION = Ir.KEY_PROJECTION;
        String CRITERIA = Ir.KEY_CRITERIA;
        String CREDIT = "credit";

        String ROWS = "rows";
        String POSITION = "position";


        String DM = "dm";                            /* Definition: DM Process */
        String UI = "ui";                            /* Definition: UI Process */
        String QR = "qr";                            /* Definition: */
        String IN = KName.IN;                        /* Definition: In Process */

        String PACK_V = "v";
        String PACK_H = "h";
        String PACK_Q = "q";

        String SURFACE = "surface";                  /* Definition: UI Show */
        String WEB_UI = "webUi";
        String VISITANT = "visitant";
        String SEEK_KEY = "seekKey";
        String ACL = "acl";
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
            String EVENT = KName.EVENT;
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
