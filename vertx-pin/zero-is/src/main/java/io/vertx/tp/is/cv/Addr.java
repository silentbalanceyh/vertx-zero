package io.vertx.tp.is.cv;

interface Prefix {

    String _EVENT = "Ἀτλαντὶς νῆσος://Ολοκλήρωση/";
}

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Addr {

    interface Directory {

        String ADD = Prefix._EVENT + "I-DIRECTORY/ADD";

        String UPDATE = Prefix._EVENT + "I-DIRECTORY/UPDATE";

        String DELETE = Prefix._EVENT + "I-DIRECTORY/DELETE";
    }
}
