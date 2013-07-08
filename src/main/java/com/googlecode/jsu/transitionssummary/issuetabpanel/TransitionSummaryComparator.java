package com.googlecode.jsu.transitionssummary.issuetabpanel;

import java.sql.Timestamp;
import java.util.Comparator;

import com.googlecode.jsu.transitionssummary.TransitionSummary;

/**
 * Comparator for comparing trasition summary by last update timestamp.
 * @author <A href="mailto:abashev at gmail dot com">Alexey Abashev</A>
 */
final class TransitionSummaryComparator implements Comparator<TransitionSummary> {
    /* (non-Javadoc)
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(TransitionSummary summary1, TransitionSummary summary2) {
        if (summary1 == summary2) {
            return 0;
        }

        int result = (-1);

        if ((summary1 != null) && (summary1.getLastUpdate() != null)) {
            Timestamp ts = summary1.getLastUpdate();

            if (summary2 != null) {
                return ts.compareTo(summary2.getLastUpdate());
            } else {
                return ts.compareTo(null);
            }
        }

        return result;
    }
}
