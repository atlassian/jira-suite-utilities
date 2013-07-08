package com.googlecode.jsu.helpers;

/**
 * Interface for checking conditions. Used as strategy pattern.
 *
 * @author <A href="mailto:abashev at gmail dot com">Alexey Abashev</A>
 */
public interface ConditionChecker {
    /**
     * Check two values and return true if condition success or false is not.
     */
    boolean checkValues(Object value1, Object value2);
}
