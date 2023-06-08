package io.vertx.mod.lbs.cv;

interface Prefix {

    String _EVENT = "Ἀτλαντὶς νῆσος://Διεύθυνση/";
}

public interface Addr {
    /*
     * Interface for list selector only
     */
    interface PickUp {
        String REGION_META = Prefix._EVENT + "L-REGION/META";

        String REGION_BY_CITY = Prefix._EVENT + "L-REGION/BY-CITY";

        String CITY_BY_STATE = Prefix._EVENT + "L-CITY/BY-STATE";

        String STATE_BY_COUNTRY = Prefix._EVENT + "L-STATE/BY-COUNTRY";

        String COUNTRIES = Prefix._EVENT + "L-COUNTRIES";

        String TENT_BY_SIGMA = Prefix._EVENT + "L-TENT/BY-SIGMA";

        String FLOOR_BY_SIGMA = Prefix._EVENT + "L-FLOOR/BY-SIGMA";
    }
}
