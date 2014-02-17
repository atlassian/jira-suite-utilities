package com.googlecode.jsu.helpers.checkers;

import java.util.Calendar;

/**
 * @author <A href="mailto:abashev at gmail dot com">Alexey Abashev</A>
 */
class ConverterDate implements ValueConverter {
    /* (non-Javadoc)
     * @see com.googlecode.jsu.helpers.checkers.ValueConverter#getComparable(java.lang.Object)
     */
    public Comparable<?> getComparable(Object object) {
        if (object == null || "".equals(object)) {
            return null;
        }

        if (object instanceof Calendar) {
            Calendar cal = (Calendar) object;

            cal.clear(Calendar.SECOND);
            cal.clear(Calendar.MILLISECOND);

            return cal;
        }

        throw new UnsupportedOperationException("Unsupported value type " + object);
    }
}
