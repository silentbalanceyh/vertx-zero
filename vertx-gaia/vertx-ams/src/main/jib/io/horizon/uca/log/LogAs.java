package io.horizon.uca.log;

/**
 * @author lang : 2023/5/2
 */
public interface LogAs {
    String MODULE = "Προδιαγραφή μεταδεδομένων";

    LogModule Fs = Log.modulat(MODULE).cloud("Fs");
    LogModule Boot = Log.modulat(MODULE).program("Boot");
}
