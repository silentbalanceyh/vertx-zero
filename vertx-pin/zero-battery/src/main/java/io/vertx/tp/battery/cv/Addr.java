package io.vertx.tp.battery.cv;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
interface Prefix {

    String _EVENT = "Ἀτλαντὶς νῆσος://μορφάτης/";
}

public interface Addr {

    interface Module {

        String FETCH = Prefix._EVENT + "X-BAG/FETCH-ALL";

        String UP_PROCESS = Prefix._EVENT + "X-BAG/PROCESS";

        String UP_AUTHORIZE = Prefix._EVENT + "X-BAG/AUTHORIZE";
    }
}
