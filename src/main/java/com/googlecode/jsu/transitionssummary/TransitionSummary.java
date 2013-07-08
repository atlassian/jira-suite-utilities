package com.googlecode.jsu.transitionssummary;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.atlassian.jira.datetime.DateTimeFormatter;
import com.atlassian.jira.issue.status.Status;

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
    private Status fromStatus;
    private Status toStatus;
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
            Status fromStatus,
            Status toStatus,
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
        String retVal;
        long duration = this.getDurationInMillis();

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

    public int getTimesToTransition(){
        return transitions.size();
    }

    private void addTime(Long timeInMillis){
        setDuration(getDurationInMillis() + timeInMillis);
    }

    public String getId() {
        return id;
    }

    public Status getFromStatus() {
        return fromStatus;
    }

    public Status getToStatus() {
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

    private void setFromStatus(Status fromStatus) {
        this.fromStatus = fromStatus;
    }

    private void setToStatus(Status toStatus) {
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
