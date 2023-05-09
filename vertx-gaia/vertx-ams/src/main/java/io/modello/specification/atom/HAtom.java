package io.modello.specification.atom;

import io.modello.specification.meta.HMetaAtom;
import io.vertx.core.json.JsonObject;

/**
 * New structure for atom modeling for
 * - Zero Core Framework
 * - Zero Extension Framework
 *
 * Here provide abstract interface to define model
 *
 * The model include following types
 * - Dynamic Model:
 * --- Built by `M_X` ( zero-atom ) extension module, the child class will be converted typed named DataAtom
 * - Static Model:
 * --- Jooq Generated ( Pojo/Dao ) module, the standard module of jooq in zero framework
 * - Integration Model:
 * --- When to do integration job with third part, you can
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface HAtom extends
    HDiff,                  // Compared Part
    HAtomAttribute,         // Attribute Part
    HAtomRule,              // Rule Unique Part
    HAtomIo                 // Boolean Attribute Part Extracting
{

    /*
     * Create new atom based on current, the atom object must provide
     * - appName ( the same namespace )
     * - identifier ( Input new identifier )
     *
     * It means that the method could create new
     */
    HAtom atom(String identifier);

    // =================== Cross Method between two atoms ===========
    /*
     * The atomKey is calculated by
     * identifier + options ( Hash Code )
     *
     * To avoid duplicated creating atom reference
     */
    String atomKey(JsonObject options);

    HMetaAtom shape();

    <T extends HModel> T model();
    // =================== Basic method of current atom ===========

    String identifier();

    String sigma();

    String language();

    // ==================== Reference Information ================
    HReference reference();
}
