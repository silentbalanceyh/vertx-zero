package io.vertx.tp.workflow.uca.camunda;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Io<T> {
    /*
     * Sync Method for following three types
     * - ProcessDefinition
     * - Task
     * - HistoricProcessInstance
     *
     * The T could be
     * 1. StartEvent                    - Based: Definition
     * 2. EndEvent                      - Based: Definition
     * 3. Form ( JsonObject )           - Based: Definition / Task
     * 4. Workflow ( JsonObject )       - Based: Definition / Task
     * 5. Task                          - Based: Instance
     * 6. Activities                    - Based: History Instance
     */
}

interface IoInternal {
    // Fetch ProcessDefinition              : id -> key

    // Fetch ProcessInstance                : id

    // Fetch HistoricProcessInstance        : id
}
