package io.vertx.up.eon.configure;

/**
 * @author lang : 2023-05-29
 */
interface YmlShell {
    String __KEY = "shell";
    String DEBUG = "debug";
    String WELCOME = "welcome";
    String COMMANDS = "commands";
    String VALIDATE = "validate";

    /**
     * config dev
     * config
     * start
     */
    interface boot {
        String CONFIG = "config";
        String START = "start";

        interface config {
            String DEV = "dev";
        }
    }

    interface validate {
        String INPUT = "input";
        String ARGS = "args";

        interface input {
            String REQUIRED = "required";
            String EXISTING = "existing";
        }

        interface args {
            String START = "start";
            String CONFIG = "config";
        }
    }

    interface commands {
        String DEFAULT = "default";
        String DEFINED = "defined";
    }

    interface welcome {
        String BANNER = "banner";
        String VERSION = "version";
        String MESSAGE = "message";

        interface message {
            String ENVIRONMENT = "environment";
            String WAIT = "wait";
            String QUIT = "quit";
            String BACK = "back";
            String HEADER = "header";
            String HELP = "help";
            String FOOTER = "footer";
            String EMPTY = "empty";
            String INVALID = "invalid";
            String PREVIOUS = "previous";
            String USAGE = "usage";
            String ERROR = "error";
        }
    }
}
