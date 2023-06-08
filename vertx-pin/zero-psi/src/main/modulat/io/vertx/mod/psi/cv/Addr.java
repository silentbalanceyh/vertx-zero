package io.vertx.mod.psi.cv;

interface Prefix {
    String _EVENT = "Αγορά, πωλήσεις και απογραφή//περιβάλλων/";
}

public interface Addr {

    String WH_CREATE = Prefix._EVENT + "X-WH/CREATE";

    String WH_UPDATE = Prefix._EVENT + "X-WH/UPDATE";

    String WH_READ = Prefix._EVENT + "X-WH/READ";

    String WH_DELETE = Prefix._EVENT + "X-WH/DELETE";
}
