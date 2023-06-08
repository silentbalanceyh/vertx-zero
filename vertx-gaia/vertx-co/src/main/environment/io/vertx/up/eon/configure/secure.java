package io.vertx.up.eon.configure;

import io.horizon.eon.VName;

/**
 * @author lang : 2023-05-29
 */
interface YmlSecure {
    String OPTIONS = VName.OPTIONS;
    String PROVIDER = "provider";

    interface provider {
        String AUTHENTICATE = "authenticate";
        String AUTHORIZATION = "authorization";
    }

    interface options {
        String REALM = "realm";
    }

    interface jwt {

        interface options extends YmlCore.secure.options {
            String JWT_OPTIONS = "jwtOptions";
            String KEY_STORE = "keyStore";
        }
    }

    interface digest {
        interface options extends YmlCore.secure.options {
            String FILENAME = "filename";
        }
    }

    interface oauth2 {
        interface options extends YmlCore.secure.options {
            String CALLBACK = "callback";
        }
    }
}

interface YmlCors {
    String __KEY = "cors";
    String CREDENTIALS = "credentials";
    String METHODS = "methods";
    String HEADERS = "headers";
    String ORIGIN = "origin";
}
