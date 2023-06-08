package io.horizon.eon;

import io.horizon.util.HUt;

interface VSpecBundle {
    String META_INF = "META-INF";

    String PLUGIN_XML = "plugin.xml";

    String PLUGIN_PROPERTIES = "plugin.properties";

    String LIB = "lib";

    String INIT = "init";
    String MODELER = "modeler";
    String SCHEMA = "schema";
    String BACKEND = "backend";
    String FRONTEND = "frontend";

    interface frontend {
        // frontend/assembly
        String ASSEMBLY = FRONTEND + "/assembly";
        // frontend/cab
        String CAB = FRONTEND + "/cab";
        // frontend/scripts
        String SCRIPTS = FRONTEND + "/scripts";
        // frontend/skin
        String SKIN = FRONTEND + "/skin";

        // frontend/images
        String IMAGES = FRONTEND + "/images";

        // frontend/components
        String COMPONENTS = FRONTEND + "/components";

        interface components {

        }

        interface images {
            // frontend/images/icon
            String ICON = IMAGES + "/icon";
        }

        interface skin {
            // frontend/skin/{name}
            static String of(final String name) {
                return HUt.fromMessage(SKIN + "/{}", name);
            }
        }

        interface scripts {
            // frontend/scripts/{type}
            static String of(final String type) {
                return HUt.fromMessage(SCRIPTS + "/{}", type);
            }
        }

        interface cab {
            // frontend/cab/{language}
            static String of(final String language) {
                return HUt.fromMessage(CAB + "/{}", language);
            }
        }
    }

    interface backend {
        // backend/scripts
        String SCRIPTS = BACKEND + "/scripts";
        // backend/endpoint
        String ENDPOINT = BACKEND + "/endpoint";
        // backend/webapp
        String WEBAPP = BACKEND + "/webapp";
        // backend/components
        String COMPONENTS = BACKEND + "/components";

        interface components {
            // backend/components/task
            String TASK = COMPONENTS + "/task";
            // backend/components/handler
            String HANDLER = COMPONENTS + "/handler";
            // backend/components/event
            String EVENT = COMPONENTS + "/event";
            // backend/components/validator
            String VALIDATOR = COMPONENTS + "/validator";
            // backend/components/integration
            String INTEGRATION = COMPONENTS + "/integration";
        }

        interface webapp {
            String WEB_INF = WEBAPP + "/WEB-INF";
        }

        interface endpoint {
            // backend/endpoint/api
            String API = ENDPOINT + "/api";
            // backend/endpoint/ipc
            String IPC = ENDPOINT + "/ipc";
            // backend/endpoint/rpc
            String RPC = ENDPOINT + "/rpc";
            // backend/endpoint/web-socket
            String WEB_SOCKET = ENDPOINT + "/web-socket";
            // backend/endpoint/service-bus
            String SERVICE_BUS = ENDPOINT + "/service-bus";
        }

        interface scripts {
            // backend/scripts/{type}
            static String of(final String type) {
                return HUt.fromMessage(SCRIPTS + "/{}", type);
            }
        }
    }

    interface init {
        // init/modeler
        String MODELER = INIT + "/modeler";
        // init/store
        String STORE = INIT + "/store";
        // init/cloud
        String CLOUD = INIT + "/cloud";
        // init/development
        String DEVELOPMENT = INIT + "/development";

        String OOB = INIT + "/oob";

        interface oob {
            String RESOURCE = OOB + "/resource";
        }

        interface store {
            // init/store/ddl
            String DDL = STORE + "/ddl";
        }
    }

    interface modeler {
        String EMF = MODELER + "/emf";

        String ATOM = MODELER + "/atom";

        interface atom {
            String META = ATOM + "/meta";
            String REFERENCE = ATOM + "/reference"; // 不绑定，无子配置
            String RULE = ATOM + "/rule";   // 不绑定，无子配置

            static String meta_json(final String identifier) {
                return HUt.fromMessage(META + "/{}.json", identifier);
            }
        }
    }

    interface schema {
        // schema/xsd
        String XSD = SCHEMA + "/xsd";
        // schema/dtd
        String DTD = SCHEMA + "/dtd";
    }

    interface lib {
        // lib/extension
        String EXTENSION = LIB + "/extension";
    }

    interface meta_inf {
        // META-INF/MANIFEST.MF
        String MANIFEST_MF = META_INF + "/MANIFEST.MF";
        String NATIVE = META_INF + "native";
        String SERVICES = META_INF + "services";
    }
}
