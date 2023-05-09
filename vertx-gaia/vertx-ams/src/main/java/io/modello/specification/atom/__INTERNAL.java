package io.modello.specification.atom;


import io.modello.atom.normalize.KMarkAtom;

import java.util.Set;
import java.util.concurrent.ConcurrentMap;

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