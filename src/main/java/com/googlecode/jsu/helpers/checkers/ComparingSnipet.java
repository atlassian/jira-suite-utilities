package com.googlecode.jsu.helpers.checkers;

/**
 * @author <A href="mailto:abashev at gmail dot com">Alexey Abashev</A>
 */
interface ComparingSnipet {
    /**
     * Execute comparing action for objects.
     */
    boolean compareObjects(Comparable<Comparable<?>> comp1, Comparable<?> comp2);
}
