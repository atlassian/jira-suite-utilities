package com.googlecode.jsu.helpers.checkers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.jsu.helpers.ConditionChecker;

/**
 * @author <A href="mailto:abashev at gmail dot com">Alexey Abashev</A>
 */
class CheckerComposite implements ConditionChecker {
    private final Logger log = LoggerFactory.getLogger(CheckerComposite.class);

    private final ValueConverter valueConverter;
    private final ComparingSnipet comparingSnipet;

    public CheckerComposite(
            ValueConverter valueConverter, ComparingSnipet comparingSnipet
    ) {
        this.valueConverter = valueConverter;
        this.comparingSnipet = comparingSnipet;
    }

    /* (non-Javadoc)
     * @see com.googlecode.jsu.helpers.ConditionChecker#checkValues(java.lang.Object, java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    public final boolean checkValues(Object value1, Object value2) {
        final Comparable<Comparable<?>> comp1;
        final Comparable<?> comp2;

        try {
            comp1 = (Comparable<Comparable<?>>) valueConverter.getComparable(value1);
        } catch (NumberFormatException e) {
            log.warn("Wrong number format at [" + value1 + "]");

            return false;
        } catch (Exception e) {
            log.warn("Unable to get comparable from [" + value1 + "]", e);

            return false;
        }

        try {
            comp2 = valueConverter.getComparable(value2);
        } catch (NumberFormatException e) {
            log.warn("Wrong number format at [" + value2 + "]");

            return false;
        } catch (Exception e) {
            log.warn("Unable to get comparable from [" + value2 + "]", e);

            return false;
        }

        boolean result = comparingSnipet.compareObjects(comp1, comp2);

        if (log.isDebugEnabled()) {
            log.debug("Compare values [" + comp1 + "] and [" + comp2 + "] with result [" + result + "]");
        }

        return result;
    }
}
