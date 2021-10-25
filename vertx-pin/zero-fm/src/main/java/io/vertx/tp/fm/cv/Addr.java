package io.vertx.tp.fm.cv;

interface Prefix {

    String _EVENT = "Ἀτλαντὶς νῆσος://χρηματοδότηση/";
}

public interface Addr {

    interface BillItem {
        String FETCH_AGGR = Prefix._EVENT + "FETCH/AGGR";
    }

    interface Bill {
        // Pre + Authorize
        String IN_PRE = Prefix._EVENT + "BILL/PRE";
    }
}
