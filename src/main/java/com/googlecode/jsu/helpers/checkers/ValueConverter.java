package com.googlecode.jsu.helpers.checkers;

/**
 * @author <A href="mailto:abashev at gmail dot com">Alexey Abashev</A>
 */
interface ValueConverter {
    /**
     * Get comparable value from object.
     */
    Comparable<?> getComparable(Object object);
}
