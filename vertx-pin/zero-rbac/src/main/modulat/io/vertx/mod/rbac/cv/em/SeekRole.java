package io.vertx.mod.rbac.cv.em;

/*
 * Profile - Resource compared, 2 categories:
 * 1) User : Single Profile
 * 2) Group : Multi Profile
 *
 * All the role permission should be:
 *
 * 「 Role - Permission 」
 *     R1 - P1, P2, P3
 *     R2 - P2, P4
 *     R3 - P1, P4, P5, P6
 *     R4 - P2, P5, P8
 *     R5 - P4, P7
 *
 * 【 User 】For user category authority: ( User - Role )
 *
 * Single role related, range calculation is the same for these three types, but for
 * multi roles they are different.
 *
 * `Xor` could not be support because it often limit user to access resource and it's not common used.
 *
 * > High Priority ( H )
 * > Middle Priority ( M )
 * > Low Priority ( L )
 *
 * For example:
 * 「 Role - User 」
 *     U - R1 - H
 *     U - R2 - L
 *
 * For Group, it's the same as each user instead,
 * The role searching process is the same between User / Group, you can refer
 * role looking up based on user as group entity.
 */
public enum SeekRole {
    /*
     * Default situation ( R1, R2 ) union calculation.
     *
     * -->     P1, P2, P3, P4
     */
    UNION,
    /*
     * Priority select ( R1, R2 ), select high priority role ( Only 1 available )
     *
     * -->     P1, P2, P3
     */
    EAGER,
    /*
     * Priority select ( R1, R2 ), select low priority role ( Only 1 available )
     *
     * -->     P2, P4
     */
    LAZY,
    /*
     * Intersect select ( R1, R2 ), select shared part of two roles, high limitation.
     *
     * -->     P2
     */
    INTERSECT,
}
