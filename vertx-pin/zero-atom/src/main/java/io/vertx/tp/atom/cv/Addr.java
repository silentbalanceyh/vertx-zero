package io.vertx.tp.atom.cv;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
interface Prefix {

    String _EVENT = "Ἀτλαντὶς νῆσος://διαμορφωτής/";
}

public interface Addr {

    interface Model {

        String MODELS = Prefix._EVENT + "X-MODEL/LIST";

        String MODEL_FIELDS = Prefix._EVENT + "X-MODEL/IDENTIFIER/FIELDS";
    }
}
