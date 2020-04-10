/**
 * This package defined following features
 * 1. Annotation scanning for all classes in java reflection.
 * 2. Package scanning for all classes in java reflection.
 * 3. Multi-thread metadata scanning for all classes.
 * All the scanned classes will be stored into concurrent map in memory, this action happened in startup
 * process only. It's one time process and most map will store scanned method references into memory map.
 * There are some important annotations that will be used in zero.
 * All the annotations have been defined in {@link io.vertx.up.annotations} package
 */
package io.vertx.up.runtime;