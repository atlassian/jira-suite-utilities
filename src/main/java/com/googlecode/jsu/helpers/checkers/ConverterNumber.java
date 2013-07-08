package com.googlecode.jsu.helpers.checkers;

/**
 * @author <A href="mailto:abashev at gmail dot com">Alexey Abashev</A>
 */
class ConverterNumber implements ValueConverter {
    /* (non-Javadoc)
     * @see com.googlecode.jsu.helpers.checkers.ValueConverter#getComparable(java.lang.Object)
     */
    public Comparable<?> getComparable(Object object) {
        if (object == null) {
            return null;
        }

        Double numberValue;

        if (object instanceof String) {
            final String s = (String) object;

            if (s.trim().length() == 0) {
                return null;
            }

            numberValue = Double.valueOf(s);
        } else if (object instanceof Number) {
            numberValue = ((Number) object).doubleValue();
        } else {
            throw new NumberFormatException();
        }

        return numberValue;
    }
}
