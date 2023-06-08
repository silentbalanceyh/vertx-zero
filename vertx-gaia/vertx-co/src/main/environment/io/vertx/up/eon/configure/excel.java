package io.vertx.up.eon.configure;

/**
 * @author lang : 2023-05-29
 */
interface YmlExcel {
    String PEN = "pen";
    String ENVIRONMENT = "environment";
    String TEMP = "temp";
    String TENANT = "tenant";
    String MAPPING = "mapping";

    interface environment {
        String NAME = "name";
        String PATH = "path";
        String ALIAS = "alias";
    }

    interface mapping {
        String POJO_FILE = "pojoFile";
        String DAO = "dao";
        String KEY = "key";
        String POJO = "pojo";
        String UNIQUE = "unique";
        String TABLE = "table";
    }
}
