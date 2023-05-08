package io.modello.eon;

import io.horizon.eon.VName;

/**
 * @author lang : 2023-05-08
 */
public interface VEmf {
    // Common Name
    String V_NAME = VName.NAME;           // name
    String V_VALUE = VName.VALUE;         // value
    String V_LITERAL = VName.LITERAL;     // literal

    // Common EMF Name
    String E_TYPE = "eType";
    String E_OPPOSITE = "eOpposite";

    // Common EMF Node
    String N_LITERALS = "eLiterals";

    // ns prefix
    String NS_URI = "nsURI";              // nsURI
    String NS_PREFIX = "nsPrefix";        // nsPrefix
}
