package com.googlecode.jsu.helpers;

/**
 * Simple wrapper around a duration in milliseconds. Used for {@link com.googlecode.jsu.customfields.TimeInSourceStatusCFType}
 * and {@link com.googlecode.jsu.transitionssummary.TransitionSummary}.
 */
public class FormattableDuration {
    private final long millis;

    public FormattableDuration(final long millis) {
        this.millis = millis;
    }

    public FormattableDuration(final String text) {
        if(text!=null) {
            millis = Long.parseLong(text);
        } else {
            millis = 0;
        }
    }

    public long getMillis() {
        return millis;
    }

    public String getFormatted() {
        return getFormatted(millis);
    }

    /**
     * @return a nice String format of the duration.
     */
    public static String getFormatted(long duration) {
        String retVal;

        if(duration!=0){
            Long days = duration / 86400000;
            Long restDay = duration % 86400000;

            Long hours = restDay / 3600000;
            Long resthours = restDay % 3600000;

            Long minutes = resthours / 60000;
            Long restMinutes = resthours % 60000;

            Long seconds = restMinutes / 1000;

            // If it has been days, it does not have sense to show the seconds.
            retVal = days.equals(new Long("0"))?"":String.valueOf(days) + "d ";
            retVal = retVal + (hours.equals(new Long("0"))?"":String.valueOf(hours) + "h ");
            retVal = retVal + (minutes.equals(new Long("0"))?"":String.valueOf(minutes) + "m ");
            if((days.equals(new Long("0"))) && (hours.equals(new Long("0")))){
                retVal = retVal + (seconds.equals(new Long("0"))?"":String.valueOf(seconds) + "s");
            }

        }else{
            retVal = "0s";
        }

        return retVal;
    }

    @Override
    public String toString() {
        return Long.toString(millis);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FormattableDuration that = (FormattableDuration) o;

        if (millis != that.millis) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (millis ^ (millis >>> 32));
    }
}
