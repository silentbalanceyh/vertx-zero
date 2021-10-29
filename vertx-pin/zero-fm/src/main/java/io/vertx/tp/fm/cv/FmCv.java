package io.vertx.tp.fm.cv;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface FmCv {
    String[] SEQ = new String[]{"A", "B", "C", "D", "E", "F", "G"};

    interface Status {
        String PENDING = "Pending";
        String FINISHED = "Finished";
        String INVALID = "InValid";
    }

    interface Type {
        String TRANSFER_FROM = "TransferFrom";
    }
}
