package io.vertx.mod.jet.cv;

interface Prefix {
    String _EVENT = "Ἀτλαντὶς νῆσος://Πίδακας δρομολογητή/";
}

public interface JtAddr {

    interface Job {

        String START = Prefix._EVENT + "I-JOB/START";

        String STOP = Prefix._EVENT + "I-JOB/STOP";

        String RESUME = Prefix._EVENT + "I-JOB/RESUME";

        String STATUS = Prefix._EVENT + "I-JOB/STATUS";

        String BY_SIGMA = Prefix._EVENT + "I-JOB/BY/SIGMA";

        String GET_BY_KEY = Prefix._EVENT + "I-JOB/GET/BY/KEY";

        String UPDATE_BY_KEY = Prefix._EVENT + "I-JOB/UPDATE/BY/KEY";
    }

    interface Aeon {

        String NEW_ROUTE = Prefix._EVENT + "X-API/ROUTE/NEW";
    }
}
