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
     * @param fromStatus
     * @param toStatus
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
        String retVal = "";
        Long duration = this.getDurationInMillis();
        
        long daysInWeek = 5;
        long hoursInDay = 8;
        long minutesInHour = 60;
        long secondsInMinute = 60;

        long millisInMinute = secondsInMinute * getMillisInSecond();
        long millisInHour = millisInMinute * minutesInHour;
        long millisInDay = millisInHour * hoursInDay;
        long millisInWeek = daysInWeek * millisInDay;

        if(duration!=0){
            Long weeks = new Long(duration.longValue() / millisInWeek);
            Long restWeek = new Long(duration.longValue() % millisInWeek);

            Long days = new Long(restWeek.longValue() / millisInDay);
            Long restDay = new Long(restWeek.longValue() % millisInDay);

            Long hours = new Long(restDay.longValue() / millisInHour);
            Long resthours = new Long(restDay.longValue() % millisInHour);

            Long minutes = new Long(resthours.longValue() / millisInMinute);
            Long restMinutes = new Long(resthours.longValue() % millisInMinute);

            Long seconds = new Long(restMinutes.longValue() / getMillisInSecond());

            // If it has been days, it does not have sense to show the seconds.
            retVal = weeks.equals(new Long("0"))?"":String.valueOf(days) + "w ";
            retVal = retVal + (days.equals(new Long("0"))?"":String.valueOf(days) + "d ");
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

    public long getMillisInSecond(){
    	return 1000;
    }

    public int getTimesToTransition(){
        return transitions.size();
    }

    private void addTime(Long timeInMillis){
        setDuration(new Long(getDurationInMillis().longValue() + timeInMillis.longValue()));
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
    
    public Long getDurationInSeconds() {
        return duration/getMillisInSecond();
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
