package io.vertx.mod.tpl.cv;

interface Prefix {

    String _EVENT = "Ἀτλαντὶς νῆσος://Πρότυπο/";
}

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Addr {
    interface Menu {

        String MY_FETCH = Prefix._EVENT + "X-MENU/MY/FETCH";

        String MY_SAVE = Prefix._EVENT + "X-MENU/MY/SAVE";
    }
}
