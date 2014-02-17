package com.googlecode.jsu.helpers.checkers;

import java.util.Calendar;

/**
 * @author <A href="mailto:abashev at gmail dot com">Alexey Abashev</A>
 */
class ConverterDateWithoutTime extends ConverterDate {
    /* (non-Javadoc)
     * @see com.googlecode.jsu.helpers.checkers.ConverterDate#getComparable(java.lang.Object)
     */
    @Override
    public Comparable<?> getComparable(Object object) {
        if (object == null || "".equals(object)) {
            return null;
        }

        Calendar cal = (Calendar) super.getComparable(object);

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal;
    }
}
