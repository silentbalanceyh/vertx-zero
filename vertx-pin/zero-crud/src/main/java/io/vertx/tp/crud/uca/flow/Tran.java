package io.vertx.tp.crud.uca.flow;

/**
 * The interface for module selection
 * Phase 1: Combine input request into IxOpt and pass to IxPanel
 *
 * 1) Combine input ( Envelop, Body, Module ) three format
 * 2) Calculate the result to IxOpt
 *
 * > This component will be called by IxOpt internal
 *
 *
 * 「Sequence」
 * Phase 2: For sequence only
 *
 * 1) Execute `active` function
 * 2) Pass the result of `active` into `standBy` ( Tran Component )
 * 3) Execute `standBy` function
 *
 * Phase 3: Response building
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Tran {
    /*
     * Phase 1
     */
}
