package com.googlecode.jsu.transitionssummary;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.atlassian.jira.datetime.DateTimeFormatter;
import com.googlecode.jsu.helpers.FormattableDuration;

/**
 * @author Gustavo Martin
 *
 * This class represents the summary of a set of Transitions.
 *
 * Allowing to obtain the total duration, how many times it happened through her,
 * and who/when it was the last update.
 *
 */
public class TransitionSummary {
    private String id;
    private StatusDelegate fromStatus;
    private StatusDelegate toStatus;
    private Long duration;
    private String lastUpdater;
    private Timestamp lastUpdate;
    private List<Transition> transitions = new ArrayList<Transition>();
    private DateTimeFormatter userFormatter;

    /**
     * @param id an external ID generate.
     */
    public TransitionSummary(
            String id,
            StatusDelegate fromStatus,
            StatusDelegate toStatus,
            DateTimeFormatter userFormatter
    ) {
        setId(id);
        setFromStatus(fromStatus);
        setToStatus(toStatus);
        setDuration(new Long("0"));
        this.userFormatter = userFormatter;
    }

    /**
     * @param tran a simple Transition.
     *
     * Allows to add a transition and recalculate the summary values.
     */
    public void addTransition(Transition tran){
        transitions.add(tran);

        setLastUpdater(tran.getChangedBy());
        setLastupdate(tran.getChangedAt());

        addTime(tran.getDurationInMillis());
    }

    /**
     * @return a nice String format of the duration.
     */
    public String getDurationAsString(){
        return FormattableDuration.getFormatted(this.getDurationInMillis());
    }

    public int getTimesToTransition(){
        return transitions.size();
    }

    private void addTime(Long timeInMillis){
        setDuration(getDurationInMillis() + timeInMillis);
    }

    public String getId() {
        return id;
    }

    public StatusDelegate getFromStatus() {
        return fromStatus;
    }

    public StatusDelegate getToStatus() {
        return toStatus;
    }

    /**
     * @return a nice formatted date as String.
     */
    public String getLastUpdateAsString(){
        return this.userFormatter.format(lastUpdate);
    }

    /**
     * @return the lastUpdate
     */
    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public String getLastUpdater() {
        return lastUpdater;
    }

    private void setId(String id) {
        this.id = id;
    }

    public Long getDurationInMillis() {
        return duration;
    }

    private void setFromStatus(StatusDelegate fromStatus) {
        this.fromStatus = fromStatus;
    }

    private void setToStatus(StatusDelegate toStatus) {
        this.toStatus = toStatus;
    }

    private void setLastupdate(Timestamp lastupdate) {
        this.lastUpdate = lastupdate;
    }

    private void setLastUpdater(String lastUpdater) {
        this.lastUpdater = lastUpdater;
    }

    private void setDuration(Long duration) {
        this.duration = duration;
    }

}
