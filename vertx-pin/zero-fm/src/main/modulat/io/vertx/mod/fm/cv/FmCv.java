package io.vertx.mod.fm.cv;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface FmCv {
    String[] SEQ = new String[]{"A", "B", "C", "D", "E", "F", "G"};

    interface Status {
        String PENDING = "Pending";

        String FINISHED = "Finished";
        String INVALID = "InValid";

        String FIXED = "Fixed";
        String VALID = "Valid";
    }

    interface Type {
        String TRANSFER_FROM = "TransferFrom";
        String CANCEL = "Cancel";
    }

    interface ID {
        String SETTLEMENT_ID = "settlementId";
        String PAYMENT_ID = "paymentId";
        String PAYMENT = "payment";
    }
}
