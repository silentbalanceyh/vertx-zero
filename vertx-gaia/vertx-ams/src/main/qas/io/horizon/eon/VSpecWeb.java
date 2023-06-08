package io.horizon.eon;

/**
 * @author lang : 2023-05-29
 */
interface VSpecWeb {
    String CONFIGURATION = "configuration";
    String INIT = "init";
    String RUNTIME = "runtime";
    String PLUGIN = "plugin";
    // OSGI：OSGI部分全部采用复数形式
    String PLUGINS = "plugins";
    String FEATURES = "features";
    String EXTENSIONS = "extensions";

    interface runtime {
        // runtime/cache
        String CACHE = RUNTIME + "/cache";
        // runtime/log
        String LOG = RUNTIME + "/log";
    }

    interface init {
        // init/oob
        String OOB = INIT + "/oob";

        interface oob {
            // init/oob/secure
            String SECURE = OOB + "/secure";
            // init/oob/environment
            String ENVIRONMENT = OOB + "/environment";
            // init/oob/navigation
            String NAVIGATION = OOB + "/navigation";
        }
    }

    interface configuration {
        // configuration/library
        String LIBRARY = CONFIGURATION + "/library";
        // configuration/editor
        String EDITOR = CONFIGURATION + "/editor";

        interface library {
            // configuration/library/system
            String SYSTEM = LIBRARY + "/system";
            // configuration/library/internal
            String INTERNAL = LIBRARY + "/internal";
            // configuration/library/external
            String EXTERNAL = LIBRARY + "/external";
        }

        interface editor {
            // configuration/editor/internal
            String INTERNAL = EDITOR + "/internal";
            // configuration/editor/external
            String EXTERNAL = EDITOR + "/external";
        }
    }
}
