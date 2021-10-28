package io.vertx.tp.fm.cv;

interface Prefix {

    String _EVENT = "Ἀτλαντὶς νῆσος://χρηματοδότηση/";
}

public interface Addr {

    interface BillItem {
        String FETCH_AGGR = Prefix._EVENT + "FETCH/AGGR";
        // Split
        String IN_SPLIT = Prefix._EVENT + "BILL/SPLIT";
    }

    interface Bill {
        // Pre + Authorize
        String IN_PRE = Prefix._EVENT + "BILL/PRE";
        // Common, Bill + Items
        String IN_COMMON = Prefix._EVENT + "BILL/COMMON";
        // Multi, Bill + n Items
        String IN_MULTI = Prefix._EVENT + "BILL/MULTI";
    }
}
