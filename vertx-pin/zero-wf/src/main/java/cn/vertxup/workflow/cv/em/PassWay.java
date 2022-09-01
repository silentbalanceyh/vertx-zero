package cn.vertxup.workflow.cv.em;

/**
 * The way of current request here. in current version the workflow aisle way means
 * four modes.
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public enum PassWay {
    /**
     * Fork/Join Mode,
     * ( n: 1 )
     *
     * - 1) n means the next node will be `n` types.
     * - 2) 1 means each type contains `1` task.
     *
     *
     *
     * The request data:
     *
     * ```json
     * <pre><code class="json">
     * {
     *     "toUser": {
     *         "type1": "user1",
     *         "type2": "user2",
     *         "type3": "user3",
     *         "...":   "...",
     *         "typeN": "userN"
     *     }
     * }
     * </code></pre>
     * ```
     */
    Fork,       // Fork/Join


    /**
     * Multi Mode,
     * ( 1: n )
     *
     * - 1) 1 means the next node is unique ( only one ), should be `1` type.
     * - 2) n means each type contains `n` tasks.
     *
     *
     *
     * The request data:
     *
     * ```json
     * <pre><code class="json">
     * {
     *     "toUser": [
     *         "user1",
     *         "user2",
     *         "user3",
     *         "...",
     *         "userN"
     *     ]
     * }
     * </code></pre>
     * ```
     */
    Multi,      // Multi
    /**
     * Standard Mode,
     * ( 1: 1 )
     *
     * - 1) 1 means the next node is unique ( only one ), should be `1` type.
     * - 2) 1 means each type contains `1` task.
     *
     *
     *
     * The request data:
     *
     * ```json
     * <pre><code class="json">
     * {
     *     "toUser": "user1"
     * }
     * </code></pre>
     * ```
     */
    Standard,   // Standard ( Default )
    /**
     * Grid Mode,
     * ( n: n )
     *
     * - 1) n means the next node will be `n` types.
     * - 2) n means each type contains `n` tasks.
     *
     *
     *
     * The request data:
     *
     * ```json
     * <pre><code class="json">
     * {
     *     "toUser": {
     *         "type1": [
     *              "user1",
     *              "user2",
     *              "..."
     *         ],
     *         "type2": [
     *              "user3",
     *              "user4",
     *              "...",
     *              "userY"
     *         ],
     *         "...": [
     *              "...",
     *              "userN"
     *         ],
     *         "typeN": [
     *              "user2",
     *              "user3",
     *              "...",
     *              "userX"
     *         ],
     *     }
     * }
     * </code></pre>
     * ```
     */
    Grid,       // Grid
}
