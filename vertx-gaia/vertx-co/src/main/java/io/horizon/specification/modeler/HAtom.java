package io.horizon.specification.modeler;

import io.modello.atom.normalize.KMarkAtom;
import io.modello.specification.atom.HAttribute;
import io.modello.specification.atom.HReference;
import io.modello.specification.atom.HUnique;
import io.modello.specification.meta.HMetaAtom;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.json.JsonObject;

import java.util.Set;
import java.util.concurrent.ConcurrentMap;

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
    @Fluent
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

interface HAtomIo {
    /*
     * Whether current atom is trackable for activities
     */
    Boolean trackable();

    /*
     * Track:       Generate change histories
     * Confirm:     Secondary modification confirmation
     * SyncIn:      Integration Reading
     * SyncOut:     Integration Writing
     */
    KMarkAtom marker();
}

// ==================== Rule Unique Part =====================
interface HAtomRule {
    /*
     * Stored rule into database that it's configured, this rule bind
     * to model shared lower priority in each model, it means that when
     * your channel has no rule bind, this one should be the choice.
     *
     * - RuleUnique: Model Runtime
     * - RuleUnique: Channel Runtime
     */
    HUnique ruleAtom();

    /*
     * Rule seeking by following code logical
     * - Firstly, seek the rule object that's belong to current channel ( API/JOB )
     *   instead of stored in Atom
     * - When there is no bind rule, this method will be called and return to stored
     *   rule that's belong to current model ( ATOM )
     */
    HUnique ruleSmart();

    /*
     * Following method is for Channel Runtime only, it's not stored in Atom, instead
     * it's stored in `I_API / I_JOB` for different channels usage.
     */
    HUnique rule();

    <T extends HAtom> T rule(HUnique rule);
}

// ==================== Attribute Part =====================
interface HAtomAttribute {
    /*
     * Return all attribute names combined into Set
     *
     * Because of the data structure, this method is not focus on
     * sequence of attribute.
     */
    Set<String> attribute();

    HAttribute attribute(String name);

    /*
     * name = alias
     */
    ConcurrentMap<String, String> alias();

    String alias(String name);

    /*
     * name = Class<?>
     */
    ConcurrentMap<String, Class<?>> type();

    Class<?> type(String name);
}
