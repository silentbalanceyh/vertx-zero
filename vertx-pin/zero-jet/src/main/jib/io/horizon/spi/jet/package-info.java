/*
 * This comments are for extension points in dynamic routing system
 * Extension 1:
 * 1. 「Worker」
 *     Yes, you can define your own worker component and configured in `workerClass` in I_API, but it's complex because
 *     You must finish all above components in the calling chain instead of some small development.
 *
 * Extension 2:
 * 2. 「Ingest」
 *     In the parameter normalization, you can define your own parameters code logical
 *     1) Set your class into `vertx-inject.yml` file with key `zero.jet.param.ingest`
 *     2) Your class must implements `io.horizon.jet.spi.JtIngest` interface
 *     3) Your class must contains public constructor without arguments ( non-arg )
 *
 * Extension 3:
 * 3. 「Consumer」
 *     Each consumer could be used by worker class and bind to address ( 1 - 1 ), here you can define your own
 *     consumer. This life cycle happened in ( agent -> eventbus -> ??? ), The ??? means consumer position in the
 *     whole request.
 *     !!!: Another thing is that if you defined consumer, you must import `zero-jet` project because the class
 *     appear in interface className = `io.vertx.mod.jet.io.vertx.up.atom.JtUri`.
 *
 * Extension 4:
 * 4. 「Channel」
 *     The channel could be defined by user with `serviceChannel` field of I_SERVICE instead of four default category
 *     ADAPTOR, DIRECTOR, ACTOR, CONNECTOR
 *
 * Extension 5:
 * 5. 「Component」
 *     The component could be defined by user with `serviceComponent` field of I_SERVICE instead of others, there are
 *     no definition of Component
 */
package io.horizon.spi.jet;