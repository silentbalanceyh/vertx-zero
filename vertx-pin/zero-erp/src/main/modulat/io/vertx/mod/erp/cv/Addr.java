package io.vertx.mod.erp.cv;

interface Prefix {

    String _EVENT = "Ἀτλαντὶς νῆσος://Επιχείρηση/";
}

public interface Addr {

    interface Company {
        String INFORMATION = Prefix._EVENT + "X-COMPANY";
    }

    interface Employee {
        String ADD = Prefix._EVENT + "E-EMPLOYEE/ADD";

        String BY_ID = Prefix._EVENT + "E-EMPLOYEE/BY-ID";

        String EDIT = Prefix._EVENT + "E-EMPLOYEE/EDIT";

        String DELETE = Prefix._EVENT + "E-EMPLOYEE/DELETE";
    }
}
